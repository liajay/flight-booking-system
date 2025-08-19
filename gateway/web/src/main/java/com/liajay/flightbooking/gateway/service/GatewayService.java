package com.liajay.flightbooking.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;

@Service
public class GatewayService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    // 服务地址配置
    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private static final String ORDER_SERVICE_URL = "http://localhost:8082";
    private static final String INVENTORY_SERVICE_URL = "http://localhost:8083";
    
    public ResponseEntity<String> forwardRequest(HttpServletRequest request, String body) {
        try {
            // 根据请求路径确定目标服务
            String targetServiceUrl = getTargetServiceUrl(request.getRequestURI());
            if (targetServiceUrl == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 构建目标URL
            String targetPath = getTargetPath(request.getRequestURI());
            String targetUrl = targetServiceUrl + targetPath;
            
            // 如果有查询参数，添加到URL中
            if (request.getQueryString() != null) {
                targetUrl += "?" + request.getQueryString();
            }
            
            // 复制请求头
            HttpHeaders headers = copyHeaders(request);
            
            // 创建HTTP实体
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            
            // 确定HTTP方法
            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            
            // 转发请求
            ResponseEntity<String> response = restTemplate.exchange(
                URI.create(targetUrl),
                method,
                entity,
                String.class
            );
            
            return response;
            
        }catch (HttpClientErrorException e){
            return ResponseEntity
                    .status(e.getRawStatusCode())
                    .header("Content-Type", "application/json")
                    .header("X-Error-Source", "Gateway")
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Gateway forwarding failed: " + e.getMessage() + "\"}");
        }
    }
    
    private String getTargetServiceUrl(String requestUri) {
        if (requestUri.startsWith("/api/users/")) {
            return USER_SERVICE_URL;
        } else if (requestUri.startsWith("/api/order/")) {
            return ORDER_SERVICE_URL;
        } else if (requestUri.startsWith("/api/inventory/")) {
            return INVENTORY_SERVICE_URL;
        }
        return null;
    }
    
    private String getTargetPath(String requestUri) {
        // 移除网关前缀，保留原始路径结构
        if (requestUri.startsWith("/api/user/")) {
            return requestUri.substring("/api/user".length());
        } else if (requestUri.startsWith("/api/order/")) {
            return requestUri.substring("/api/order".length());
        } else if (requestUri.startsWith("/api/inventory/")) {
            return requestUri.substring("/api/inventory".length());
        }
        return requestUri;
    }
    
    private HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            
            // 跳过一些不应该转发的头
            if (shouldSkipHeader(headerName)) {
                continue;
            }
            
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                headers.add(headerName, headerValues.nextElement());
            }
        }
        
        return headers;
    }
    
    private boolean shouldSkipHeader(String headerName) {
        // 跳过一些由网关或容器管理的头
        String lowerCaseName = headerName.toLowerCase();
        return lowerCaseName.equals("host") ||
               lowerCaseName.equals("content-length") ||
               lowerCaseName.equals("connection") ||
               lowerCaseName.equals("upgrade") ||
               lowerCaseName.equals("proxy-connection");
    }
}
