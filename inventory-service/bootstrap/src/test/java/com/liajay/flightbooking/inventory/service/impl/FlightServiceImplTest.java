package com.liajay.flightbooking.inventory.service.impl;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.service.FlightService;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * FlightServiceImpl测试类 - MyBatis版本
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FlightServiceImplTest {

    @Autowired
    private FlightService flightService;

    @Test
    void testFindByFlightNumber() {
        // 测试根据航班号查询航班
        Flight flight = flightService.findByFlightNumber("CA1234");
        
        assertNotNull(flight);
        assertEquals("CA1234", flight.getFlightNumber());
        assertEquals("中国国际航空", flight.getAirline());
        assertEquals("北京", flight.getDepartureCity());
        assertEquals("上海", flight.getArrivalCity());
    }

    @Test
    void testFindByFlightNumberNotFound() {
        // 测试查询不存在的航班号
        Flight flight = flightService.findByFlightNumber("NOTEXIST");
        assertNull(flight);
    }

    @Test
    void testQueryFlightsWithPagination() {
        // 测试分页查询航班
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setPage(0);
        queryDTO.setSize(5);
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).hasSizeLessThanOrEqualTo(5);
        assertThat(result.getTotalElements()).isGreaterThan(0);
        assertEquals(0, result.getCurrentPage());
    }

    @Test
    void testQueryFlightsWithoutPagination() {
        // 测试非分页查询航班
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setEnablePaging(false);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        assertEquals(0, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void testQueryFlightsByRoute() {
        // 测试根据路线查询航班（使用统一的queryFlights方法）
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setDepartureCity("北京");
        queryDTO.setArrivalCity("上海");
        queryDTO.setPage(0);
        queryDTO.setSize(10);
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        result.getFlightList().forEach(flight -> {
            assertEquals("北京", flight.getDepartureCity());
            assertEquals("上海", flight.getArrivalCity());
            assertEquals("SCHEDULED", flight.getStatus());
        });
    }

    @Test
    void testQueryFlightsByAirline() {
        // 测试根据航空公司查询航班（使用统一的queryFlights方法）
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setAirline("中国国际航空");
        queryDTO.setPage(0);
        queryDTO.setSize(10);
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        result.getFlightList().forEach(flight -> {
            assertEquals("中国国际航空", flight.getAirline());
            assertEquals("SCHEDULED", flight.getStatus());
        });
    }

    @Test
    void testQueryFlightsByDateRange() {
        // 测试根据日期范围查询航班（使用统一的queryFlights方法）
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setStartTime(LocalDateTime.of(2025, 8, 20, 0, 0));
        queryDTO.setEndTime(LocalDateTime.of(2025, 8, 25, 23, 59));
        queryDTO.setPage(0);
        queryDTO.setSize(10);
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        result.getFlightList().forEach(flight -> {
            assertTrue(flight.getDepartureTime().isAfter(queryDTO.getStartTime().minusSeconds(1)));
            assertTrue(flight.getDepartureTime().isBefore(queryDTO.getEndTime().plusSeconds(1)));
            assertEquals("SCHEDULED", flight.getStatus());
        });
    }

    @Test
    void testQueryActiveFlights() {
        // 测试查询活跃航班（使用统一的queryFlights方法）
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setPage(0);
        queryDTO.setSize(10);
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        result.getFlightList().forEach(flight -> assertEquals("SCHEDULED", flight.getStatus()));
    }

    @Test
    void testQueryFlightsWithComplexConditions() {
        // 测试复杂条件查询
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setAirline("中国国际航空");
        queryDTO.setDepartureCity("北京");
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setPage(0);
        queryDTO.setSize(10);
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        result.getFlightList().forEach(flight -> {
            assertEquals("中国国际航空", flight.getAirline());
            assertEquals("北京", flight.getDepartureCity());
            assertEquals("SCHEDULED", flight.getStatus());
        });
    }

    @Test
    void testQueryFlightsWithSorting() {
        // 测试排序查询
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setSortBy("basePrice");
        queryDTO.setSortDirection("DESC");
        queryDTO.setPage(0);
        queryDTO.setSize(5);
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        // 验证结果按价格降序排列
        for (int i = 1; i < result.getFlightList().size(); i++) {
            assertTrue(result.getFlightList().get(i-1).getBasePrice()
                .compareTo(result.getFlightList().get(i).getBasePrice()) >= 0);
        }
    }

    @Test
    void testQueryFlightsWithFlightStatistics() {
        // 测试航班统计信息
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setFlightNumber("CA1234");
        queryDTO.setEnablePaging(false);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).hasSize(1);
        
        var flight = result.getFlightList().get(0);
        assertEquals("CA1234", flight.getFlightNumber());
        
        // 验证座位统计信息被正确设置
        assertNotNull(flight.getTotalSeats());
        assertNotNull(flight.getAvailableSeats());
        assertTrue(flight.getTotalSeats() >= flight.getAvailableSeats());
    }

    @Test
    void testFlightResponseFormat() {
        // 测试航班响应格式是否符合前端要求
        FlightQueryDTO queryDTO = new FlightQueryDTO();
        queryDTO.setStatus("SCHEDULED");
        queryDTO.setPage(0);
        queryDTO.setSize(10);
        queryDTO.setEnablePaging(true);
        
        FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
        
        assertNotNull(result);
        assertNotNull(result.getFlightList());
        assertThat(result.getFlightList()).isNotEmpty();
        
        // 验证分页信息
        assertNotNull(result.getTotalElements());
        assertNotNull(result.getTotalPages());
        assertNotNull(result.getCurrentPage());
        assertNotNull(result.getPageSize());
        assertNotNull(result.getHasNext());
        assertNotNull(result.getHasPrevious());
        assertNotNull(result.getIsFirst());
        assertNotNull(result.getIsLast());
        
        // 验证航班信息格式
        var flight = result.getFlightList().get(0);
        assertNotNull(flight.getId());
        assertNotNull(flight.getFlightNumber());
        assertNotNull(flight.getAirline());
        assertNotNull(flight.getDepartureCity());
        assertNotNull(flight.getArrivalCity());
        assertNotNull(flight.getDepartureTime());
        assertNotNull(flight.getArrivalTime());
        assertNotNull(flight.getBasePrice());
        assertNotNull(flight.getStatus());
        assertNotNull(flight.getStatusDescription());
        
        // 验证座位统计信息
        assertNotNull(flight.getTotalSeats());
        assertNotNull(flight.getAvailableSeats());
        assertNotNull(flight.getEconomySeats());
        assertNotNull(flight.getBusinessSeats());
        assertNotNull(flight.getFirstSeats());
        
        // 验证座位数量逻辑正确性
        assertEquals(flight.getTotalSeats(), 
            flight.getEconomySeats() + flight.getBusinessSeats() + flight.getFirstSeats());
        assertTrue(flight.getAvailableSeats() <= flight.getTotalSeats());
    }

    @Test
    void testFlightServiceNotNull() {
        // 测试服务注入正常
        assertThat(flightService).isNotNull();
    }
}
