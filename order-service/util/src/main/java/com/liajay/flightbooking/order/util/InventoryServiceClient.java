package com.liajay.flightbooking.order.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 库存服务HTTP客户端
 * 
 * @author liajay
 */
//todo : 后续切换成rpc框架或者消息队列来实现
@Component
public class InventoryServiceClient {
    
    private final HttpClient httpClient;
    private final String inventoryServiceBaseUrl;
    
    public InventoryServiceClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        // TODO: 从配置文件读取
        this.inventoryServiceBaseUrl = "http://localhost:8082";
    }
    
    /**
     * 为指定航班分配座位
     * 
     * @param flightNumber 航班号
     * @return 分配的座位信息，如果失败则返回null
     */
    public SeatInfo allocateSeat(String flightNumber) {
        try {
            String url = inventoryServiceBaseUrl + "/api/seats/allocate/" + flightNumber;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // 简单解析JSON响应
                return parseJsonResponse(response.body());
            }
            
            return null;
        } catch (IOException | InterruptedException e) {
            System.err.println("调用库存服务分配座位失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 简单解析JSON响应
     */
    private SeatInfo parseJsonResponse(String jsonResponse) {
        try {
            // 检查是否成功
            if (!jsonResponse.contains("\"success\":true")) {
                return null;
            }
            
            // 提取座位信息
            SeatInfo seatInfo = new SeatInfo();
            
            // 提取ID
            Pattern idPattern = Pattern.compile("\"id\":(\\d+)");
            Matcher idMatcher = idPattern.matcher(jsonResponse);
            if (idMatcher.find()) {
                seatInfo.setId(Long.parseLong(idMatcher.group(1)));
            }
            
            // 提取航班号
            Pattern flightPattern = Pattern.compile("\"flightNumber\":\"([^\"]+)\"");
            Matcher flightMatcher = flightPattern.matcher(jsonResponse);
            if (flightMatcher.find()) {
                seatInfo.setFlightNumber(flightMatcher.group(1));
            }
            
            // 提取座位号
            Pattern seatPattern = Pattern.compile("\"seatNumber\":\"([^\"]+)\"");
            Matcher seatMatcher = seatPattern.matcher(jsonResponse);
            if (seatMatcher.find()) {
                seatInfo.setSeatNumber(seatMatcher.group(1));
            }
            
            // 提取舱位等级
            Pattern classPattern = Pattern.compile("\"seatClass\":\"([^\"]+)\"");
            Matcher classMatcher = classPattern.matcher(jsonResponse);
            if (classMatcher.find()) {
                seatInfo.setSeatClass(classMatcher.group(1));
            }
            
            // 提取价格
            Pattern pricePattern = Pattern.compile("\"price\":([\\d.]+)");
            Matcher priceMatcher = pricePattern.matcher(jsonResponse);
            if (priceMatcher.find()) {
                seatInfo.setPrice(new BigDecimal(priceMatcher.group(1)));
            }
            
            return seatInfo;
        } catch (Exception e) {
            System.err.println("解析座位信息失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 座位信息类
     */
    public static class SeatInfo {
        private Long id;
        private String flightNumber;
        private String seatNumber;
        private String seatClass;
        private Boolean isAvailable;
        private BigDecimal price;
        
        // Getter和Setter方法
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getFlightNumber() {
            return flightNumber;
        }
        
        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }
        
        public String getSeatNumber() {
            return seatNumber;
        }
        
        public void setSeatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
        }
        
        public String getSeatClass() {
            return seatClass;
        }
        
        public void setSeatClass(String seatClass) {
            this.seatClass = seatClass;
        }
        
        public Boolean getIsAvailable() {
            return isAvailable;
        }
        
        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }
        
        public BigDecimal getPrice() {
            return price;
        }
        
        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
