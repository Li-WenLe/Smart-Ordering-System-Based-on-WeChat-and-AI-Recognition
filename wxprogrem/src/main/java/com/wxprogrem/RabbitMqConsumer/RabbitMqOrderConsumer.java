package com.wxprogrem.RabbitMqConsumer;

import com.wxprogrem.config.RabbitMqConfiguration;
import com.wxprogrem.pojo.Order;
import com.wxprogrem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqOrderConsumer {
    @Autowired
    private OrderService orderService;
    @RabbitListener(queues = RabbitMqConfiguration.ORDER_DLX_QUEUE)
    public void RabbitMqOrderTimeOutConsumer(Order order) {
        log.info("未支付订单已进入消费队列");
        //查验数据库订单状态
        Integer orderId = order.getId();
        Integer status = orderService.findStatus(String.valueOf(orderId));
        //如果已经支付不做处理
        if (status == 2) {
            return;
        }
        //如果仍然没有支付
        if (status == 1) {
            //设置状态为取消状态
            orderService.updateStatusByOrderId(orderId, 3);
        }
    }
}
