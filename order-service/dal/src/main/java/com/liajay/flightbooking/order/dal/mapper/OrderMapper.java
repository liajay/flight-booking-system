package com.liajay.flightbooking.order.dal.mapper;

import com.liajay.flightbooking.order.dal.dataobject.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单Mapper接口
 * 
 * @author liajay
 */
@Mapper
public interface OrderMapper {
    
    /**
     * 新增订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    int insert(Order order);
    
    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    Order findById(@Param("id") Long id);
    
    /**
     * 根据订单编号查询订单
     * 
     * @param orderNumber 订单编号
     * @return 订单信息
     */
    Order findByOrderNumber(@Param("orderNumber") String orderNumber);
    
    /**
     * 根据用户ID查询订单列表
     * 
     * @param userId 用户ID
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 订单列表
     */
    List<Order> findByUserId(@Param("userId") Long userId, 
                           @Param("offset") int offset, 
                           @Param("pageSize") int pageSize);
    
    /**
     * 根据用户ID统计订单总数
     * 
     * @param userId 用户ID
     * @return 订单总数
     */
    long countByUserId(@Param("userId") Long userId);

}
