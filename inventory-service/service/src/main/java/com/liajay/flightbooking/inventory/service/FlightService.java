package com.liajay.flightbooking.inventory.service;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;

/**
 * 航班服务接口
 * Service层 - 业务逻辑接口
 * 
 * 简化设计：只保留核心的查询方法，通过 FlightQueryDTO 参数来支持各种查询场景
 */
public interface FlightService {

    /**
     * 根据航班号查询航班
     */
    Flight findByFlightNumber(String flightNumber);

    /**
     * 通用航班查询方法
     * 支持多种查询条件组合：
     * - 航班号查询：设置 flightNumber
     * - 航线查询：设置 departureCity 和 arrivalCity  
     * - 航空公司查询：设置 airline
     * - 日期范围查询：设置 startTime 和 endTime
     * - 状态查询：设置 status
     * - 分页查询：设置分页参数
     * - 排序查询：设置排序参数
     */
    FlightQueryResultDTO queryFlights(FlightQueryDTO queryDTO);
}
