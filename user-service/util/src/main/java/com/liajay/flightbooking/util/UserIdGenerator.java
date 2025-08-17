package com.liajay.flightbooking.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * UserId生成工具类
 * 提供多种ID生成策略，包括UUID、雪花算法、时间戳+随机数等
 * 
 * @author liajay
 * @since 2025-08-16
 */
public class UserIdGenerator {

    private static final Logger logger = LoggerFactory.getLogger(UserIdGenerator.class);

    // 雪花算法相关常量
    private static final long EPOCH = 1640995200000L; // 2022-01-01 00:00:00 UTC
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    // 实例变量
    private final long workerId;
    private final long datacenterId;
    private final AtomicLong sequence = new AtomicLong(0L);
    private volatile long lastTimestamp = -1L;
    private final SecureRandom secureRandom;

    /**
     * 默认构造函数，使用默认的工作节点ID和数据中心ID
     */
    public UserIdGenerator() {
        this(1L, 1L);
    }

    /**
     * 构造函数
     * 
     * @param workerId 工作节点ID (0-31)
     * @param datacenterId 数据中心ID (0-31)
     */
    public UserIdGenerator(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(
                String.format("Worker ID must be between 0 and %d", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(
                String.format("Datacenter ID must be between 0 and %d", MAX_DATACENTER_ID));
        }
        
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.secureRandom = new SecureRandom();
        
        logger.info("UserIdGenerator initialized with workerId: {}, datacenterId: {}", workerId, datacenterId);
    }

    /**
     * 生成基于UUID的用户ID（去除连字符）
     * 
     * @return 32位字符串用户ID
     */
    public String generateUuidUserId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成基于UUID的用户ID（保留连字符）
     * 
     * @return 36位字符串用户ID
     */
    public String generateUuidUserIdWithHyphens() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成基于雪花算法的用户ID
     * 
     * @return 19位数字字符串用户ID
     */
    public synchronized String generateSnowflakeUserId() {
        long currentTimestamp = getCurrentTimestamp();
        
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(
                String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                    lastTimestamp - currentTimestamp));
        }
        
        if (currentTimestamp == lastTimestamp) {
            long currentSequence = sequence.incrementAndGet() & SEQUENCE_MASK;
            if (currentSequence == 0) {
                currentTimestamp = tilNextMillis(lastTimestamp);
            }
            sequence.set(currentSequence);
        } else {
            sequence.set(0L);
        }
        
        lastTimestamp = currentTimestamp;
        
        long id = ((currentTimestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence.get();
        
        return String.valueOf(id);
    }

    /**
     * 生成基于时间戳和随机数的用户ID
     * 格式：yyyyMMddHHmmss + 6位随机数
     * 
     * @return 20位字符串用户ID
     */
    public String generateTimestampBasedUserId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = secureRandom.nextInt(1000000); // 0-999999
        return timestamp + String.format("%06d", random);
    }

    /**
     * 生成带前缀的用户ID
     * 
     * @param prefix 前缀
     * @param type ID生成类型：uuid, snowflake, timestamp
     * @return 带前缀的用户ID
     */
    public String generateUserIdWithPrefix(String prefix, String type) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        
        String id;
        switch (type.toLowerCase()) {
            case "uuid":
                id = generateUuidUserId();
                break;
            case "snowflake":
                id = generateSnowflakeUserId();
                break;
            case "timestamp":
                id = generateTimestampBasedUserId();
                break;
            default:
                throw new IllegalArgumentException("Unsupported ID type: " + type);
        }
        
        return prefix + "_" + id;
    }

    /**
     * 生成数字用户ID（基于雪花算法）
     * 
     * @return long类型用户ID
     */
    public long generateNumericUserId() {
        return Long.parseLong(generateSnowflakeUserId());
    }

    /**
     * 生成短用户ID（8位随机字符串）
     * 使用数字和字母组合
     * 
     * @return 8位字符串用户ID
     */
    public String generateShortUserId() {
        return generateShortUserId(8);
    }

    /**
     * 生成指定长度的短用户ID
     * 
     * @param length ID长度
     * @return 指定长度的字符串用户ID
     */
    public String generateShortUserId(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        
        return sb.toString();
    }

    /**
     * 验证用户ID格式是否有效
     * 
     * @param userId 用户ID
     * @param type ID类型：uuid, snowflake, timestamp, short
     * @return 是否有效
     */
    public boolean isValidUserId(String userId, String type) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        
        switch (type.toLowerCase()) {
            case "uuid":
                return userId.matches("^[a-fA-F0-9]{32}$") || 
                       userId.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$");
            case "snowflake":
                return userId.matches("^\\d{19}$");
            case "timestamp":
                return userId.matches("^\\d{20}$");
            case "short":
                return userId.matches("^[0-9A-Za-z]+$");
            default:
                return false;
        }
    }

    /**
     * 获取当前时间戳
     */
    private long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 等待到下一毫秒
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }
}
