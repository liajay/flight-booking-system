package com.liajay.flightbooking.inventory.dal.repository;

import com.liajay.flightbooking.inventory.InventoryServiceApplication;
import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.FlightStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 航班数据访问层测试类
 * 使用@DataJpaTest进行仓储层集成测试
 * 
 * @author liajay
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;


    @Test
    void testFindByFlightNumber() {
        // 测试根据航班号查询航班
        Optional<Flight> flightOptional = flightRepository.findByFlightNumber("CA1234");
        
        assertTrue(flightOptional.isPresent());
        Flight flight = flightOptional.get();
        assertEquals("CA1234", flight.getFlightNumber());
        assertEquals("中国国际航空", flight.getAirline());
        assertEquals("北京", flight.getDepartureCity());
        assertEquals("上海", flight.getArrivalCity());

        // 测试查询不存在的航班
        Optional<Flight> notFoundFlight = flightRepository.findByFlightNumber("NOTFOUND");
        assertFalse(notFoundFlight.isPresent());
    }

    @Test
    void testFindByDepartureCityAndArrivalCityAndStatus() {
        // 测试根据出发地、目的地和状态查询航班
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> beijingToShanghaiFlights = flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                "北京", "上海", FlightStatus.SCHEDULED, pageable);
        
        assertEquals(1, beijingToShanghaiFlights.getTotalElements());
        Flight flight = beijingToShanghaiFlights.getContent().get(0);
        assertEquals("CA1234", flight.getFlightNumber());

        // 测试查询取消的航班
        Page<Flight> cancelledFlights = flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                "北京", "上海", FlightStatus.CANCELLED, pageable);
        assertEquals(1, cancelledFlights.getTotalElements());
        assertEquals("XX9999", cancelledFlights.getContent().get(0).getFlightNumber());

        // 测试查询不存在的路线
        Page<Flight> notFoundFlights = flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                "北京", "广州", FlightStatus.SCHEDULED, pageable);
        assertEquals(0, notFoundFlights.getTotalElements());
    }

    @Test
    void testFindByDepartureCityAndStatus() {
        // 测试根据出发地和状态查询航班
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> beijingFlights = flightRepository.findByDepartureCityAndStatus(
                "北京", FlightStatus.SCHEDULED, pageable);
        
        assertEquals(1, beijingFlights.getTotalElements());
        assertEquals("CA1234", beijingFlights.getContent().get(0).getFlightNumber());

        Page<Flight> shanghaiFlights = flightRepository.findByDepartureCityAndStatus(
                "上海", FlightStatus.SCHEDULED, pageable);
        assertEquals(1, shanghaiFlights.getTotalElements());
        assertEquals("MU5678", shanghaiFlights.getContent().get(0).getFlightNumber());
    }

    @Test
    void testFindByArrivalCityAndStatus() {
        // 测试根据目的地和状态查询航班
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> toShanghaiFlights = flightRepository.findByArrivalCityAndStatus(
                "上海", FlightStatus.SCHEDULED, pageable);
        
        assertEquals(2, toShanghaiFlights.getTotalElements());
        assertTrue(toShanghaiFlights.getContent().stream()
                .allMatch(flight -> "上海".equals(flight.getArrivalCity())));

        Page<Flight> toBeijingFlights = flightRepository.findByArrivalCityAndStatus(
                "北京", FlightStatus.SCHEDULED, pageable);
        assertEquals(1, toBeijingFlights.getTotalElements());
        assertEquals("3U3456", toBeijingFlights.getContent().get(0).getFlightNumber());
    }

    @Test
    void testFindByDepartureTimeBetweenAndStatus() {
        // 测试根据日期范围查询航班
        LocalDateTime startTime = LocalDateTime.of(2025, 8, 21, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 8, 21, 23, 59);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Flight> todayFlights = flightRepository.findByDepartureTimeBetweenAndStatus(
                startTime, endTime, FlightStatus.SCHEDULED, pageable);
        
        assertEquals(3, todayFlights.getTotalElements());
        assertTrue(todayFlights.getContent().stream()
                .allMatch(flight -> flight.getDepartureTime().toLocalDate()
                        .equals(LocalDateTime.of(2025, 8, 21, 0, 0).toLocalDate())));

        // 测试查询第二天的航班
        LocalDateTime nextDayStart = LocalDateTime.of(2025, 8, 22, 0, 0);
        LocalDateTime nextDayEnd = LocalDateTime.of(2025, 8, 22, 23, 59);
        
        Page<Flight> nextDayFlights = flightRepository.findByDepartureTimeBetweenAndStatus(
                nextDayStart, nextDayEnd, FlightStatus.SCHEDULED, pageable);
        assertEquals(2, nextDayFlights.getTotalElements());
    }

    @Test
    void testFindFlights() {
        // 测试复合查询
        LocalDateTime startTime = LocalDateTime.of(2025, 8, 21, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 8, 21, 23, 59);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Flight> specificFlights = flightRepository.findFlights(
                "北京", "上海", startTime, endTime, FlightStatus.SCHEDULED, pageable);
        
        assertEquals(1, specificFlights.getTotalElements());
        Flight flight = specificFlights.getContent().get(0);
        assertEquals("CA1234", flight.getFlightNumber());
        assertEquals("北京", flight.getDepartureCity());
        assertEquals("上海", flight.getArrivalCity());

        // 测试查询不存在的复合条件
        Page<Flight> notFoundFlights = flightRepository.findFlights(
                "北京", "广州", startTime, endTime, FlightStatus.SCHEDULED, pageable);
        assertEquals(0, notFoundFlights.getTotalElements());
    }

    @Test
    void testFindByAirlineAndStatus() {
        // 测试根据航空公司查询航班
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> airChinaFlights = flightRepository.findByAirlineAndStatus(
                "中国国际航空", FlightStatus.SCHEDULED, pageable);
        
        assertEquals(1, airChinaFlights.getTotalElements());
        assertEquals("CA1234", airChinaFlights.getContent().get(0).getFlightNumber());

        Page<Flight> easternFlights = flightRepository.findByAirlineAndStatus(
                "中国东方航空", FlightStatus.SCHEDULED, pageable);
        assertEquals(1, easternFlights.getTotalElements());
        assertEquals("MU5678", easternFlights.getContent().get(0).getFlightNumber());

        // 测试查询不存在的航空公司
        Page<Flight> notFoundAirlineFlights = flightRepository.findByAirlineAndStatus(
                "不存在航空", FlightStatus.SCHEDULED, pageable);
        assertEquals(0, notFoundAirlineFlights.getTotalElements());
    }

    @Test
    void testFindByStatus() {
        // 测试根据状态查询所有航班
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> scheduledFlights = flightRepository.findByStatus(FlightStatus.SCHEDULED, pageable);
        
        assertEquals(5, scheduledFlights.getTotalElements());
        assertTrue(scheduledFlights.getContent().stream()
                .allMatch(flight -> flight.getStatus() == FlightStatus.SCHEDULED));

        Page<Flight> cancelledFlights = flightRepository.findByStatus(FlightStatus.CANCELLED, pageable);
        assertEquals(1, cancelledFlights.getTotalElements());
        assertEquals("XX9999", cancelledFlights.getContent().get(0).getFlightNumber());
    }

    @Test
    void testFlightCreationAndRetrieval() {
        // 测试航班创建和检索
        Flight newFlight = new Flight(
                "TEST123", "测试航空", "测试出发地", "测试目的地",
                LocalDateTime.of(2025, 8, 25, 12, 0),
                LocalDateTime.of(2025, 8, 25, 14, 0),
                new BigDecimal("999.99")
        );
        newFlight.setStatus(FlightStatus.SCHEDULED);

        Flight savedFlight = flightRepository.save(newFlight);
        assertNotNull(savedFlight.getId());
        assertEquals("TEST123", savedFlight.getFlightNumber());
        assertEquals("测试航空", savedFlight.getAirline());
        assertEquals(FlightStatus.SCHEDULED, savedFlight.getStatus());

        // 验证可以通过ID检索
        Optional<Flight> retrievedFlight = flightRepository.findById(savedFlight.getId());
        assertTrue(retrievedFlight.isPresent());
        assertEquals(savedFlight.getId(), retrievedFlight.get().getId());
    }

    @Test
    void testFlightUpdate() {
        // 测试航班更新
        Optional<Flight> flightOptional = flightRepository.findByFlightNumber("CA1234");
        assertTrue(flightOptional.isPresent());

        Flight flight = flightOptional.get();
        flight.setStatus(FlightStatus.DELAYED);
        flight.setBasePrice(new BigDecimal("850.00"));

        Flight updatedFlight = flightRepository.save(flight);
        assertEquals(FlightStatus.DELAYED, updatedFlight.getStatus());
        assertEquals(new BigDecimal("850.00"), updatedFlight.getBasePrice());

        // 验证更新后的查询结果
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> delayedFlights = flightRepository.findByStatus(FlightStatus.DELAYED, pageable);
        assertEquals(1, delayedFlights.getTotalElements());
    }

    @Test
    void testFlightDeletion() {
        // 测试航班删除
        Optional<Flight> flightToDelete = flightRepository.findByFlightNumber("CA1234");
        assertTrue(flightToDelete.isPresent());

        flightRepository.delete(flightToDelete.get());

        Optional<Flight> deletedFlight = flightRepository.findByFlightNumber("CA1234");
        assertFalse(deletedFlight.isPresent());

        // 验证总数减少
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> allScheduledFlights = flightRepository.findByStatus(FlightStatus.SCHEDULED, pageable);
        assertEquals(4, allScheduledFlights.getTotalElements()); // 原来5个，删除1个
    }

    @Test
    void testFlightEntityConstraints() {
        // 测试唯一约束：航班号不能重复
        Flight duplicateFlight = new Flight(
                "CA1234", "另一个航空", "测试出发地", "测试目的地",
                LocalDateTime.of(2025, 8, 25, 12, 0),
                LocalDateTime.of(2025, 8, 25, 14, 0),
                new BigDecimal("999.99")
        );

    }

    @Test
    void testPaginationWithLargeDataSet() {
        // 测试分页功能
        Pageable firstPage = PageRequest.of(0, 2);
        Page<Flight> page1 = flightRepository.findByStatus(FlightStatus.SCHEDULED, firstPage);
        
        assertEquals(2, page1.getNumberOfElements());
        assertEquals(5, page1.getTotalElements());
        assertEquals(3, page1.getTotalPages());
        assertTrue(page1.hasNext());

        // 测试第二页
        Pageable secondPage = PageRequest.of(1, 2);
        Page<Flight> page2 = flightRepository.findByStatus(FlightStatus.SCHEDULED, secondPage);
        assertEquals(2, page2.getNumberOfElements());
        assertTrue(page2.hasNext());

        // 测试最后一页
        Pageable lastPage = PageRequest.of(2, 2);
        Page<Flight> page3 = flightRepository.findByStatus(FlightStatus.SCHEDULED, lastPage);
        assertEquals(1, page3.getNumberOfElements());
        assertFalse(page3.hasNext());
    }

    @Test
    void testQueryWithEmptyResults() {
        // 测试查询不存在的数据
        Optional<Flight> nonExistentFlight = flightRepository.findByFlightNumber("NONEXISTENT");
        assertFalse(nonExistentFlight.isPresent());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> nonExistentRoute = flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                "不存在的城市", "另一个不存在的城市", FlightStatus.SCHEDULED, pageable);
        assertEquals(0, nonExistentRoute.getTotalElements());

        Page<Flight> nonExistentAirline = flightRepository.findByAirlineAndStatus(
                "不存在的航空公司", FlightStatus.SCHEDULED, pageable);
        assertEquals(0, nonExistentAirline.getTotalElements());
    }
}
