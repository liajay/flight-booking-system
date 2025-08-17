package com.liajay.flightbooking;

import javax.validation.ValidationException;

import com.liajay.flightbooking.service.exception.LoginException;
import com.liajay.flightbooking.util.exception.BizException;
import com.liajay.flightbooking.web.response.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * 全局异常处理器
 * @author liajay
 * @since 2025-8-16
 */
//todo 完善日志记录
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     * @param e
     * @return  返回400
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public HttpResponse<String> handleValidationException(ValidationException e) {
        return HttpResponse.error("Validation failed: " + e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public HttpResponse<String> handleValidationException(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        for(ObjectError error : e.getAllErrors()){
            stringBuilder.append(error.getDefaultMessage() + "\n");
        }

        return HttpResponse.error(stringBuilder.toString());
    }

    /**
     * 业务异常处理,不知道用哪个错误码好-_-?
     * @param e
     * @return 返回400
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public HttpResponse<String> handleBizException(BizException e) {
        return HttpResponse.error(e.getMessage());
    }

    /**
     * 登录出错
     * @param e
     * @return 返回401
     */
    @ExceptionHandler(value = LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public HttpResponse<String> handleLoginException(LoginException e){
        return HttpResponse.error(e.getMessage());
    }
}