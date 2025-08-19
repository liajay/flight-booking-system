package com.liajay.flightbooking.user.util.validator;

/**
 * 校验器使用示例
 */
public class ValidatorExample {
    
    public static void main(String[] args) {
        // 测试用户名校验
        System.out.println("=== 用户名校验测试 ===");
        testUsername("admin", true);           // 有效用户名
        testUsername("user123", true);         // 有效用户名
        testUsername("test_user", true);       // 有效用户名
        testUsername("ab", false);             // 太短
        testUsername("123user", false);        // 以数字开头
        testUsername("user-name", false);      // 包含连字符
        testUsername("verylongusernamethatexceedsthemaximumlength", false); // 太长
        
        System.out.println("\n=== 密码校验测试 ===");
        // 测试密码校验
        testPassword("Abc123!@", true);        // 有效密码
        testPassword("MyPass123!", true);      // 有效密码
        testPassword("password", false);       // 缺少数字和特殊字符
        testPassword("12345678", false);       // 缺少字母和特殊字符
        testPassword("Abc123", false);         // 缺少特殊字符
        testPassword("Ab1!", false);           // 太短
        testPassword("VeryLongPasswordThatExceedsTheMaximumLength123!", false); // 太长
    }
    
    private static void testUsername(String username, boolean expectedValid) {
        ValidationUtils.ValidationResult result = ValidationUtils.validateUsername(username);
        System.out.printf("用户名: %-20s | 期望: %-5s | 实际: %-5s | %s%n", 
            "\"" + username + "\"", 
            expectedValid ? "有效" : "无效", 
            result.isValid() ? "有效" : "无效",
            result.isValid() ? "✓" : "✗ " + result.getMessage());
    }
    
    private static void testPassword(String password, boolean expectedValid) {
        ValidationUtils.ValidationResult result = ValidationUtils.validatePassword(password);
        System.out.printf("密码: %-20s | 期望: %-5s | 实际: %-5s | %s%n", 
            "\"" + password + "\"", 
            expectedValid ? "有效" : "无效", 
            result.isValid() ? "有效" : "无效",
            result.isValid() ? "✓" : "✗ " + result.getMessage());
    }
}
