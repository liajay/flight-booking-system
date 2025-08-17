package com.liajay.flightbooking.util.config;

import com.liajay.flightbooking.util.UserIdGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UserIdGenerator配置类
 * 
 * @author liajay
 * @since 2025-08-16
 */
@Configuration
@ConfigurationProperties(prefix = "user.id.generator")
public class UserIdGeneratorConfig {

    /**
     * 工作节点ID，默认为1
     */
    private long workerId = 1L;

    /**
     * 数据中心ID，默认为1
     */
    private long datacenterId = 1L;

    /**
     * 创建UserIdGenerator Bean
     * 
     * @return UserIdGenerator实例
     */
    @Bean
    public UserIdGenerator userIdGenerator() {
        return new UserIdGenerator(workerId, datacenterId);
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
