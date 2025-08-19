package com.liajay.flightbooking.user.util.validator;

/**
 * 校验工具类
 * 提供快速校验方法，不依赖注解
 */
public class ValidationUtils {
    
    /**
     * 校验用户名
     * @param username 用户名
     * @return 校验结果
     */
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return ValidationResult.error("用户名不能为空");
        }
        
        if (username.length() < 5 || username.length() > 32) {
            return ValidationResult.error("用户名长度必须在5-32个字符之间");
        }
        
        if (!username.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            return ValidationResult.error("用户名只能包含字母、数字、下划线，且必须以字母开头");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * 校验密码
     * @param password 密码
     * @return 校验结果
     */
    public static ValidationResult validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return ValidationResult.error("密码不能为空");
        }
        
        if (password.length() < 8 || password.length() > 16) {
            return ValidationResult.error("密码长度必须在8-16个字符之间");
        }
        
        if (!password.matches(".*[a-zA-Z].*")) {
            return ValidationResult.error("密码必须包含至少一个字母");
        }
        
        if (!password.matches(".*[0-9].*")) {
            return ValidationResult.error("密码必须包含至少一个数字");
        }
        
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*")) {
            return ValidationResult.error("密码必须包含至少一个特殊字符");
        }
        
        return ValidationResult.success();
    }
    
    /**
     * 校验结果类
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return valid ? "Valid" : "Invalid: " + message;
        }
    }
}
