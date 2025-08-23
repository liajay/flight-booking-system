package com.liajay.flightbooking.dal.repository;

import com.liajay.flightbooking.user.UserServiceApplication;
import com.liajay.flightbooking.user.dal.dataobject.User;
import com.liajay.flightbooking.user.dal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户数据访问层测试类
 * 使用专用的测试数据库进行测试
 * 
 * @author liajay
 */
@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("user1").orElse(null);
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    void existsByUsername() {
        assertTrue(userRepository.existsByUsername("user1"));
        assertFalse(userRepository.existsByUsername("user4"));
    }
}