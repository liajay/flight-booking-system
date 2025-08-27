package com.liajay.flightbooking.inventory.service.impl;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import com.liajay.flightbooking.inventory.dal.mapper.SeatMapper;
import com.liajay.flightbooking.inventory.dal.mapper.FlightMapper;
import com.liajay.flightbooking.inventory.model.vo.SeatVO;
import com.liajay.flightbooking.inventory.service.SeatService;
import com.liajay.flightbooking.inventory.service.dto.PageResult;
import com.liajay.flightbooking.inventory.service.dto.SeatQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.SeatQueryResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位服务实现类 - MyBatis版本
 * 使用MyBatis原生分页实现
 */
@Service
public class SeatServiceImpl implements SeatService {

    private final SeatMapper seatMapper;
    private final FlightMapper flightMapper;

    public SeatServiceImpl(SeatMapper seatMapper, FlightMapper flightMapper) {
        this.seatMapper = seatMapper;
        this.flightMapper = flightMapper;
    }

    @Override
    public Seat findSeatByFlightNumberAndSeatNumber(String flightNumber, String seatNumber) {
        return seatMapper.findByFlightNumberAndSeatNumber(flightNumber, seatNumber);
    }

    @Override
    public SeatQueryResultDTO querySeats(SeatQueryDTO queryDTO) {
        SeatClass seatClassEnum = parseSeatClass(queryDTO.getSeatClass());
        
        if (queryDTO.isPaginationEnabled()) {
            // 先获取总数
            long total = seatMapper.countByConditions(
                queryDTO.getFlightNumber(),
                seatClassEnum,
                queryDTO.getIsAvailable(),
                queryDTO.getMinPrice(),
                queryDTO.getMaxPrice(),
                queryDTO.getSeatNumberStart(),
                queryDTO.getSeatNumberEnd()
            );
            
            // 计算分页参数
            int offset = queryDTO.getPage() * queryDTO.getSize();
            
            // 分页查询
            List<Seat> seats = seatMapper.findByConditions(
                queryDTO.getFlightNumber(),
                seatClassEnum,
                queryDTO.getIsAvailable(),
                queryDTO.getMinPrice(),
                queryDTO.getMaxPrice(),
                queryDTO.getSeatNumberStart(),
                queryDTO.getSeatNumberEnd(),
                offset,
                queryDTO.getSize()
            );
            
            // 转换为VO并构建分页结果
            List<SeatVO> seatVOs = seats.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            
            PageResult<SeatVO> pageResult = new PageResult<>(seatVOs, total, queryDTO.getPage() + 1, queryDTO.getSize());
            return SeatQueryResultDTO.fromPageResult(pageResult);
        } else {
            // 非分页查询 - 设置offset=0, pageSize=-1表示查询所有
            List<Seat> seats = seatMapper.findByConditions(
                queryDTO.getFlightNumber(),
                seatClassEnum,
                queryDTO.getIsAvailable(),
                queryDTO.getMinPrice(),
                queryDTO.getMaxPrice(),
                queryDTO.getSeatNumberStart(),
                queryDTO.getSeatNumberEnd(),
                0,
                -1  // -1表示不限制数量
            );
            
            List<SeatVO> seatVOs = seats.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            return SeatQueryResultDTO.fromList(seatVOs);
        }
    }

    // 以下方法已简化，统一使用 querySeats 方法实现
    // 调用者可以通过设置 SeatQueryDTO 的相应字段来实现这些查询功能

    /**
     * 解析座位舱位等级
     */
    private SeatClass parseSeatClass(String seatClass) {
        if (seatClass == null || seatClass.trim().isEmpty()) {
            return null;
        }
        try {
            return SeatClass.valueOf(seatClass.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 转换为VO对象
     */
    private SeatVO convertToVO(Seat seat) {
        SeatVO vo = new SeatVO(
            seat.getId(),
            seat.getFlightNumber(),
            seat.getSeatNumber(),
            seat.getSeatClass().name(),
            seat.getSeatClass().getDescription(),
            seat.getIsAvailable(),
            seat.getPrice()
        );

        // 添加航班信息
        Flight flight = flightMapper.findByFlightNumber(seat.getFlightNumber());
        if (flight != null) {
            vo.setFlightNumber(flight.getFlightNumber());
            vo.setAirline(flight.getAirline());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Seat allocateSeat(String flightNumber) {
        // 查找第一个可用座位
        Seat availableSeat = seatMapper.findFirstAvailableSeat(flightNumber);
        
        if (availableSeat == null) {
            return null; // 没有可用座位
        }
        
        // 将座位标记为不可用
        int updateResult = seatMapper.updateSeatAvailability(availableSeat.getId(), false);
        
        if (updateResult <= 0) {
            throw new RuntimeException("更新座位状态失败");
        }
        
        // 更新返回对象的状态
        availableSeat.setIsAvailable(false);
        
        return availableSeat;
    }
}
