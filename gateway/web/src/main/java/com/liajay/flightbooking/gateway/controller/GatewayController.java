package com.liajay.flightbooking.gateway.controller;

import com.liajay.flightbooking.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
public class GatewayController {
    
    @Autowired
    private GatewayService gatewayService;
    
    // 处理所有用户服务相关请求
    @RequestMapping(value = "/api/users/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardUserRequest(HttpServletRequest request) throws IOException {
        String body = readRequestBody(request);
        return gatewayService.forwardRequest(request, body);
    }
    
    // 处理所有订单服务相关请求
    @RequestMapping(value = "/api/order/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardOrderRequest(HttpServletRequest request) throws IOException {
        String body = readRequestBody(request);
        return gatewayService.forwardRequest(request, body);
    }
    
    // 处理所有库存服务相关请求
    @RequestMapping(value = "/api/inventory/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardInventoryRequest(HttpServletRequest request) throws IOException {
        String body = readRequestBody(request);
        return gatewayService.forwardRequest(request, body);
    }
    
    // 健康检查接口
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"gateway\"}");
    }
    
    // 网关信息接口
    @GetMapping("/")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("{\"message\":\"Flight Booking Gateway\",\"version\":\"1.0\"}");
    }
    
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
