package com.liajay.flightbooking.order.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建订单并自动分配座位请求
 * 
 * @author liajay
 */
public class CreateOrderWithSeatAllocationRequest {
    /**
     * 航班号
     */
    @NotBlank(message = "航班号不能为空")
    private String flightNumber;

    // 构造函数
    public CreateOrderWithSeatAllocationRequest() {
    }

    public CreateOrderWithSeatAllocationRequest(String flightNumber) {
        this.flightNumber = flightNumber;
    }


    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String toString() {
        return "CreateOrderWithSeatAllocationRequest{" +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
