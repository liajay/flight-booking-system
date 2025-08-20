package com.liajay.flightbooking.inventory.util.exception;

/**
 * 业务异常类
 */
public class BizException extends RuntimeException {
    
    private String errorCode;
    
    public BizException(String message) {
        super(message);
    }
    
    public BizException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BizException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
