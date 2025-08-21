package com.liajay.flightbooking.inventory.service;

import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.service.dto.SeatQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.SeatQueryResultDTO;

import java.util.List;

/**
 * 座位服务接口
 * Service层 - 业务逻辑接口
 */
public interface SeatService {
    
    /**
     * 根据航班号查询所有座位
     */
    SeatQueryResultDTO findSeatsByFlightNumber(String flightNumber, SeatQueryDTO queryDTO);
    
    /**
     * 根据航班号查询可用座位
     */
    SeatQueryResultDTO findAvailableSeatsByFlightNumber(String flightNumber, SeatQueryDTO queryDTO);

    /**
     * 根据航班ID和舱位等级查询座位
     */
    SeatQueryResultDTO findSeatsByFlightNumberAndClass(String FlightNumber, String seatClass, SeatQueryDTO queryDTO);

    /**
     * 根据航班ID和座位号查询特定座位
     */
    Seat findSeatByFlightNumberAndSeatNumber(String FlightNumber, String seatNumber);

    /**
     * 统计航班座位信息
     */
    SeatQueryResultDTO getFlightSeatStatistics(String flightNumber);
}
