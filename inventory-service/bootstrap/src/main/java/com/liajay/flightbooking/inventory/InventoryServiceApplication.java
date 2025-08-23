package com.liajay.flightbooking.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 库存服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.liajay.flightbooking.inventory")
@MapperScan("com.liajay.flightbooking.inventory.dal.mapper")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
