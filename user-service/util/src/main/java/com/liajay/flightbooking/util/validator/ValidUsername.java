package com.liajay.flightbooking.util.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用户名校验注解
 * 校验规则：长度在5-32个字符之间
 */
@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    
    /**
     * 默认错误消息
     */
    String message() default "用户名长度必须在5-32个字符之间";
    
    /**
     * 分组
     */
    Class<?>[] groups() default {};
    
    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 最小长度
     */
    int min() default 5;
    
    /**
     * 最大长度
     */
    int max() default 32;
}
