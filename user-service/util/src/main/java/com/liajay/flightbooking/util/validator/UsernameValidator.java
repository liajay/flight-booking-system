package com.liajay.flightbooking.util.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 用户名校验器实现
 * 校验规则：
 * 1. 长度在5-32个字符之间
 * 2. 只能包含字母、数字、下划线
 * 3. 必须以字母开头
 */
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    
    private int min;
    private int max;
    
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // 空值校验由@NotNull或@NotBlank处理
        if (username == null) {
            return true;
        }

        // 长度校验
        if (username.length() < min || username.length() > max) {
            updateErrorMessage(context, String.format("用户名长度必须在%d-%d个字符之间", min, max));
            return false;
        }

        // 格式校验：只能包含字母、数字、下划线，且必须以字母开头
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            updateErrorMessage(context, "用户名只能包含字母、数字、下划线，且必须以字母开头");
            return false;
        }

        return true;
    }

    
    /**
     * 更新错误消息
     */
    private void updateErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
