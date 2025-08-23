package com.liajay.flightbooking.inventory.util;

/**
 * 用户上下文工具类
 * 用于在下游服务中获取网关传递的用户信息
 * 
 * @author liajay
 * @since 2025-08-23
 */
public class UserContextUtil {
    
    private static final String USER_ID_HEADER = "X-User-Id";
    
    // 使用ThreadLocal存储用户ID
    private static final ThreadLocal<Long> USER_ID_CONTEXT = new ThreadLocal<>();
    
    /**
     * 设置当前线程的用户ID
     * 
     * @param userId 用户ID
     */
    public static void setCurrentUserId(Long userId) {
        USER_ID_CONTEXT.set(userId);
    }
    
    /**
     * 获取当前线程的用户ID
     * 
     * @return 用户ID，如果未设置则返回null
     */
    public static Long getCurrentUserId() {
        return USER_ID_CONTEXT.get();
    }
    
    /**
     * 获取当前线程的用户ID（字符串格式）
     * 
     * @return 用户ID字符串，如果未设置则返回null
     */
    public static String getCurrentUserIdAsString() {
        Long userId = getCurrentUserId();
        return userId != null ? userId.toString() : null;
    }
    
    /**
     * 检查当前线程是否设置了用户ID
     * 
     * @return 如果设置了用户ID则返回true，否则返回false
     */
    public static boolean hasCurrentUserId() {
        return getCurrentUserId() != null;
    }
    
    /**
     * 获取当前线程的用户ID，如果未设置则抛出异常
     * 
     * @return 用户ID
     * @throws IllegalStateException 如果未设置用户ID
     */
    public static Long requireCurrentUserId() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new IllegalStateException("User ID not found in current thread context");
        }
        return userId;
    }
    
    /**
     * 清除当前线程的用户ID
     */
    public static void clearCurrentUserId() {
        USER_ID_CONTEXT.remove();
    }
    
    /**
     * 获取用户ID请求头名称
     * 
     * @return 请求头名称
     */
    public static String getUserIdHeaderName() {
        return USER_ID_HEADER;
    }
}
