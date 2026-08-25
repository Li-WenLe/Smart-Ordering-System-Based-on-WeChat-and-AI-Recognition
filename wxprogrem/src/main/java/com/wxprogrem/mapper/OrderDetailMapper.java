package com.wxprogrem.mapper;

import com.wxprogrem.pojo.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface OrderDetailMapper {
    void addOrderDetail(List<OrderDetail> orderDetail);

    @Select("select *from order_detail where order_id=#{orderId}")
    List<OrderDetail> getDetail(String orderId);


    List<OrderDetail> historyOrder(Set<String> orderIds);

    @Select("SELECT *from order_detail where order_id=#{orderId}")
    List<OrderDetail> getOrderDetailByOrderId(String orderId);


    List<OrderDetail> getOrderDetailsByOrderIds(List<String> realOrderIds);

    @Insert("INSERT INTO order_detail VALUES (#{id},#{dishname},#{image},#{orderId},#{dishId},#{number},#{acount})")
    void addorderdetails(OrderDetail orderDetail);
}
