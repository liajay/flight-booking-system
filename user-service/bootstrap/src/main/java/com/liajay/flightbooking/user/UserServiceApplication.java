package com.liajay.flightbooking.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.liajay.flightbooking")
@EnableJpaRepositories(basePackages = "com.liajay.flightbooking.user.dal.repository")
@EntityScan(basePackages = "com.liajay.flightbooking.user.dal.dataobject")
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
