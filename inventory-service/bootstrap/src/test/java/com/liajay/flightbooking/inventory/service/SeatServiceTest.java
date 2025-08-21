package com.liajay.flightbooking.inventory.service;

import com.liajay.flightbooking.inventory.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SeatServiceTest {
    @Autowired
    private SeatService seatService;

    @Test
    void testSeatServiceNotNull() {
        assertThat(seatService).isNotNull();
    }
    // 可补充更多seat相关测试
}
