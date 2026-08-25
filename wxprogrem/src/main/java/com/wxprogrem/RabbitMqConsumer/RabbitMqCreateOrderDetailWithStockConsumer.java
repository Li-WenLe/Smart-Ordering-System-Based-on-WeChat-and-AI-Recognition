package com.wxprogrem.RabbitMqConsumer;

import com.wxprogrem.config.RabbitMqConfiguration;
import com.wxprogrem.pojo.OrderDetail;
import com.wxprogrem.service.DishService;
import com.wxprogrem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqCreateOrderDetailWithStockConsumer {
    @Autowired
    private OrderService orderService;
    @Autowired
    private DishService dishService;
    @RabbitListener(queues = RabbitMqConfiguration.CREATORDERWITHSTOCK_BUSINESS_QUEUE)
    public void OrderDetailOperation(OrderDetail orderDetail) {
        log.info("MQ异步处理库存扣减和订单入库---订单详情：{}", orderDetail);
        //订单插入，库存扣减
        orderService.addOrderDetailS(orderDetail);
        dishService.dishInventoryDeduct(orderDetail.getDishId(),orderDetail.getNumber());
        log.info("MQ落库成功");
        //
    }
}
