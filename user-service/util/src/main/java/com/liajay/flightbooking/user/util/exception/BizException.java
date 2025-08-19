package com.liajay.flightbooking.user.util.exception;

/**
 * 由用户操作不当引起的异常
 * 比如：用户输入不合法的参数，或者请求的资源不存在等
 * @author liajay
 * @since 2025-8-15
 */
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }
    
}
