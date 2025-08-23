package com.liajay.flightbooking.inventory.dal.mapper;


import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SeatMapperTest {

    @Autowired
    private SeatMapper seatMapper;

    @Test
    void testFindByFlightNumberAndSeatNumber() {
        // 测试根据航班号和座位号查询座位 - 存在的座位
        Seat seat = seatMapper.findByFlightNumberAndSeatNumber("CA1234", "1A");
        
        assertNotNull(seat);
        assertEquals("CA1234", seat.getFlightNumber());
        assertEquals("1A", seat.getSeatNumber());
        assertEquals(SeatClass.BUSINESS, seat.getSeatClass());
        assertEquals(new BigDecimal("1200.00"), seat.getPrice());
        assertFalse(seat.getIsAvailable()); // 根据测试数据，1A已被占用
    }

    @Test
    void testFindByFlightNumberAndSeatNumberAvailable() {
        // 测试根据航班号和座位号查询座位 - 可用座位
        Seat seat = seatMapper.findByFlightNumberAndSeatNumber("CA1234", "1D");
        
        assertNotNull(seat);
        assertEquals("CA1234", seat.getFlightNumber());
        assertEquals("1D", seat.getSeatNumber());
        assertEquals(SeatClass.BUSINESS, seat.getSeatClass());
        assertEquals(new BigDecimal("1200.00"), seat.getPrice());
        assertTrue(seat.getIsAvailable()); // 1D应该是可用的
    }

    @Test
    void testFindByFlightNumberAndSeatNumberNotFound() {
        // 测试查询不存在的座位
        Seat seat = seatMapper.findByFlightNumberAndSeatNumber("CA1234", "99Z");
        assertNull(seat);
    }

    @Test
    void testFindByFlightNumberAndSeatNumberInvalidFlight() {
        // 测试查询不存在的航班
        Seat seat = seatMapper.findByFlightNumberAndSeatNumber("INVALID", "1A");
        assertNull(seat);
    }

    @Test
    void testFindByConditionsAllParameters() {
        // 测试使用所有参数的条件查询
        List<Seat> seats = seatMapper.findByConditions(
            "CA1234",                           // flightNumber
            SeatClass.BUSINESS,                 // seatClass
            true,                               // isAvailable
            new BigDecimal("1000.00"),          // minPrice
            new BigDecimal("1500.00"),          // maxPrice
            "1A",                               // seatNumberStart
            "4Z",                               // seatNumberEnd
            0,                                  // offset
            10                                  // pageSize
        );
        
        assertNotNull(seats);
        assertFalse(seats.isEmpty());
        
        // 验证查询结果符合条件
        for (Seat seat : seats) {
            assertEquals("CA1234", seat.getFlightNumber());
            assertEquals(SeatClass.BUSINESS, seat.getSeatClass());
            assertTrue(seat.getIsAvailable());
            assertTrue(seat.getPrice().compareTo(new BigDecimal("1000.00")) >= 0);
            assertTrue(seat.getPrice().compareTo(new BigDecimal("1500.00")) <= 0);
        }
    }

    @Test
    void testFindByConditionsFlightNumberOnly() {
        // 测试只使用航班号的条件查询
        List<Seat> seats = seatMapper.findByConditions(
            "CZ9012",                           // flightNumber
            null,                               // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null,                               // seatNumberEnd
            0,                                  // offset
            20                                  // pageSize
        );
        
        assertNotNull(seats);
        assertFalse(seats.isEmpty());
        
        // 验证所有座位都属于CZ9012航班
        for (Seat seat : seats) {
            assertEquals("CZ9012", seat.getFlightNumber());
            assertEquals(SeatClass.ECONOMY, seat.getSeatClass()); // CZ9012只有经济舱
        }
    }

    @Test
    void testFindByConditionsSeatClassFilter() {
        // 测试按座位等级过滤
        List<Seat> businessSeats = seatMapper.findByConditions(
            "MU5678",                           // flightNumber
            SeatClass.BUSINESS,                 // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null,                               // seatNumberEnd
            0,                                  // offset
            50                                  // pageSize
        );
        
        assertNotNull(businessSeats);
        assertFalse(businessSeats.isEmpty());
        
        // 验证所有座位都是商务舱
        for (Seat seat : businessSeats) {
            assertEquals("MU5678", seat.getFlightNumber());
            assertEquals(SeatClass.BUSINESS, seat.getSeatClass());
        }
    }

    @Test
    void testFindByConditionsAvailabilityFilter() {
        // 测试按可用性过滤
        List<Seat> unavailableSeats = seatMapper.findByConditions(
            "CA1234",                           // flightNumber
            null,                               // seatClass
            false,                              // isAvailable (查询不可用座位)
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null,                               // seatNumberEnd
            0,                                  // offset
            10                                  // pageSize
        );
        
        assertNotNull(unavailableSeats);
        assertFalse(unavailableSeats.isEmpty());
        
        // 验证所有座位都不可用
        for (Seat seat : unavailableSeats) {
            assertEquals("CA1234", seat.getFlightNumber());
            assertFalse(seat.getIsAvailable());
        }
    }

    @Test
    void testFindByConditionsPriceRange() {
        // 测试价格范围过滤
        List<Seat> seats = seatMapper.findByConditions(
            null,                               // flightNumber
            null,                               // seatClass
            null,                               // isAvailable
            new BigDecimal("800.00"),           // minPrice
            new BigDecimal("900.00"),           // maxPrice
            null,                               // seatNumberStart
            null,                               // seatNumberEnd
            0,                                  // offset
            50                                  // pageSize
        );
        
        assertNotNull(seats);
        assertFalse(seats.isEmpty());
        
        // 验证价格在范围内
        for (Seat seat : seats) {
            assertTrue(seat.getPrice().compareTo(new BigDecimal("800.00")) >= 0);
            assertTrue(seat.getPrice().compareTo(new BigDecimal("900.00")) <= 0);
        }
    }

    @Test
    void testFindByConditionsPagination() {
        // 测试分页功能
        List<Seat> firstPage = seatMapper.findByConditions(
            "CA1234",                           // flightNumber
            null,                               // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null,                               // seatNumberEnd
            0,                                  // offset
            5                                   // pageSize
        );
        
        List<Seat> secondPage = seatMapper.findByConditions(
            "CA1234",                           // flightNumber
            null,                               // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null,                               // seatNumberEnd
            5,                                  // offset
            5                                   // pageSize
        );
        
        assertNotNull(firstPage);
        assertNotNull(secondPage);
        assertEquals(5, firstPage.size());
        assertEquals(5, secondPage.size());
        
        // 验证两页数据不重复
        for (Seat seat1 : firstPage) {
            for (Seat seat2 : secondPage) {
                assertNotEquals(seat1.getId(), seat2.getId());
            }
        }
    }

    @Test
    void testCountByConditionsAllParameters() {
        // 测试使用所有参数的统计查询
        long count = seatMapper.countByConditions(
            "CA1234",                           // flightNumber
            SeatClass.BUSINESS,                 // seatClass
            true,                               // isAvailable
            new BigDecimal("1000.00"),          // minPrice
            new BigDecimal("1500.00"),          // maxPrice
            "1A",                               // seatNumberStart
            "4Z"                                // seatNumberEnd
        );
        
        assertTrue(count > 0);
    }

    @Test
    void testCountByConditionsFlightNumberOnly() {
        // 测试只使用航班号的统计查询
        long count = seatMapper.countByConditions(
            "CZ9012",                           // flightNumber
            null,                               // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null                                // seatNumberEnd
        );
        
        assertEquals(60, count); // CZ9012有60个座位
    }

    @Test
    void testCountByConditionsSeatClassFilter() {
        // 测试按座位等级统计
        long businessCount = seatMapper.countByConditions(
            "CA1234",                           // flightNumber
            SeatClass.BUSINESS,                 // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null                                // seatNumberEnd
        );
        
        long economyCount = seatMapper.countByConditions(
            "CA1234",                           // flightNumber
            SeatClass.ECONOMY,                  // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null                                // seatNumberEnd
        );
        
        assertEquals(16, businessCount);    // CA1234有16个商务舱座位 (4排 * 4座)
        assertEquals(120, economyCount);    // CA1234有120个经济舱座位 (20排 * 6座)
    }

    @Test
    void testCountByConditionsNoResults() {
        // 测试查询无结果的统计
        long count = seatMapper.countByConditions(
            "INVALID_FLIGHT",                   // flightNumber
            null,                               // seatClass
            null,                               // isAvailable
            null,                               // minPrice
            null,                               // maxPrice
            null,                               // seatNumberStart
            null                                // seatNumberEnd
        );
        
        assertEquals(0, count);
    }


}
