package com.wxprogrem.controller.admin;

import com.wxprogrem.dto.ShowOrderDetail;
import com.wxprogrem.pojo.Order;
import com.wxprogrem.pojo.OrderDetail;
import com.wxprogrem.pojo.PageBean;
import com.wxprogrem.service.OrderService;
import com.wxprogrem.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@CrossOrigin(maxAge = 3600)

@Tag(name="商家管理端订单管理",description = "商家管理端订单管理相关api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(summary = "分页查询所有订单信息",description = "分页查询所有订单信息")
    @GetMapping("/getall")              //分页查询所有订单信息
    public PageBean<Order> getAll(@RequestParam int pageSize,@RequestParam int pageNum) {
        log.info("分页查询所有订单信息--pageSize:{},pageNum:{}", pageSize, pageNum);
        return orderService.getAll(pageSize,pageNum);
    }


    @Operation(summary = "根据订单号，手机号，订单状态条件查询所有订单信息",description = "根据订单号，手机号，订单状态条件查询所有订单信息")
    @PostMapping("/search")             //根据订单号，手机号，订单状态条件查询所有订单信息
    public PageBean<Order> search(@RequestParam int pageSize,@RequestParam int pageNum,@RequestParam (required = false)String phone, @RequestParam (required = false)Integer status,@RequestParam (required = false)String orderId) {
        log.info("根据订单号，手机号，订单状态条件查询所有订单信息--pageSize：{},pageNum:{},phone:{},status:{},orderId:{}",pageSize,pageNum,phone,status,orderId);
        return orderService.search(pageSize,pageNum,phone,status,orderId);
    }


    @Operation(summary = "根据订单ID修改订单状态",description = "根据订单ID修改订单状态")
    @PostMapping("/updatestatus")        //根据orderId修改订单状态
    public Result updateStatus(@RequestBody Map<String, Object> params) {
        String orderId = (String) params.get("orderId");
        int status = (Integer) params.get("status");
        log.info("根据orderId修改订单状态--orderId:{},status:{}",orderId,status);
        orderService.updateStatus(orderId,status);
        return Result.success();
    }


    @Operation(summary = "根据订单id获取订单包含的商品详情信息",description = "根据订单id获取订单详情orderDetail")
    @PostMapping("/getdetail")            //根据订单id获取订单详情orderDetail
    public Result<List<OrderDetail>> getDetail(@RequestBody Map<String, Object> params) {
        String orderId = (String) params.get("orderId");
        log.info("根据订单id获取订单详情orderDetail--订单号：{}",orderId);
        List<OrderDetail>list=orderService.getDetail(orderId);
        return Result.success(list);
    }


    @Operation(summary = "获取当前时间为止的今日订单信息",description = "获取当前时间为止的今日订单信息")
    @GetMapping("/gettodayorder")          //获取当前时间为止的今日订单信息
    @CrossOrigin
    public Result<List<Order>> getTodayOrder() {
        LocalDateTime now=LocalDateTime.now();
        log.info("获取当前时间为止的今日订单信息**********");
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        log.info("今日启始时间：startOfDay：{}当前时间：now:{}",startOfDay,now);
        List<Order>list= orderService.getTodayOrder(startOfDay,now);
        return Result.success(list);
    }


    @Operation(summary = "获取当前时间为止的今日有效订单",description = "获取当前时间为止的今日有效订单{已完成，待接单（已付款），已送达}")
    @GetMapping("/gettodayrealorder")     //获取当前时间为止的今日有效订单{已完成，待接单（已付款），已送达}
    @CrossOrigin
    public Result<List<Order>> getTodayRealOrder() {
        log.info("获取当前时间为止的今日有效订单信息**********");
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        log.info("今日启始时间：startOfDay：{}当前时间：now:{}",startOfDay,now);
        List<Order>list= orderService.getTodayRealOrder(startOfDay,now);
        return Result.success(list);
    }


    @Operation(summary = "获取今日已完成状态的订单",description = "获取今日已完成状态的订单")
    @GetMapping("/gettodaycomplateorder")     //获取今日已完成的订单
    @CrossOrigin
    public Result<List<Order>> getTodayComplateOrder() {
        log.info("获取当前时间为止的今日已完成订单信息**********");
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        log.info("今日启始时间：startOfDay：{}当前时间：now:{}",startOfDay,now);
        List<Order>list=orderService.getTodayComplateOrder(startOfDay,now);
        return Result.success(list);
    }


    @Operation(summary = "获取所有订单信息",description = "获取所有订单信息")
    @GetMapping("/getallorders")              //获取所有订单信息
    @CrossOrigin
    public Result<List<Order>> getAllOrders() {
        List<Order>list=orderService.getAllOrders();
        return Result.success(list);
    }


    @Operation(summary = "获取所有有效订单",description = "获取所有有效订单")
    @GetMapping("/getallrealorders")           //获取所有有效订单
    @CrossOrigin
    public Result<List<Order>> getAllRealOrders() {
        log.info("获取所有有效订单信息**********");
        List<Order>list=orderService.getAllRealOrders();
        return Result.success(list);
    }


    @Operation(summary = "获取所有已完成状态的订单")
    @GetMapping("/getallcomplateorders")        //获取所有已完成的订单
    public Result<List<Order>> getAllComplateOrders() {
        log.info("获取所有已完成订单信息**********");
        List<Order>list=orderService. getAllComplateOrders();
        return Result.success(list);
    }

    @Operation(summary = "商品总销量排行",description ="获取商品总销量排行")
    @GetMapping("/alltop")                       //获取商品总销量排行
    public Result<List<ShowOrderDetail>> getAllTop() {
        // 获取所有有效订单的订单号
        List<String> realOrderIds = orderService.getAllRealOrderIds();

        // 根据有效订单号获取所有的orderDetail
        List<OrderDetail> allOrderDetails = orderService.getOrderDetailsByOrderIds(realOrderIds);

        // 统计每种商品的销售总数量
        Map<Integer, Integer> dishSales = new HashMap<>();
        Map<Integer, OrderDetail> dishInfoMap = new HashMap<>();

        for (OrderDetail detail : allOrderDetails) {
            int dishId = detail.getDishId();       //定义并获取菜品id
            int quantity = detail.getNumber();     //获取订单的对应商品的数量
                                                   // 累计销售数量，不存在菜品id,插入菜品id和对应的数量，存在则累加商品的数量
            dishSales.merge(dishId, quantity, Integer::sum);

            // 保存商品信息（名称、图片等），如果map中不包含对应的菜品id，就把对应的菜品信息插入
            if (!dishInfoMap.containsKey(dishId)) {
                dishInfoMap.put(dishId, detail);
            }
        }

        // 转换为结果列表
        List<ShowOrderDetail> result = new ArrayList<>();
        //遍历键值对
        for (Map.Entry<Integer, Integer> entry : dishSales.entrySet()) {
            int dishId = entry.getKey();
            int totalSales = entry.getValue();
            OrderDetail detail = dishInfoMap.get(dishId);

            ShowOrderDetail showDetail = new ShowOrderDetail();
            showDetail.setDishId(dishId);
            showDetail.setDishname(detail.getDishname());
            showDetail.setImage(detail.getImage());
            showDetail.setTotalSales(totalSales);      //插入总销售数量
            showDetail.setTotalAmount(detail.getAcount() * totalSales / detail.getNumber());

            result.add(showDetail);
        }

        // 按销售数量降序排序
        result.sort((a, b) -> b.getTotalSales() - a.getTotalSales());

        return Result.success(result);
    }



    @Operation(summary = "商品今日销量排行",description = "商品今日销量排行")
    @GetMapping("/todaytop")                  //获取商品今日销量排行
    public Result<List<ShowOrderDetail>> getTodayTop() {
        // 获取所有有效订单的订单号
        List<String> realOrderIds = orderService.getTodayRealOrderIds();

        // 根据有效订单号获取所有的orderDetail
        List<OrderDetail> allOrderDetails = orderService.getOrderDetailsByOrderIds(realOrderIds);

        // 统计每种商品的销售总数量
        Map<Integer, Integer> dishSales = new HashMap<>();
        Map<Integer, OrderDetail> dishInfoMap = new HashMap<>();

        for (OrderDetail detail : allOrderDetails) {
            int dishId = detail.getDishId();
            int quantity = detail.getNumber();

            // 累计销售数量
            dishSales.merge(dishId, quantity, Integer::sum);

            // 保存商品信息（名称、图片等）
            if (!dishInfoMap.containsKey(dishId)) {
                dishInfoMap.put(dishId, detail);
            }
        }

        // 转换为结果列表
        List<ShowOrderDetail> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : dishSales.entrySet()) {
            int dishId = entry.getKey();
            int totalSales = entry.getValue();
            OrderDetail detail = dishInfoMap.get(dishId);

            ShowOrderDetail showDetail = new ShowOrderDetail();
            showDetail.setDishId(dishId);
            showDetail.setDishname(detail.getDishname());
            showDetail.setImage(detail.getImage());
            showDetail.setTotalSales(totalSales);
            showDetail.setTotalAmount(detail.getAcount() * totalSales / detail.getNumber());

            result.add(showDetail);
        }

        // 按销售数量降序排序
        result.sort((a, b) -> b.getTotalSales() - a.getTotalSales());

        return Result.success(result);
    }
}
