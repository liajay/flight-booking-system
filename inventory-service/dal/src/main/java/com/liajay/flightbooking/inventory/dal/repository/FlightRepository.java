package com.liajay.flightbooking.inventory.dal.repository;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 航班数据访问接口
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    /**
     * 根据航班号查询航班
     */
    Optional<Flight> findByFlightNumber(String flightNumber);

    /**
     * 根据出发地和目的地查询航班（分页）
     */
    Page<Flight> findByDepartureCityAndArrivalCityAndStatus(
            String departureCity, 
            String arrivalCity, 
            FlightStatus status, 
            Pageable pageable);

    /**
     * 根据出发地查询航班（分页）
     */
    Page<Flight> findByDepartureCityAndStatus(String departureCity, FlightStatus status, Pageable pageable);

    /**
     * 根据目的地查询航班（分页）
     */
    Page<Flight> findByArrivalCityAndStatus(String arrivalCity, FlightStatus status, Pageable pageable);

    /**
     * 根据日期范围查询航班
     */
    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :startTime AND f.departureTime <= :endTime AND f.status = :status")
    Page<Flight> findByDepartureTimeBetweenAndStatus(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") FlightStatus status,
            Pageable pageable);

    /**
     * 复合查询：出发地、目的地、日期范围
     */
    @Query("SELECT f FROM Flight f WHERE " +
           "f.departureCity = :departureCity AND " +
           "f.arrivalCity = :arrivalCity AND " +
           "f.departureTime >= :startTime AND " +
           "f.departureTime <= :endTime AND " +
           "f.status = :status")
    Page<Flight> findFlights(
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") FlightStatus status,
            Pageable pageable);

    /**
     * 根据航空公司查询航班
     */
    Page<Flight> findByAirlineAndStatus(String airline, FlightStatus status, Pageable pageable);

    /**
     * 查询所有有效航班（分页）
     */
    Page<Flight> findByStatus(FlightStatus status, Pageable pageable);
}
