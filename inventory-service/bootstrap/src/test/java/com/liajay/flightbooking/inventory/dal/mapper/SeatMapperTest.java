package com.liajay.flightbooking.inventory.dal.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SeatMapper MyBatis精简版测试类
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SeatMapperTest {

    @Autowired
    private SeatMapper seatMapper;

    @Test
    void testFindByConditionsAllSeats() {
        // 测试查询CA1234的所有座位
        Page<Seat> ca1234Seats = seatMapper.findByConditions("CA1234", null, null, null, null, null, null);
        assertEquals(136, ca1234Seats.size()); // 商务舱16座 + 经济舱120座
        assertTrue(ca1234Seats.stream().allMatch(seat -> "CA1234".equals(seat.getFlightNumber())));
    }

    @Test
    void testFindByConditionsWithPageHelper() {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(1, 10, "seat_class, seat_number");
        Page<Seat> seats = seatMapper.findByConditions("CA1234", null, null, null, null, null, null);
        PageInfo<Seat> pageInfo = new PageInfo<>(seats);
        
        assertEquals(136, pageInfo.getTotal()); // 总记录数
        assertEquals(10, pageInfo.getSize()); // 当前页记录数
        assertEquals(14, pageInfo.getPages()); // 总页数
        assertEquals(1, pageInfo.getPageNum()); // 当前页号
        assertTrue(pageInfo.isHasNextPage()); // 有下一页
        assertFalse(pageInfo.isHasPreviousPage()); // 没有上一页
    }

    @Test
    void testFindByConditionsAvailableSeats() {
        // 测试查询CA1234可用座位
        Page<Seat> availableSeats = seatMapper.findByConditions("CA1234", null, true, null, null, null, null);
        assertEquals(131, availableSeats.size());
        assertTrue(availableSeats.stream().allMatch(Seat::getIsAvailable));

        // 测试查询不可用座位
        Page<Seat> unavailableSeats = seatMapper.findByConditions("CA1234", null, false, null, null, null, null);
        assertEquals(5, unavailableSeats.size());
        assertTrue(unavailableSeats.stream().noneMatch(Seat::getIsAvailable));
    }

    @Test
    void testFindByFlightNumberAndSeatNumber() {
        // 测试查询特定座位
        Seat seat = seatMapper.findByFlightNumberAndSeatNumber("CA1234", "1A");
        assertNotNull(seat);
        assertEquals("CA1234", seat.getFlightNumber());
        assertEquals("1A", seat.getSeatNumber());
        assertEquals(SeatClass.BUSINESS, seat.getSeatClass());

        // 测试查询不存在的座位
        Seat notFoundSeat = seatMapper.findByFlightNumberAndSeatNumber("CA1234", "99Z");
        assertNull(notFoundSeat);
    }

    @Test
    void testFindByConditionsSeatClass() {
        // 测试查询CA1234经济舱座位
        Page<Seat> economySeats = seatMapper.findByConditions("CA1234", SeatClass.ECONOMY, null, null, null, null, null);
        assertEquals(120, economySeats.size());
        assertTrue(economySeats.stream().allMatch(seat -> seat.getSeatClass() == SeatClass.ECONOMY));

        // 测试查询商务舱座位
        Page<Seat> businessSeats = seatMapper.findByConditions("CA1234", SeatClass.BUSINESS, null, null, null, null, null);
        assertEquals(16, businessSeats.size());
        assertTrue(businessSeats.stream().allMatch(seat -> seat.getSeatClass() == SeatClass.BUSINESS));
    }

    @Test
    void testFindByConditionsSeatClassAndAvailability() {
        // 测试查询CA1234可用经济舱座位
        Page<Seat> availableEconomySeats = seatMapper.findByConditions("CA1234", SeatClass.ECONOMY, true, null, null, null, null);
        assertEquals(117, availableEconomySeats.size());
        assertTrue(availableEconomySeats.stream()
                .allMatch(seat -> seat.getSeatClass() == SeatClass.ECONOMY && seat.getIsAvailable()));

        // 测试查询可用商务舱座位
        Page<Seat> availableBusinessSeats = seatMapper.findByConditions("CA1234", SeatClass.BUSINESS, true, null, null, null, null);
        assertEquals(14, availableBusinessSeats.size());
        assertTrue(availableBusinessSeats.stream()
                .allMatch(seat -> seat.getSeatClass() == SeatClass.BUSINESS && seat.getIsAvailable()));
    }

    @Test
    void testFindByConditionsWithPriceRange() {
        Page<Seat> seatsInPriceRange = seatMapper.findByConditions(
                "CA1234", SeatClass.ECONOMY, true, new BigDecimal("800"), new BigDecimal("900"), null, null);

        assertTrue(seatsInPriceRange.size() > 0);
        assertTrue(seatsInPriceRange.stream()
                .allMatch(seat -> seat.getFlightNumber().equals("CA1234") &&
                          seat.getSeatClass() == SeatClass.ECONOMY &&
                          seat.getIsAvailable() &&
                          seat.getPrice().compareTo(new BigDecimal("800")) >= 0 &&
                          seat.getPrice().compareTo(new BigDecimal("900")) <= 0));
    }

    @Test
    void testFindByConditionsWithSeatNumberRange() {
        // 测试座位号范围查询
        Page<Seat> seatsInRange = seatMapper.findByConditions(
                "CA1234", null, null, null, null, "1A", "5F");

        assertTrue(seatsInRange.size() > 0);
        assertTrue(seatsInRange.stream()
                .allMatch(seat -> seat.getSeatNumber().compareTo("1A") >= 0 && 
                          seat.getSeatNumber().compareTo("5F") <= 0));
    }

    @Test
    void testFindByConditionsComplexQuery() {
        // 测试复杂条件查询
        PageHelper.startPage(1, 5);
        Page<Seat> seats = seatMapper.findByConditions(
                "CA1234",
                SeatClass.ECONOMY,
                true,
                new BigDecimal("800"),
                new BigDecimal("900"),
                null,
                null
        );

        PageInfo<Seat> pageInfo = new PageInfo<>(seats);
        assertTrue(pageInfo.getTotal() > 0);
        assertTrue(pageInfo.getList().stream()
                .allMatch(seat -> seat.getFlightNumber().equals("CA1234") &&
                          seat.getSeatClass() == SeatClass.ECONOMY &&
                          seat.getIsAvailable() &&
                          seat.getPrice().compareTo(new BigDecimal("800")) >= 0 &&
                          seat.getPrice().compareTo(new BigDecimal("900")) <= 0));
    }

    @Test
    void testFindByConditionsWithPagination() {
        PageHelper.startPage(2, 10); // 第二页，每页10条
        Page<Seat> seats = seatMapper.findByConditions("CA1234", null, null, null, null, null, null);
        
        PageInfo<Seat> pageInfo = new PageInfo<>(seats);
        assertEquals(136, pageInfo.getTotal());
        assertEquals(10, pageInfo.getList().size());
        assertEquals(2, pageInfo.getPageNum());
        assertTrue(pageInfo.isHasPreviousPage());
        assertTrue(pageInfo.isHasNextPage());
    }

    @Test
    void testFindByConditionsEmptyResult() {
        Page<Seat> seats = seatMapper.findByConditions("NONEXISTENT", null, null, null, null, null, null);
        assertEquals(0, seats.size());
    }

    @Test
    void testFindByConditionsResultsSorted() {
        Page<Seat> seats = seatMapper.findByConditions("CA1234", null, null, null, null, null, null);

        for (int i = 1; i < seats.size(); i++) {
            Seat prev = seats.get(i - 1);
            Seat curr = seats.get(i);
            
            int classComparison = prev.getSeatClass().compareTo(curr.getSeatClass());
            if (classComparison == 0) {
                assertTrue(prev.getSeatNumber().compareTo(curr.getSeatNumber()) <= 0);
            }
        }
    }

    @Test
    void testQueryAllFlightSeats() {
        // 测试查询多个航班的座位
        Page<Seat> ca1234Seats = seatMapper.findByConditions("CA1234", null, null, null, null, null, null);
        assertEquals(136, ca1234Seats.size());

        Page<Seat> mu5678Seats = seatMapper.findByConditions("MU5678", null, null, null, null, null, null);
        assertTrue(mu5678Seats.size() > 0);
        assertTrue(mu5678Seats.stream().allMatch(seat -> "MU5678".equals(seat.getFlightNumber())));
    }
}
