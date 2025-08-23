package com.liajay.flightbooking.order.util.exception;

/**
 * 订单业务异常
 * 
 * @author liajay
 */
public class OrderException extends RuntimeException {
    
    public OrderException(String message) {
        super(message);
    }
    
    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
