package com.wxprogrem.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wxprogrem.dto.OrderWithTime;
import com.wxprogrem.mapper.DishMapper;
import com.wxprogrem.mapper.OrderDetailMapper;
import com.wxprogrem.mapper.OrderMapper;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.Order;
import com.wxprogrem.pojo.OrderDetail;
import com.wxprogrem.pojo.PageBean;
import com.wxprogrem.service.OrderService;
import com.wxprogrem.utils.Result;
import com.wxprogrem.utils.SimpleDateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public List<String> getAllOrderId(int userId) {
        return orderMapper.getAllOrderId(userId);
    }

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public PageBean<Order> getAll(int pageSize, int pageNum) {
        PageBean<Order> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list= orderMapper.getAllOrders();
        Page<Order> p=(Page<Order>)list;
        pageBean.setItems(p);
        pageBean.setTotal((int) p.getTotal());
        return pageBean;
    }

    /*@Override
    public List<String> getAllOrderId(Integer userId) {
        return orderMapper.getAllOrderId(userId);
    }*/

    @Override
    public PageBean<Order> search(int pageSize, int pageNum, String phone, int status, String orderId) {
        PageBean<Order> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = orderMapper.search(phone,orderId,status);
        Page<Order>P= (Page<Order>)list;
        pageBean.setItems(P);
        pageBean.setTotal((int)P.getTotal());
        return pageBean;
    }

    @Override
    public void addOrder(Order order) {
        //添加时间戳+random生成的唯一订单号
        String orderId = SimpleDateFormatUtils.getOrderId();
        log.info("订单号：{}", orderId);
        Order newOrder = new Order();
        BeanUtil.copyProperties(order, newOrder);
        newOrder.setOrderId(orderId);
        orderMapper.addOrder(newOrder);
    }

    @Override
    public String getNew(int userId) {
        return orderMapper.getNew(userId);
    }

    @Override
    public void addOrderDetail(List<OrderDetail> orderDetail) {
        orderDetailMapper.addOrderDetail(orderDetail);
    }

    @Override
    public void updateStatus(String orderId,int status) {
        if(status==0||status==3){
            LocalDateTime update_time = LocalDateTime.now();
            orderMapper.updateStatusWithTime(orderId,status,update_time);
        }else{
            orderMapper.updateStatus(orderId,status);
        }
    }

    @Override
    public List<OrderDetail> getDetail(String orderId) {
        return orderDetailMapper.getDetail(orderId);
    }

   /* @Override
    public List<OrderDetail> historyOrder(List<Integer> orderIds) {
        return orderDetailMapper.historyOrder(orderIds);
    }

    @Override
    public List<String> getAllOrderId(int userId) {
        return List.of();
    }*/

    @Override
    public List<OrderDetail> historyOrder(Set<String> orderIds) {
        return orderDetailMapper.historyOrder(orderIds);
    }

    @Override
    public Integer findStatus(String orderId) {
        return orderMapper.findStatus(orderId);
    }
    @Override
    public Map<String, Integer> getOrderStatusMap(List<String> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return orderMapper.selectOrderStatusByIds(orderIds).stream()
                .collect(Collectors.toMap(Order::getOrderId, Order::getStatus));
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(String orderId) {
        return orderDetailMapper.getOrderDetailByOrderId(orderId);
    }

    @Override
    public List<Order> getTodayOrder(LocalDateTime start,LocalDateTime now) {
        return orderMapper.getTodayOrder(start,now);
    }

    @Override
    public List<Order> getTodayRealOrder(LocalDateTime startOfDay, LocalDateTime now) {
        return orderMapper.getTodayRealOrder(startOfDay,now);
    }

    @Override
    public List<Order> getTodayComplateOrder(LocalDateTime startOfDay, LocalDateTime now) {
        return orderMapper.getTodayComplateOrder(startOfDay,now);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public List<Order> getAllRealOrders() {
        return orderMapper.getAllRealOrders();
    }

    @Override
    public List<Order> getAllComplateOrders() {
        return orderMapper. getAllComplateOrders();
    }

    @Override
    public List<String> getAllRealOrderIds() {
        return  orderMapper.getAllRealOrderIds();
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderIds(List<String> realOrderIds) {
        return orderDetailMapper.getOrderDetailsByOrderIds(realOrderIds);
    }

    @Override
    public List<String> getTodayRealOrderIds() {
        LocalDateTime start=LocalDate.now().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        return orderMapper.getTodayRealOrderIds(start,now);
    }

    @Override
    public List<OrderWithTime> getAllOrdersWithTime(int userId) {
        return orderMapper.selectOrdersWithTimeByUserId(userId);
    }

    @Override
    public List<String> getOrderIdsByStatus(int userId, int status) {
        return orderMapper.selectOrderIdsByUserIdAndStatus(userId, status);
    }

    @Override
    public void addOrderDetailS(OrderDetail orderDetail) {
        orderDetailMapper.addorderdetails(orderDetail);
    }
}
