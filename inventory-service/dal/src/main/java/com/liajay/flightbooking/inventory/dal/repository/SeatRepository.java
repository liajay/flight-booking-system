package com.liajay.flightbooking.inventory.dal.repository;

import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 座位数据访问接口
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    /**
     * 根据航班ID查询所有座位
     */
    List<Seat> findByFlightNumber(String flightNumber);

    /**
     * 根据航班ID查询可用座位
     */
    List<Seat> findByFlightNumberAndIsAvailable(String flightNumber, Boolean isAvailable);

    /**
     * 根据航班ID和座位号查询座位
     */
    Optional<Seat> findByFlightNumberAndSeatNumber(String flightNumber, String seatNumber);

    /**
     * 根据航班ID和舱位等级查询座位（分页）
     */
    Page<Seat> findByFlightNumberAndSeatClass(String flightNumber, SeatClass seatClass, Pageable pageable);

    /**
     * 根据航班ID和舱位等级查询可用座位
     */
    List<Seat> findByFlightNumberAndSeatClassAndIsAvailable(String flightNumber, SeatClass seatClass, Boolean isAvailable);

    /**
     * 统计航班可用座位数量
     */
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.flightNumber = :flightNumber AND s.isAvailable = true")
    Long countAvailableSeatsByFlightNumber(@Param("flightNumber") String flightNumber);

    /**
     * 统计航班某舱位等级的可用座位数量
     */
    @Query("SELECT COUNT(s) FROM Seat s WHERE s.flightNumber = :flightNumber AND s.seatClass = :seatClass AND s.isAvailable = true")
    Long countAvailableSeatsByFlightNumberAndSeatClass(@Param("flightNumber") String flightNumber, @Param("seatClass") SeatClass seatClass);

    /**
     * 根据航班ID分页查询座位
     */
    Page<Seat> findByFlightNumber(String flightNumber, Pageable pageable);

    /**
     * 根据航班ID和可用性分页查询座位
     */
    Page<Seat> findByFlightNumberAndIsAvailable(String flightNumber, Boolean isAvailable, Pageable pageable);
}
