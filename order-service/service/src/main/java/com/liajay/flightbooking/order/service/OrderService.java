package com.liajay.flightbooking.order.service;

import com.liajay.flightbooking.order.model.vo.OrderVO;
import com.liajay.flightbooking.order.service.dto.CreateOrderDTO;
import com.liajay.flightbooking.order.service.dto.OrderQueryResultDTO;

/**
 * 订单服务接口
 * 
 * @author liajay
 */
public interface OrderService {
    
    /**
     * 创建订单
     * 
     * @param createOrderDTO 创建订单DTO
     * @return 订单VO
     */
    OrderVO createOrder(CreateOrderDTO createOrderDTO);
    
    /**
     * 根据订单编号查询订单
     * 
     * @param orderNumber 订单编号
     * @return 订单VO
     */
    OrderVO getOrderByNumber(String orderNumber);
    
    /**
     * 根据用户ID分页查询订单
     * 
     * @param userId 用户ID
     * @param page 页码（从0开始）
     * @param size 页大小
     * @return 订单查询结果
     */
    OrderQueryResultDTO getOrdersByUserId(Long userId, int page, int size);
}
