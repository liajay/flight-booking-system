package com.liajay.flightbooking.order.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一响应结果
 * 
 * @author liajay
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<T> {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;

    // 私有构造函数
    private HttpResponse() {
    }

    private HttpResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 静态工厂方法
    public static <T> HttpResponse<T> success(T data) {
        return new HttpResponse<>(true, null, data);
    }

    public static <T> HttpResponse<T> success() {
        return new HttpResponse<>(true, null, null);
    }

    public static <T> HttpResponse<T> error(String message) {
        return new HttpResponse<>(false, message, null);
    }

    public static <T> HttpResponse<T> error(String message, T data) {
        return new HttpResponse<>(false, message, data);
    }

    // Getter和Setter方法
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
