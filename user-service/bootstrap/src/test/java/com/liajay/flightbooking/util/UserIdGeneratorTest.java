package com.liajay.flightbooking.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserIdGenerator单元测试
 * 
 * @author liajay
 * @since 2025-08-16
 */
class UserIdGeneratorTest {

    private UserIdGenerator userIdGenerator;

    @BeforeEach
    void setUp() {
        userIdGenerator = new UserIdGenerator(1L, 1L);
    }

    @Test
    void testGenerateUuidUserId() {
        String userId = userIdGenerator.generateUuidUserId();
        
        assertNotNull(userId);
        assertEquals(32, userId.length());
        assertFalse(userId.contains("-"));
        assertTrue(userId.matches("^[a-fA-F0-9]{32}$"));
    }

    @Test
    void testGenerateUuidUserIdWithHyphens() {
        String userId = userIdGenerator.generateUuidUserIdWithHyphens();
        
        assertNotNull(userId);
        assertEquals(36, userId.length());
        assertTrue(userId.contains("-"));
        assertTrue(userId.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$"));
    }

    @Test
    void testGenerateSnowflakeUserId() {
        String userId = userIdGenerator.generateSnowflakeUserId();
        
        assertNotNull(userId);
        assertEquals(19, userId.length());
        assertTrue(userId.matches("^\\d{19}$"));
        
        // 测试生成的ID是递增的
        String userId2 = userIdGenerator.generateSnowflakeUserId();
        assertTrue(Long.parseLong(userId2) > Long.parseLong(userId));
    }

    @Test
    void testGenerateTimestampBasedUserId() {
        String userId = userIdGenerator.generateTimestampBasedUserId();
        
        assertNotNull(userId);
        assertEquals(20, userId.length());
        assertTrue(userId.matches("^\\d{20}$"));
        
        // 验证时间戳部分格式（前14位）
        String timestampPart = userId.substring(0, 14);
        assertTrue(timestampPart.matches("^\\d{14}$"));
    }

    @Test
    void testGenerateShortUserId() {
        String shortId = userIdGenerator.generateShortUserId();
        
        assertNotNull(shortId);
        assertEquals(8, shortId.length());
        assertTrue(shortId.matches("^[0-9A-Za-z]{8}$"));
    }

    @Test
    void testGenerateShortUserIdWithCustomLength() {
        int customLength = 12;
        String shortId = userIdGenerator.generateShortUserId(customLength);
        
        assertNotNull(shortId);
        assertEquals(customLength, shortId.length());
        assertTrue(shortId.matches("^[0-9A-Za-z]{" + customLength + "}$"));
    }

    @Test
    void testGenerateShortUserIdWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            userIdGenerator.generateShortUserId(0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            userIdGenerator.generateShortUserId(-1);
        });
    }

    @Test
    void testGenerateUserIdWithPrefix() {
        String prefix = "USER";
        String userId = userIdGenerator.generateUserIdWithPrefix(prefix, "uuid");
        
        assertNotNull(userId);
        assertTrue(userId.startsWith(prefix + "_"));
        assertEquals(37, userId.length()); // prefix(4) + "_"(1) + uuid(32)
    }

    @Test
    void testGenerateUserIdWithPrefixInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            userIdGenerator.generateUserIdWithPrefix("USER", "invalid");
        });
    }

    @Test
    void testGenerateUserIdWithPrefixNullPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            userIdGenerator.generateUserIdWithPrefix(null, "uuid");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            userIdGenerator.generateUserIdWithPrefix("", "uuid");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            userIdGenerator.generateUserIdWithPrefix("   ", "uuid");
        });
    }

    @Test
    void testGenerateNumericUserId() {
        long numericId = userIdGenerator.generateNumericUserId();
        
        assertTrue(numericId > 0);
        
        // 测试生成的ID是递增的
        long numericId2 = userIdGenerator.generateNumericUserId();
        assertTrue(numericId2 > numericId);
    }

    @Test
    void testIsValidUserId() {
        // 测试UUID验证
        assertTrue(userIdGenerator.isValidUserId("a1b2c3d4e5f67890123456789012abcd", "uuid"));
        assertTrue(userIdGenerator.isValidUserId("a1b2c3d4-e5f6-7890-1234-56789012abcd", "uuid"));
        assertFalse(userIdGenerator.isValidUserId("invalid-uuid", "uuid"));
        
        // 测试雪花算法验证
        assertTrue(userIdGenerator.isValidUserId("1234567890123456789", "snowflake"));
        assertFalse(userIdGenerator.isValidUserId("123456789012345678", "snowflake")); // 18位
        assertFalse(userIdGenerator.isValidUserId("12345678901234567890", "snowflake")); // 20位
        assertFalse(userIdGenerator.isValidUserId("abcd567890123456789", "snowflake")); // 包含字母
        
        // 测试时间戳验证
        assertTrue(userIdGenerator.isValidUserId("20250816143025123456", "timestamp"));
        assertFalse(userIdGenerator.isValidUserId("2025081614302512345", "timestamp")); // 19位
        assertFalse(userIdGenerator.isValidUserId("202508161430251234567", "timestamp")); // 21位
        
        // 测试短ID验证
        assertTrue(userIdGenerator.isValidUserId("A1b2C3d4", "short"));
        assertTrue(userIdGenerator.isValidUserId("12345678", "short"));
        assertFalse(userIdGenerator.isValidUserId("A1b2C3d4!", "short")); // 包含特殊字符
        
        // 测试null和空字符串
        assertFalse(userIdGenerator.isValidUserId(null, "uuid"));
        assertFalse(userIdGenerator.isValidUserId("", "uuid"));
        assertFalse(userIdGenerator.isValidUserId("   ", "uuid"));
    }

    @RepeatedTest(10)
    void testUuidUniqueness() {
        Set<String> uuids = new HashSet<>();
        int count = 1000;
        
        for (int i = 0; i < count; i++) {
            String uuid = userIdGenerator.generateUuidUserId();
            uuids.add(uuid);
        }
        
        assertEquals(count, uuids.size(), "UUID should be unique");
    }

    @RepeatedTest(10)
    void testSnowflakeUniqueness() {
        Set<String> snowflakeIds = new HashSet<>();
        int count = 1000;
        
        for (int i = 0; i < count; i++) {
            String id = userIdGenerator.generateSnowflakeUserId();
            snowflakeIds.add(id);
        }
        
        assertEquals(count, snowflakeIds.size(), "Snowflake IDs should be unique");
    }

    @Test
    void testConcurrentSnowflakeGeneration() throws InterruptedException {
        int threadCount = 10;
        int idsPerThread = 100;
        Set<String> allIds = new HashSet<>();
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    Set<String> threadIds = new HashSet<>();
                    for (int j = 0; j < idsPerThread; j++) {
                        String id = userIdGenerator.generateSnowflakeUserId();
                        threadIds.add(id);
                    }
                    
                    synchronized (allIds) {
                        allIds.addAll(threadIds);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(threadCount * idsPerThread, allIds.size(), 
                    "All generated IDs should be unique in concurrent environment");
    }

    @Test
    void testInvalidWorkerIdAndDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new UserIdGenerator(-1L, 1L);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new UserIdGenerator(32L, 1L);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new UserIdGenerator(1L, -1L);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new UserIdGenerator(1L, 32L);
        });
    }

    @Test
    void testSnowflakeIdOrdering() {
        String id1 = userIdGenerator.generateSnowflakeUserId();
        
        // 等待一小段时间确保时间戳不同
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String id2 = userIdGenerator.generateSnowflakeUserId();
        
        assertTrue(Long.parseLong(id2) > Long.parseLong(id1), 
                  "Later generated snowflake ID should be larger");
    }

    @Test
    void testGenerateUserIdWithPrefixAllTypes() {
        String prefix = "TEST";
        
        String uuidId = userIdGenerator.generateUserIdWithPrefix(prefix, "uuid");
        assertTrue(uuidId.startsWith(prefix + "_"));
        assertTrue(userIdGenerator.isValidUserId(uuidId.substring(5), "uuid"));
        
        String snowflakeId = userIdGenerator.generateUserIdWithPrefix(prefix, "snowflake");
        assertTrue(snowflakeId.startsWith(prefix + "_"));
        assertTrue(userIdGenerator.isValidUserId(snowflakeId.substring(5), "snowflake"));
        
        String timestampId = userIdGenerator.generateUserIdWithPrefix(prefix, "timestamp");
        assertTrue(timestampId.startsWith(prefix + "_"));
        assertTrue(userIdGenerator.isValidUserId(timestampId.substring(5), "timestamp"));
    }
}
