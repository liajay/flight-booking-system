package com.liajay.flightbooking.inventory.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * 
 * @author liajay
 */
@Configuration
@MapperScan("com.liajay.flightbooking.inventory.dal.mapper")
public class MyBatisConfig {
    // 配置会通过application-mybatis.yml文件进行
    // 这里主要是启用MapperScan扫描Mapper接口
}
