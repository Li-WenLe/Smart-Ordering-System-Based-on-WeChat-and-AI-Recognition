package com.wxprogrem.mapper;

import com.wxprogrem.dto.OrderWithTime;
import com.wxprogrem.pojo.Dish;
import com.wxprogrem.pojo.Order;
import com.wxprogrem.utils.Result;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("select *from `order`")
    List<Order> getAllOrders();

    List<Order> search( @Param("phone") String phone,
                        @Param("orderId") String orderId,
                        @Param("status") Integer status);

    @Insert("insert into `order`(name,phone,total,user_id,address,status,order_time,payed_total,order_id) values (#{name},#{phone},#{total},#{userId},#{address},#{status},#{orderTime},#{payedTotal},#{orderId})")
    void addOrder(Order order);

    @Select("select *from `order` where status=#{status} and order_time<#{orderTime}")
    List<Order>getByStatusAndLtOrderTime(Integer status, LocalDateTime orderTime);


    void update(Order order);

    @Select("SELECT order_id \n" +
            "    FROM `order` \n" +
            "    WHERE user_id = #{userId}\n" +
            "    ORDER BY order_time DESC \n" +
            "    LIMIT 1")
    String getNew(int userId);

    @Update("update `order` set status =#{status} where order_id=#{orderId}")
    void updateStatus(String orderId,int status);

    @Select("select order_id from `order` where user_id=#{userId} order by order_time ")
    List<String> getAllOrderId(@Param("userId") Integer userId);

    @Select("select status from `order` where order_id=#{orderId}")
    Integer findStatus(String orderId);

    // OrderMapper.java

    /**
     * 批量查询订单状态
     */
    @Select("<script>" +
            "SELECT order_id, status FROM `order` " +
            "WHERE order_id IN " +
            "<foreach item='id' collection='list' open='(' separator=',' close=')'>" +
            "   #{id}" +
            "</foreach>" +
            "order by order_time"+
            "</script>")
    List<Order> selectOrderStatusByIds(List<String> orderIds);

    @Select("select *from `order` where order_time <=#{now} and order_time>=#{start}")
    List<Order> getTodayOrder(LocalDateTime start,LocalDateTime now);

    @Select("select *from `order` where order_time <=#{now} and order_time>=#{startOfDay} and status in(0,2,4)")
    List<Order> getTodayRealOrder(LocalDateTime startOfDay, LocalDateTime now);

    @Select("select *from `order` where order_time <=#{now} and order_time>=#{startOfDay} and status=0")
    List<Order> getTodayComplateOrder(LocalDateTime startOfDay, LocalDateTime now);

    @Select("select *from `order` where status  in(0,2,4)")
    List<Order> getAllRealOrders();
    @Select("select *from `order` where status=0")
    List<Order> getAllComplateOrders();

    @Select("select order_id from `order` where status in(0,2,4)")
    List<String> getAllRealOrderIds();
    @Select("select order_id from `order` where status in(0,2,4) and order_time>=#{start} and order_time<=#{now}")
    List<String> getTodayRealOrderIds(LocalDateTime start, LocalDateTime now);


    List<OrderWithTime> selectOrdersWithTimeByUserId(int userId);

    @Select("select order_id from `order` where status=#{status} and user_id=#{userId} ")
    List<String> selectOrderIdsByUserIdAndStatus(int userId, int status);

    @Update("update `order` set status =#{status},update_time=#{updateTime} where order_id=#{orderId}")
    void updateStatusWithTime(String orderId, int status, LocalDateTime updateTime);
}
