package com.liajay.flightbooking.user.util.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码校验注解
 * 校验规则：
 * 1. 长度在8-16个字符之间
 * 2. 至少包含一个字母
 * 3. 至少包含一个数字
 * 4. 至少包含一个特殊字符
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    
    /**
     * 默认错误消息
     */
    String message() default "密码必须为8-16位，且包含字母、数字和特殊字符";
    
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
    int min() default 8;
    
    /**
     * 最大长度
     */
    int max() default 16;
    
    /**
     * 是否需要字母
     */
    boolean requireLetter() default true;
    
    /**
     * 是否需要数字
     */
    boolean requireDigit() default true;
    
    /**
     * 是否需要特殊字符
     */
    boolean requireSpecialChar() default true;
    
    /**
     * 允许的特殊字符
     */
    String specialChars() default "!@#$%^&*()_+-=[]{}|;:,.<>?";
}
