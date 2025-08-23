package com.liajay.flightbooking.order.dal.dataobject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据对象
 * 
 * @author liajay
 */
public class Order {

    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 订单编号
     */
    private String orderNumber;
    
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
    public Order() {
    }

    public Order(Long userId, String flightNumber, String seatNumber, BigDecimal amount) {
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.amount = amount;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

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
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", userId=" + userId +
                ", flightNumber='" + flightNumber + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
