package com.liajay.flightbooking.order.service.dto;

import java.math.BigDecimal;

/**
 * 创建订单并自动分配座位DTO
 * 
 * @author liajay
 */
public class CreateOrderWithSeatAllocationDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 航班号
     */
    private String flightNumber;

    // 构造函数
    public CreateOrderWithSeatAllocationDTO() {
    }

    public CreateOrderWithSeatAllocationDTO(Long userId, String flightNumber) {
        this.userId = userId;
        this.flightNumber = flightNumber;
    }

    // Getter和Setter方法
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String toString() {
        return "CreateOrderWithSeatAllocationDTO{" +
                "userId=" + userId +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
