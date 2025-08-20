package com.liajay.flightbooking.inventory.service;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;

/**
 * 航班服务接口
 * Service层 - 业务逻辑接口
 */
public interface FlightService {

    /**
     * 根据航班号查询航班
     */
    Flight findByFlightNumber(String flightNumber);

    /**
     * 分页查询航班
     */
    FlightQueryResultDTO queryFlights(FlightQueryDTO queryDTO);

    /**
     * 根据出发地和目的地查询航班
     */
    FlightQueryResultDTO queryFlightsByRoute(String departureCity, String arrivalCity, FlightQueryDTO queryDTO);

    /**
     * 根据航空公司查询航班
     */
    FlightQueryResultDTO queryFlightsByAirline(String airline, FlightQueryDTO queryDTO);

    /**
     * 根据日期范围查询航班
     */
    FlightQueryResultDTO queryFlightsByDateRange(FlightQueryDTO queryDTO);

    /**
     * 获取所有有效航班
     */
    FlightQueryResultDTO queryActiveFlights(FlightQueryDTO queryDTO);
}
