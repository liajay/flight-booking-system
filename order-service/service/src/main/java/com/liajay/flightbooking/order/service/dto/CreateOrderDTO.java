package com.liajay.flightbooking.order.service.dto;

import java.math.BigDecimal;

/**
 * 创建订单DTO
 * 
 * @author liajay
 */
public class CreateOrderDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 航班号
     */
    private String flightNumber;
    
    /**
     * 座位号
     */
    private String seatNumber;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;

    // 构造函数
    public CreateOrderDTO() {
    }

    public CreateOrderDTO(Long userId, String flightNumber, String seatNumber, BigDecimal amount) {
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.amount = amount;
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

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CreateOrderDTO{" +
                "userId=" + userId +
                ", flightNumber='" + flightNumber + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
