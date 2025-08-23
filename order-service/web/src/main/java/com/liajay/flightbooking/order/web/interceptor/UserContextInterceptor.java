package com.liajay.flightbooking.order.web.interceptor;

import com.liajay.flightbooking.order.util.UserContextUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户上下文拦截器
 * 从请求头中提取用户ID并设置到当前线程上下文中
 * 
 * @author liajay
 * @since 2025-08-23
 */
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userIdStr = request.getHeader(UserContextUtil.getUserIdHeaderName());
        
        if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            try {
                Long userId = Long.valueOf(userIdStr);
                UserContextUtil.setCurrentUserId(userId);
            } catch (NumberFormatException e) {
                // 用户ID格式不正确，记录日志但不阻止请求
                System.err.println("Invalid user ID format: " + userIdStr);
            }
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理ThreadLocal，防止内存泄漏
        UserContextUtil.clearCurrentUserId();
    }
}
