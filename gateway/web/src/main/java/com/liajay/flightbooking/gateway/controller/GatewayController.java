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
    

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @RequestMapping(value = "/api/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardUserRequest(HttpServletRequest request) throws IOException {
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
