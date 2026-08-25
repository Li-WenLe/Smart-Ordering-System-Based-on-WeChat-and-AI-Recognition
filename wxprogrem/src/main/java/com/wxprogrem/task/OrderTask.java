//package com.wxprogrem.task;
//
//import com.wxprogrem.mapper.OrderMapper;
//import com.wxprogrem.pojo.Order;
//import com.wxprogrem.service.OrderService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@Slf4j
//public class OrderTask {
//    @Autowired
//    private OrderMapper orderMapper;
//    @Scheduled(cron ="0 * * * * ?" )
//    public void task() {
//        log.info("处理超时订单：{}", LocalDateTime.now());
//        //待支付时长超过15分钟自动取消订单
//        LocalDateTime threshold = LocalDateTime.now().plusMinutes(-15);
//        log.info("阈值时间：{}", threshold);
//        List<Order> list=orderMapper.getByStatusAndLtOrderTime(1, threshold);
//        //List<Order> list=orderMapper.getByStatusAndLtOrderTime(1,LocalDateTime.now().plusMinutes(-15));
//        log.info("查询到{}条待取消订单", list.size());
//        if(list!=null&&list.size()>0){
//            //修改订单状态为已取消
//            for(Order order:list){
//                //3,表示已取消
//                order.setStatus(3);
//                order.setUpdateTime(LocalDateTime.now());
//                orderMapper.update(order);
//            }
//        }
//
//    }
//}
