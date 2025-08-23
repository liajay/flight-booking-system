package com.liajay.flightbooking.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class GatewayBootstrapApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GatewayBootstrapApplication.class, args);
    }
}
