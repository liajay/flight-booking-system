package com.liajay.flightbooking.order.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * 创建订单请求
 * 
 * @author liajay
 */
public class CreateOrderRequest {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;
    
    /**
     * 航班号
     */
    @NotBlank(message = "航班号不能为空")
    private String flightNumber;
    
    /**
     * 座位号
     */
    @NotBlank(message = "座位号不能为空")
    private String seatNumber;
    
    /**
     * 订单金额
     */
    @NotNull(message = "订单金额不能为空")
    @Positive(message = "订单金额必须为正数")
    private BigDecimal amount;

    // 构造函数
    public CreateOrderRequest() {
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
        return "CreateOrderRequest{" +
                "userId=" + userId +
                ", flightNumber='" + flightNumber + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
