package com.liajay.flightbooking.inventory.service;

import com.liajay.flightbooking.inventory.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FlightServiceTest {
    @Autowired
    private FlightService flightService;

    @Test
    void testFlightServiceNotNull() {
        assertThat(flightService).isNotNull();
    }
    // 可补充更多flight相关测试
}
