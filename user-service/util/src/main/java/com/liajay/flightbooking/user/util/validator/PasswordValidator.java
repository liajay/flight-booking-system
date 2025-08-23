package com.liajay.flightbooking.user.util.validator;



import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 密码校验器实现
 * 校验规则：
 * 1. 长度在8-16个字符之间
 * 2. 至少包含一个字母（大写或小写）
 * 3. 至少包含一个数字
 * 4. 至少包含一个特殊字符
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    
    private int min;
    private int max;
    private boolean requireLetter;
    private boolean requireDigit;
    private boolean requireSpecialChar;
    private String specialChars;
    
    // 预编译正则表达式以提高性能
    private Pattern letterPattern = Pattern.compile("[a-zA-Z]");
    private Pattern digitPattern = Pattern.compile("[0-9]");
    private Pattern specialCharPattern;
    
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.requireLetter = constraintAnnotation.requireLetter();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
        this.specialChars = constraintAnnotation.specialChars();
        
        // 动态构建特殊字符正则表达式
        if (requireSpecialChar) {
            // 转义特殊字符以用于正则表达式
            String escapedSpecialChars = Pattern.quote(specialChars).replaceAll("\\\\(.)", "\\\\$1");
            specialCharPattern = Pattern.compile("[" + escapedSpecialChars + "]");
        }
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }

        // 长度校验
        if (password.length() < min || password.length() > max) {
            updateErrorMessage(context, String.format("密码长度必须在%d-%d个字符之间", min, max));
            return false;
        }

        // 字母校验
        if (requireLetter && !letterPattern.matcher(password).find()) {
            updateErrorMessage(context, "密码必须包含至少一个字母");
            return false;
        }

        // 数字校验
        if (requireDigit && !digitPattern.matcher(password).find()) {
            updateErrorMessage(context, "密码必须包含至少一个数字");
            return false;
        }

        // 特殊字符校验
        if (requireSpecialChar && !specialCharPattern.matcher(password).find()) {
            updateErrorMessage(context, "密码必须包含至少一个特殊字符：" + specialChars);
            return false;
        }

        // 检查是否包含非法字符
        if (!isValidCharacters(password)) {
            updateErrorMessage(context, "密码只能包含字母、数字和以下特殊字符：" + specialChars);
            return false;
        }

        return true;
    }
    
    /**
     * 检查密码是否只包含有效字符
     */
    private boolean isValidCharacters(String password) {
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && specialChars.indexOf(c) == -1) {
                return false;
            }
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
