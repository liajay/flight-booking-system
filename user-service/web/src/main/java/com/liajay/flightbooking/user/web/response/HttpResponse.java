package com.liajay.flightbooking.user.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public HttpResponse() {
    }

    private HttpResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

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

    public static <T> HttpResponse<T> success(T data) {
        return new HttpResponse<>(true, null, data);
    }

    public static <T> HttpResponse<T> error(String message) {
        return new HttpResponse<>(false, message, null);
    }
}
