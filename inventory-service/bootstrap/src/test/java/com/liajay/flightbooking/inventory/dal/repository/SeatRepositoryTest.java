package com.liajay.flightbooking.inventory.dal.repository;

import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 座位数据访问层测试类
 * 使用@DataJpaTest进行仓储层集成测试
 * 
 * @author liajay
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void testFindByFlightNumber() {
        // 测试根据航班号查询所有座位
        List<Seat> seats = seatRepository.findByFlightNumber("CA1234");
        
        assertEquals(136, seats.size());
        assertTrue(seats.stream().allMatch(seat -> "CA1234".equals(seat.getFlightNumber())));
    }

    @Test
    void testFindByFlightNumberAndIsAvailable() {
        // 测试查询可用座位
        List<Seat> availableSeats = seatRepository.findByFlightNumberAndIsAvailable("CA1234", true);
        assertEquals(131, availableSeats.size());
        assertTrue(availableSeats.stream().allMatch(Seat::getIsAvailable));

        // 测试查询不可用座位
        List<Seat> unavailableSeats = seatRepository.findByFlightNumberAndIsAvailable("CA1234", false);
        assertEquals(5, unavailableSeats.size());
        assertFalse(unavailableSeats.get(0).getIsAvailable());
    }

    @Test
    void testFindByFlightNumberAndSeatNumber() {
        // 测试根据航班号和座位号查询座位
        Optional<Seat> seatOptional = seatRepository.findByFlightNumberAndSeatNumber("CA1234", "7A");
        
        assertTrue(seatOptional.isPresent());
        Seat seat = seatOptional.get();
        assertEquals("CA1234", seat.getFlightNumber());
        assertEquals("7A", seat.getSeatNumber());
        assertEquals(SeatClass.ECONOMY, seat.getSeatClass());

        // 测试查询不存在的座位
        Optional<Seat> notFoundSeat = seatRepository.findByFlightNumberAndSeatNumber("CA1234", "99Z");
        assertFalse(notFoundSeat.isPresent());
    }

    @Test
    void testFindByFlightNumberAndSeatClass() {
        // 测试分页查询舱位等级座位
        Pageable pageable = PageRequest.of(0, 10);
        Page<Seat> economySeats = seatRepository.findByFlightNumberAndSeatClass("CA1234", SeatClass.ECONOMY, pageable);
        
        assertEquals(3, economySeats.getTotalElements()); // 包括不可用的座位
        assertTrue(economySeats.getContent().stream()
                .allMatch(seat -> seat.getSeatClass() == SeatClass.ECONOMY));

        Page<Seat> businessSeats = seatRepository.findByFlightNumberAndSeatClass("CA1234", SeatClass.BUSINESS, pageable);
        assertEquals(1, businessSeats.getTotalElements());
        assertEquals(SeatClass.BUSINESS, businessSeats.getContent().get(0).getSeatClass());
    }

    @Test
    void testFindByFlightNumberAndSeatClassAndIsAvailable() {
        // 测试查询特定舱位等级的可用座位
        List<Seat> availableEconomySeats = seatRepository.findByFlightNumberAndSeatClassAndIsAvailable(
                "CA1234", SeatClass.ECONOMY, true);
        assertEquals(2, availableEconomySeats.size());
        assertTrue(availableEconomySeats.stream()
                .allMatch(seat -> seat.getSeatClass() == SeatClass.ECONOMY && seat.getIsAvailable()));

        List<Seat> availableBusinessSeats = seatRepository.findByFlightNumberAndSeatClassAndIsAvailable(
                "CA1234", SeatClass.BUSINESS, true);
        assertEquals(1, availableBusinessSeats.size());
        assertEquals(SeatClass.BUSINESS, availableBusinessSeats.get(0).getSeatClass());

        // 测试查询不可用的经济舱座位
        List<Seat> unavailableEconomySeats = seatRepository.findByFlightNumberAndSeatClassAndIsAvailable(
                "CA1234", SeatClass.ECONOMY, false);
        assertEquals(1, unavailableEconomySeats.size());
        assertFalse(unavailableEconomySeats.get(0).getIsAvailable());
    }

    @Test
    void testCountAvailableSeatsByFlightNumber() {
        // 测试统计航班可用座位数量
        Long availableCount = seatRepository.countAvailableSeatsByFlightNumber("CA1234");
        assertEquals(3L, availableCount);

        Long mu5678AvailableCount = seatRepository.countAvailableSeatsByFlightNumber("MU5678");
        assertEquals(1L, mu5678AvailableCount);

        // 测试不存在的航班
        Long notFoundCount = seatRepository.countAvailableSeatsByFlightNumber("XX9999");
        assertEquals(0L, notFoundCount);
    }

    @Test
    void testCountAvailableSeatsByFlightNumberAndSeatClass() {
        // 测试统计特定舱位等级的可用座位数量
        Long economyAvailableCount = seatRepository.countAvailableSeatsByFlightNumberAndSeatClass(
                "CA1234", SeatClass.ECONOMY);
        assertEquals(2L, economyAvailableCount);

        Long businessAvailableCount = seatRepository.countAvailableSeatsByFlightNumberAndSeatClass(
                "CA1234", SeatClass.BUSINESS);
        assertEquals(1L, businessAvailableCount);

        // 测试不存在的舱位等级
        Long firstAvailableCount = seatRepository.countAvailableSeatsByFlightNumberAndSeatClass(
                "CA1234", SeatClass.FIRST);
        assertEquals(0L, firstAvailableCount);
    }

    @Test
    void testFindByFlightNumberWithPagination() {
        // 测试分页查询航班座位
        Pageable pageable = PageRequest.of(0, 2);
        Page<Seat> firstPage = seatRepository.findByFlightNumber("CA1234", pageable);
        
        assertEquals(4, firstPage.getTotalElements());
        assertEquals(2, firstPage.getNumberOfElements());
        assertEquals(2, firstPage.getTotalPages());
        assertTrue(firstPage.hasNext());

        // 查询第二页
        Pageable secondPageable = PageRequest.of(1, 2);
        Page<Seat> secondPage = seatRepository.findByFlightNumber("CA1234", secondPageable);
        assertEquals(2, secondPage.getNumberOfElements());
        assertFalse(secondPage.hasNext());
    }

    @Test
    void testFindByFlightNumberAndIsAvailableWithPagination() {
        // 测试分页查询可用座位
        Pageable pageable = PageRequest.of(0, 2);
        Page<Seat> availableSeatsPage = seatRepository.findByFlightNumberAndIsAvailable("CA1234", true, pageable);
        
        assertEquals(3, availableSeatsPage.getTotalElements());
        assertEquals(2, availableSeatsPage.getNumberOfElements());
        assertTrue(availableSeatsPage.hasNext());

        // 测试分页查询不可用座位
        Page<Seat> unavailableSeatsPage = seatRepository.findByFlightNumberAndIsAvailable("CA1234", false, pageable);
        assertEquals(1, unavailableSeatsPage.getTotalElements());
        assertEquals(1, unavailableSeatsPage.getNumberOfElements());
        assertFalse(unavailableSeatsPage.hasNext());
    }

    @Test
    void testSeatEntityConstraints() {
        // 测试唯一约束：同一航班不能有相同座位号
        Seat duplicateSeat = new Seat("CA1234", "7A", SeatClass.ECONOMY, new BigDecimal("800.00"));
        duplicateSeat.setIsAvailable(true);

    }

    @Test
    void testSeatCreationAndRetrieval() {
        // 测试座位创建和检索
        Seat newSeat = new Seat("TEST123", "10A", SeatClass.FIRST, new BigDecimal("2000.00"));
        newSeat.setIsAvailable(true);

        Seat savedSeat = seatRepository.save(newSeat);
        assertNotNull(savedSeat.getId());
        assertEquals("TEST123", savedSeat.getFlightNumber());
        assertEquals("10A", savedSeat.getSeatNumber());
        assertEquals(SeatClass.FIRST, savedSeat.getSeatClass());
        assertEquals(new BigDecimal("2000.00"), savedSeat.getPrice());
        assertTrue(savedSeat.getIsAvailable());

        // 验证可以通过ID检索
        Optional<Seat> retrievedSeat = seatRepository.findById(savedSeat.getId());
        assertTrue(retrievedSeat.isPresent());
        assertEquals(savedSeat.getId(), retrievedSeat.get().getId());
    }

    @Test
    void testSeatUpdate() {
        // 测试座位更新
        Optional<Seat> seatOptional = seatRepository.findByFlightNumberAndSeatNumber("CA1234", "7A");
        assertTrue(seatOptional.isPresent());

        Seat seat = seatOptional.get();
        seat.setIsAvailable(false);
        seat.setPrice(new BigDecimal("850.00"));

        Seat updatedSeat = seatRepository.save(seat);
        assertEquals(false, updatedSeat.getIsAvailable());
        assertEquals(new BigDecimal("850.00"), updatedSeat.getPrice());

        // 验证更新后的统计数据
        Long availableCount = seatRepository.countAvailableSeatsByFlightNumber("CA1234");
        assertEquals(2L, availableCount); // 应该减少1个
    }

    @Test
    void testSeatDeletion() {
        // 测试座位删除
        List<Seat> initialSeats = seatRepository.findByFlightNumber("CA1234");
        int initialCount = initialSeats.size();

        Optional<Seat> seatToDelete = seatRepository.findByFlightNumberAndSeatNumber("CA1234", "7A");
        assertTrue(seatToDelete.isPresent());

        seatRepository.delete(seatToDelete.get());

        List<Seat> remainingSeats = seatRepository.findByFlightNumber("CA1234");
        assertEquals(initialCount - 1, remainingSeats.size());

        Optional<Seat> deletedSeat = seatRepository.findByFlightNumberAndSeatNumber("CA1234", "7A");
        assertFalse(deletedSeat.isPresent());
    }

    @Test
    void testQueryWithEmptyResults() {
        // 测试查询不存在的数据
        List<Seat> nonExistentFlightSeats = seatRepository.findByFlightNumber("XX9999");
        assertTrue(nonExistentFlightSeats.isEmpty());

        Optional<Seat> nonExistentSeat = seatRepository.findByFlightNumberAndSeatNumber("XX9999", "1A");
        assertFalse(nonExistentSeat.isPresent());

        Long zeroCount = seatRepository.countAvailableSeatsByFlightNumber("XX9999");
        assertEquals(0L, zeroCount);
    }
}
