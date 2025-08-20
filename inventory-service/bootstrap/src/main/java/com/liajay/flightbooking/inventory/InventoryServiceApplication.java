package com.liajay.flightbooking.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 库存服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.liajay.flightbooking.inventory")
@EntityScan(basePackages = "com.liajay.flightbooking.inventory.dal.dataobject")
@EnableJpaRepositories(basePackages = "com.liajay.flightbooking.inventory.dal.repository")
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
