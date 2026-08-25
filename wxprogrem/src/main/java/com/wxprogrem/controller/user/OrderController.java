package com.wxprogrem.controller.user;

import com.wxprogrem.dto.OrderDetailDTO;
import com.wxprogrem.dto.OrderWithTime;
import com.wxprogrem.pojo.Order;
import com.wxprogrem.pojo.OrderDetail;
import com.wxprogrem.service.OrderService;
import com.wxprogrem.service.WebSocketServer;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonWriteLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wxprogrem.config.RabbitMqConfiguration.*;
import static com.wxprogrem.config.RabbitMqConfiguration.DELAYED_BUSINESS_QUEUE;
import static com.wxprogrem.constants.Constants.*;
import static java.lang.Integer.parseInt;

@CrossOrigin
@Slf4j
@RestController("UserOrderController")
@RequestMapping("/user/order")
@Tag(name = "用户端订单相关",description = "用户端订单相关api")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "添加用户订单信息",description = "添加用户订单信息")
    @PostMapping("/add")
    public Result addOrder(@RequestBody Order order) {
        log.info("订单插入--前端传递订单参数：order：{}", order);
        orderService.addOrder(order);
        try {
            WebSocketServer.sendInfo("你有新的订单待处理");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success();
    }

    @Operation(summary = "添加用户订单详情信息",description = "添加用户订单详情信息")
    @PostMapping("/addorderdetail")
    public Result addOrderDetail(@RequestBody List<OrderDetail> orderDetailList) {
        log.info("接收到订单详情：数量={}", orderDetailList.size());
        for (OrderDetail detail : orderDetailList) {
            log.info("订单项：orderId={}, dishname={}, amount={}", detail.getOrderId(), detail.getDishname(), detail.getAcount());
            //redis库存扣减
            String dishInventory=stringRedisTemplate.opsForValue().get(DISHINVENTORYID+detail.getDishId());
            stringRedisTemplate.opsForValue().set(DISHINVENTORYID+detail.getDishId(),String.valueOf(parseInt(dishInventory)-1));
            rabbitTemplate.convertAndSend(CREATORDERWITHSTOCK_BUSINESS_EXCHANGE,CREATORDERWITHSTOCK_BUSINESS_QUEUE,detail);
            log.info("消息已发送至交换器：{}，路由键：{}，内容：{}",
                    CREATORDERWITHSTOCK_BUSINESS_EXCHANGE, CREATORDERWITHSTOCK_BUSINESS_QUEUE,detail);
        }
        //orderService.addOrderDetail(orderDetail);
        return Result.success();
    }


    @Operation(summary = "根据用户id获取用户的最新订单ID",description = "根据用户id获取用户的最新订单id")
    @PostMapping("/getnew")
    public Result getNew(@RequestBody Order order) {
        int userId = order.getUserId();
        String orderId = orderService.getNew(userId);
        log.info("获取的订单号：{}", orderId);
        return Result.success(orderId);
    }

    @Operation(summary = "根据用户id获取用户的最新订单ID",description = "根据用户id获取用户的最新订单id")
    //根据用户id获取用户的历史订单
    @PostMapping("/history")
    public Result<List<OrderDetailDTO>> historyOrder(@RequestBody Map<String,String> map) {
        int userId = parseInt(map.get("userId"));
        log.info("查询用户历史订单，用户ID: {}", userId);
        // 1. 获取用户所有订单ID
        List<String> orderIds = orderService.getAllOrderId(userId);
        log.info("订单集合：{}", orderIds);
        if (orderIds.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        // 2. 批量获取订单状态 (减少数据库查询次数)
        Map<String, Integer> orderStatusMap = orderService.getOrderStatusMap(orderIds);

        // 3. 获取订单详情
        List<OrderDetail> details = orderService.historyOrder(new HashSet<>(orderIds));

        // 4. 分组并转换为DTO
        List<OrderDetailDTO> result = details.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId))
                .entrySet().stream()
                .map(entry -> {
                    OrderDetailDTO dto = new OrderDetailDTO();
                    dto.setOrderId(entry.getKey());
                    dto.setItems(entry.getValue());
                    Integer status = orderStatusMap.get(entry.getKey());
                    dto.setStatus(orderStatusMap.get(entry.getKey())); // 从预取的map中获取状态

                    // 计算总金额
                    double total = entry.getValue().stream()
                            .mapToDouble(item -> item.getAcount() * item.getNumber())
                            .sum();
                    dto.setTotalPrice(total);

                    return dto;
                })
                .collect(Collectors.toList());

        return Result.success(result);
    }
    //根据订单号获取购买的商品信息
    @PostMapping("/get")
    public Result<List<OrderDetail>> getOrderDetail(@RequestBody Map<String,String> map) {
        String orderId = map.get("orderId");
        List<OrderDetail>list=orderService.getOrderDetailByOrderId(orderId);
        return Result.success(list);
    }
    @PostMapping("/getupay")
    public Result<List<OrderDetailDTO>> getUpayOrder(@RequestBody Map<String,String> map) {
        int userId = parseInt(map.get("userId"));
        log.info("查询用户待支付订单，用户ID: {}", userId);

        // 1. 获取用户所有待支付订单ID(status=1)
        List<String> orderIds = orderService.getOrderIdsByStatus(userId, 1);
        if (orderIds.isEmpty()) {
            return Result.success(Collections.emptyList());
        }
        log.info("订单集合{}",orderIds);
        // 2. 获取这些订单的详情
        List<OrderDetail> details = orderService.historyOrder(new HashSet<>(orderIds));

        // 3. 分组并转换为DTO
        List<OrderDetailDTO> result = details.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId))
                .entrySet().stream()
                .map(entry -> {
                    OrderDetailDTO dto = new OrderDetailDTO();
                    dto.setOrderId(entry.getKey());
                    dto.setItems(entry.getValue());
                    dto.setStatus(1); // 待支付订单状态固定为1

                    // 计算总金额
                    double total = entry.getValue().stream()
                            .mapToDouble(item -> item.getAcount() * item.getNumber())
                            .sum();
                    dto.setTotalPrice(total);

                    return dto;
                })
                .collect(Collectors.toList());

        return Result.success(result);
    }
    //根据orderId修改订单状态
    @PostMapping("/updatestatus")
    public Result updateOrderStatus(@RequestBody Map<String,String> map) {
        String orderId =map.get("orderId");
        orderService.updateStatus(orderId,2);
        return Result.success();
    }
}
