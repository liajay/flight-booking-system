package com.liajay.flightbooking.order.service.impl;

import com.liajay.flightbooking.order.dal.dataobject.Order;
import com.liajay.flightbooking.order.dal.mapper.OrderMapper;
import com.liajay.flightbooking.order.model.vo.OrderVO;
import com.liajay.flightbooking.order.service.OrderService;
import com.liajay.flightbooking.order.service.dto.CreateOrderDTO;
import com.liajay.flightbooking.order.service.dto.OrderQueryResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 * 
 * @author liajay
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderMapper orderMapper;
    
    // 简单的订单号生成器（实际项目中可能需要更复杂的分布式ID生成策略）
    private static final AtomicLong ORDER_NUMBER_COUNTER = new AtomicLong(1);

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(CreateOrderDTO createOrderDTO) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(createOrderDTO.getUserId());
        order.setFlightNumber(createOrderDTO.getFlightNumber());
        order.setSeatNumber(createOrderDTO.getSeatNumber());
        order.setAmount(createOrderDTO.getAmount());
        order.setStatus("PENDING");

        int result = orderMapper.insert(order);
        if (result <= 0) {
            throw new RuntimeException("创建订单失败");
        }

        return convertToVO(order);
    }

    @Override
    public OrderVO getOrderByNumber(String orderNumber) {
        Order order = orderMapper.findByOrderNumber(orderNumber);
        if (order == null) {
            return null;
        }
        return convertToVO(order);
    }

    @Override
    public OrderQueryResultDTO getOrdersByUserId(Long userId, int page, int size) {
        // 获取总数
        long total = orderMapper.countByUserId(userId);
        
        if (total == 0) {
            return new OrderQueryResultDTO(List.of(), 0, page + 1, size);
        }
        
        // 分页查询
        int offset = page * size;
        List<Order> orders = orderMapper.findByUserId(userId, offset, size);
        
        // 转换为VO
        List<OrderVO> orderVOs = orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new OrderQueryResultDTO(orderVOs, total, page + 1, size);
    }
    
    /**
     * 生成订单编号
     * 格式：ORD + 年月日 + 6位递增序号
     */
    private String generateOrderNumber() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long counter = ORDER_NUMBER_COUNTER.getAndIncrement();
        return String.format("ORD%s%06d", dateStr, counter % 1000000);
    }
    
    /**
     * 转换为VO对象
     */
    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNumber(order.getOrderNumber());
        vo.setUserId(order.getUserId());
        vo.setFlightNumber(order.getFlightNumber());
        vo.setSeatNumber(order.getSeatNumber());
        vo.setAmount(order.getAmount());
        vo.setStatus(order.getStatus());
        return vo;
    }

}
