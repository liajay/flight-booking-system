package com.liajay.flightbooking.inventory.service.impl;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.FlightStatus;
import com.liajay.flightbooking.inventory.dal.mapper.FlightMapper;
import com.liajay.flightbooking.inventory.dal.mapper.SeatMapper;
import com.liajay.flightbooking.inventory.model.vo.FlightVO;
import com.liajay.flightbooking.inventory.service.FlightService;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.PageResult;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 航班服务实现类 - MyBatis版本
 * 使用MyBatis原生分页实现
 */
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightMapper flightMapper;
    private final SeatMapper seatMapper;

    public FlightServiceImpl(FlightMapper flightMapper, SeatMapper seatMapper) {
        this.flightMapper = flightMapper;
        this.seatMapper = seatMapper;
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        return flightMapper.findByFlightNumber(flightNumber);
    }

    @Override
    public FlightQueryResultDTO queryFlights(FlightQueryDTO queryDTO) {
        FlightStatus statusEnum = parseFlightStatus(queryDTO.getStatus());
        
        if (queryDTO.isPaginationEnabled()) {
            long total = flightMapper.countByConditions(
                queryDTO.getFlightNumber(),
                queryDTO.getAirline(),
                queryDTO.getDepartureCity(),
                queryDTO.getArrivalCity(),
                null, // 暂时简化，不使用时间范围
                null,
                statusEnum
            );

            int offset = queryDTO.getPage() * queryDTO.getSize();

            List<Flight> flights = flightMapper.findByConditions(
                queryDTO.getFlightNumber(),
                queryDTO.getAirline(),
                queryDTO.getDepartureCity(),
                queryDTO.getArrivalCity(),
                null, // 暂时简化，不使用时间范围
                null,
                statusEnum,
                offset,
                queryDTO.getSize()
            );
            
            List<FlightVO> flightVOs = flights.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            
            PageResult<FlightVO> pageResult = new PageResult<>(flightVOs, total, queryDTO.getPage() + 1, queryDTO.getSize());
            return FlightQueryResultDTO.fromPageResult(pageResult);
        } else {
            // 非分页查询
            List<Flight> flights = flightMapper.findByConditions(
                queryDTO.getFlightNumber(),
                queryDTO.getAirline(),
                queryDTO.getDepartureCity(),
                queryDTO.getArrivalCity(),
                null, // 暂时简化，不使用时间范围
                null,
                statusEnum,
                0,
                -1
            );
            
            List<FlightVO> flightVOs = flights.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            return FlightQueryResultDTO.fromList(flightVOs);
        }
    }

    /**
     * 解析航班状态
     */
    private FlightStatus parseFlightStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }
        try {
            return FlightStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 转换为VO对象
     */
    private FlightVO convertToVO(Flight flight) {
        // 创建基本的FlightVO对象
        FlightVO vo = new FlightVO(
            flight.getId(),
            flight.getFlightNumber(),
            flight.getAirline(),
            flight.getDepartureCity(),
            flight.getArrivalCity(),
            flight.getDepartureTime(),
            flight.getArrivalTime(),
            flight.getBasePrice(),
            flight.getStatus().name(),
            flight.getStatus().getDescription()
        );

        long totalSeats = seatMapper.countByConditions(flight.getFlightNumber(), null, null, null, null, null, null);
        long availableSeats = seatMapper.countByConditions(flight.getFlightNumber(), null, true, null, null, null, null);
        vo.setTotalSeats(totalSeats);
        vo.setAvailableSeats(availableSeats);

        return vo;
    }
}
