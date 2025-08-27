package com.liajay.flightbooking.inventory.service;

import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.service.dto.SeatQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.SeatQueryResultDTO;

/**
 * 座位服务接口 - MyBatis版本
 * Service层 - 业务逻辑接口
 * 
 * 简化设计：只保留核心的查询方法，通过 SeatQueryDTO 参数来支持各种查询场景
 */
public interface SeatService {
    
    /**
     * 根据航班号和座位号查询特定座位
     */
    Seat findSeatByFlightNumberAndSeatNumber(String flightNumber, String seatNumber);

    /**
     * 通用座位查询方法
     * 支持多种查询条件组合：
     * - 航班号查询：设置 flightNumber
     * - 舱位等级查询：设置 seatClass
     * - 可用性查询：设置 isAvailable
     * - 价格范围查询：设置 minPrice 和 maxPrice
     * - 分页查询：设置分页参数
     * - 排序查询：设置排序参数
     */
    SeatQueryResultDTO querySeats(SeatQueryDTO queryDTO);

    /**
     * 为指定航班分配一个座位
     * 查找并分配一个可用座位，将其标记为不可用
     * 
     * @param flightNumber 航班号
     * @return 分配的座位，如果没有可用座位则返回null
     */
    Seat allocateSeat(String flightNumber);
}
