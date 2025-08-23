package com.liajay.flightbooking.inventory.dal.mapper;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.FlightStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FlightMapperTest {

    @Autowired
    private FlightMapper flightMapper;

    @Test
    void testFindByFlightNumber() {
        // 测试根据航班号查询航班
        Flight flight = flightMapper.findByFlightNumber("CA1234");
        
        assertNotNull(flight);
        assertEquals("CA1234", flight.getFlightNumber());
        assertEquals("中国国际航空", flight.getAirline());
        assertEquals("北京", flight.getDepartureCity());
        assertEquals("上海", flight.getArrivalCity());
        assertNotNull(flight.getAirline());
        assertNotNull(flight.getDepartureCity());
        assertNotNull(flight.getArrivalCity());
    }

    @Test
    void testFindByFlightNumberNotFound() {
        // 测试查询不存在的航班号
        Flight flight = flightMapper.findByFlightNumber("NOTEXIST");
        assertNull(flight);
    }

}
