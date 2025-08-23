package com.liajay.flightbooking.gateway.web.filter;

import com.liajay.flightbooking.gateway.util.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class JwtAuthFilter implements Filter {
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_HEADER = "X-User-Id";
    
    private final JwtUtil jwtUtil;
    
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestPath = httpRequest.getRequestURI();
        
        // 对于某些公共接口，可以跳过JWT验证（比如登录接口）
        if (isPublicEndpoint(requestPath)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        
        // 验证JWT token并提取用户信息
        try {
            if (!isValidJwtToken(token)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"error\":\"Invalid JWT token\"}");
                return;
            }
            
            // 从JWT中提取userId
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            // 创建包装的请求，添加userId到请求头
            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                @Override
                public String getHeader(String name) {
                    if (USER_ID_HEADER.equals(name)) {
                        return userId.toString();
                    }
                    return super.getHeader(name);
                }
                
                @Override
                public Enumeration<String> getHeaderNames() {
                    List<String> names = Collections.list(super.getHeaderNames());
                    names.add(USER_ID_HEADER);
                    return Collections.enumeration(names);
                }
            };
            
            chain.doFilter(wrappedRequest, response);
            
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"JWT parsing failed: " + e.getMessage() + "\"}");
        }
    }
    
    private boolean isPublicEndpoint(String path) {
        // 定义不需要JWT验证的公共接口
        return path.startsWith("/api/users/login") ||
               path.startsWith("/api/users/register") ||
               path.equals("/health") ||
               path.equals("/");
    }
    
    private boolean isValidJwtToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            // 使用JwtUtil验证token的有效性
            return !jwtUtil.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
