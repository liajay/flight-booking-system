package com.liajay.flightbooking.gateway.web.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Enumeration;

@Service
public class GatewayService {

    private final WebClient webClient;
    
    // 服务地址配置
    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private static final String INVENTORY_SERVICE_URL = "http://localhost:8082";
    private static final String ORDER_SERVICE_URL = "http://localhost:8083";

    public GatewayService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(1024 * 1024);
                    // 设置默认字符编码为UTF-8
                    configurer.defaultCodecs().enableLoggingRequestDetails(true);
                })
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8")
                .build();
    }

    
    public ResponseEntity<String> forwardRequest(HttpServletRequest request, String body) {
        try {
            String targetServiceUrl = getTargetServiceUrl(request.getRequestURI());
            if (targetServiceUrl == null) {
                return ResponseEntity.notFound().build();
            }

//            String targetPath = getTargetPath(request.getRequestURI());
            String targetPath = request.getRequestURI();
            String targetUrl = targetServiceUrl + targetPath;

            if (request.getQueryString() != null) {
                targetUrl += "?" + request.getQueryString();
            }

            ResponseEntity<String> ret = forwardWithWebClient(targetUrl, request, body);
            return ret;
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Gateway forwarding failed: " + e.getMessage() + "\"}");
        }
    }
    
    private ResponseEntity<String> forwardWithWebClient(String targetUrl, HttpServletRequest request, String body) {
        try {
            WebClient.RequestBodyUriSpec requestUriSpec = webClient
                    .method(HttpMethod.valueOf(request.getMethod()));

            WebClient.RequestBodySpec requestSpec = requestUriSpec.uri(targetUrl);

            // 确保设置正确的字符编码
            requestSpec.acceptCharset(java.nio.charset.StandardCharsets.UTF_8);
            
            copyHeadersToWebClient(request, requestSpec);

            Mono<ResponseEntity<String>> responseMono;
            if (body != null && !body.isEmpty()) {
                // 确保请求体使用UTF-8编码
                responseMono = requestSpec
                        .contentType(new MediaType("application", "json", java.nio.charset.StandardCharsets.UTF_8))
                        .bodyValue(body)
                        .retrieve()
                        .toEntity(String.class);
            } else {
                responseMono = requestSpec
                        .retrieve()
                        .toEntity(String.class);
            }

            return responseMono
                    .timeout(Duration.ofSeconds(30))
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        // 确保错误响应也使用正确的字符编码
                        String responseBody = ex.getResponseBodyAsString(java.nio.charset.StandardCharsets.UTF_8);
                        return Mono.just(ResponseEntity
                                .status(ex.getStatusCode())
                                .headers(httpHeaders -> {
                                    ex.getHeaders().forEach((key, values) -> {
                                        // 确保Content-Type包含charset信息
                                        if ("content-type".equalsIgnoreCase(key)) {
                                            for (String value : values) {
                                                if (value.contains("application/json") && !value.contains("charset")) {
                                                    value = value + "; charset=UTF-8";
                                                }
                                                httpHeaders.add(key, value);
                                            }
                                        } else {
                                            httpHeaders.put(key, values);
                                        }
                                    });
                                })
                                .body(responseBody));
                    })
                    .onErrorResume(Exception.class, ex -> {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(new MediaType("application", "json", java.nio.charset.StandardCharsets.UTF_8))
                                .body("{\"error\":\"Request failed: " + ex.getMessage() + "\"}"));
                    })
                    .block();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(new MediaType("application", "json", java.nio.charset.StandardCharsets.UTF_8))
                    .body("{\"error\":\"WebClient error: " + e.getMessage() + "\"}");
        }
    }
    
    private void copyHeadersToWebClient(HttpServletRequest request, WebClient.RequestBodySpec requestSpec) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            
            // 跳过一些不应该转发的头
            if (shouldSkipHeader(headerName)) {
                continue;
            }
            
            String headerValue = request.getHeader(headerName);
            if (headerValue != null) {
                // 特殊处理Content-Type头，确保包含UTF-8字符集
                if ("content-type".equalsIgnoreCase(headerName)) {
                    if (headerValue.contains("application/json") && !headerValue.contains("charset")) {
                        headerValue = headerValue + "; charset=UTF-8";
                    }
                }
                requestSpec.header(headerName, headerValue);
            }
        }
        
        // 如果没有Content-Type头且有请求体，添加默认的Content-Type
        if (request.getHeader("Content-Type") == null && 
            ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod()))) {
            requestSpec.contentType(new MediaType("application", "json", java.nio.charset.StandardCharsets.UTF_8));
        }
    }
    
    private String getTargetServiceUrl(String requestUri) {
        if (requestUri.startsWith("/api/users/")) {
            return USER_SERVICE_URL;
        } else if (requestUri.startsWith("/api/orders/")) {
            return ORDER_SERVICE_URL;
        } else if (requestUri.startsWith("/api/flights")) {
            return INVENTORY_SERVICE_URL;
        } else if (requestUri.startsWith("/api/seats")) {
            return INVENTORY_SERVICE_URL;
        }
        return null;
    }
    
    private String getTargetPath(String requestUri) {
        // 移除网关前缀，保留原始路径结构
        if (requestUri.startsWith("/api/users/")) {
            return requestUri.substring("/api/users".length());
        } else if (requestUri.startsWith("/api/orders/")) {
            return requestUri.substring("/api/orders".length());
        } else if (requestUri.startsWith("/api/inventory/")) {
            return requestUri.substring("/api/inventory".length());
        }
        return requestUri;
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
