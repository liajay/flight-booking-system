package com.liajay.flightbooking.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 订单服务启动类
 * 
 * @author liajay
 */
@SpringBootApplication(scanBasePackages = "com.liajay.flightbooking.order")
@MapperScan("com.liajay.flightbooking.order.dal.mapper")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
