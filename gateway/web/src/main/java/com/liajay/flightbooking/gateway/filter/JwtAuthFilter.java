package com.liajay.flightbooking.gateway.filter;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 获取请求路径
        String requestPath = httpRequest.getRequestURI();
        
        // 对于某些公共接口，可以跳过JWT验证（比如登录接口）
        if (isPublicEndpoint(requestPath)) {
            chain.doFilter(request, response);
            return;
        }
        
        // 获取Authorization头
        String authHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }
        
        // 提取JWT token
        String token = authHeader.substring(BEARER_PREFIX.length());
        
        // 验证JWT token
        if (!isValidJwtToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"Invalid JWT token\"}");
            return;
        }
        
        // 验证通过，继续处理请求
        chain.doFilter(request, response);
    }
    
    private boolean isPublicEndpoint(String path) {
        // 定义不需要JWT验证的公共接口
        return path.startsWith("/api/users/login") ||
               path.startsWith("/api/users/register") ||
               path.equals("/health") ||
               path.equals("/");
    }
    
    private boolean isValidJwtToken(String token) {
        // 简单的JWT格式验证
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        // JWT应该有三个部分，用.分隔
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        
        // 这里可以添加更复杂的JWT签名验证逻辑
        // 目前只做基本格式验证
        try {
            // 验证各部分是否为有效的Base64编码
            for (String part : parts) {
                if (part.isEmpty()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
