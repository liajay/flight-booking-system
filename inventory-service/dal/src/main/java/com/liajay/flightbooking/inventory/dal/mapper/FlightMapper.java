package com.liajay.flightbooking.inventory.dal.mapper;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.FlightStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 航班数据访问Mapper - MyBatis版本
 * 精简版：保留核心通用查询方法
 */
@Mapper
public interface FlightMapper {

    /**
     * 根据航班号查询航班
     */
    Flight findByFlightNumber(@Param("flightNumber") String flightNumber);

    /**
     * 复杂条件查询航班
     * 支持多种查询条件的组合查询，使用动态SQL
     */
    List<Flight> findByConditions(
            @Param("flightNumber") String flightNumber,
            @Param("airline") String airline,
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") FlightStatus status,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    /**
     * 根据条件统计航班总数
     */
    long countByConditions(
            @Param("flightNumber") String flightNumber,
            @Param("airline") String airline,
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") FlightStatus status);
}
