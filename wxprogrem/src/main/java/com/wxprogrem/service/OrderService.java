package com.wxprogrem.service;

import com.wxprogrem.dto.OrderWithTime;
import com.wxprogrem.pojo.Order;
import com.wxprogrem.pojo.OrderDetail;
import com.wxprogrem.pojo.PageBean;
import com.wxprogrem.utils.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {

    PageBean<Order> getAll(int pageSize, int pageNum);

    PageBean<Order> search(int pageSize, int pageNum,String phone,int status,String orderId);

    void addOrder(Order order);

    String getNew(int userId);

    void addOrderDetail(List<OrderDetail> orderDetail);

    void addOrderDetailS(OrderDetail orderDetail);
    void updateStatus(String orderId,int status);

    List<OrderDetail> getDetail(String orderId);

   //// List<OrderDetail> historyOrder(List<String>orders);

    List<String> getAllOrderId(int userId);

    List<OrderDetail> historyOrder(Set<String> orderIds);

    Integer findStatus(String orderId);

    Map<String, Integer> getOrderStatusMap(List<String> orderIds);

    List<OrderDetail> getOrderDetailByOrderId(String orderId);

    List<Order> getTodayOrder(LocalDateTime start,LocalDateTime now);

    List<Order> getTodayRealOrder(LocalDateTime startOfDay, LocalDateTime now);

    List<Order> getTodayComplateOrder(LocalDateTime startOfDay, LocalDateTime now);

    List<Order> getAllOrders();

    List<Order> getAllRealOrders();

    List<Order> getAllComplateOrders();

    List<String> getAllRealOrderIds();

    List<OrderDetail> getOrderDetailsByOrderIds(List<String> realOrderIds);

    List<String> getTodayRealOrderIds();

    List<OrderWithTime> getAllOrdersWithTime(int userId);

    List<String> getOrderIdsByStatus(int userId, int i);
}
