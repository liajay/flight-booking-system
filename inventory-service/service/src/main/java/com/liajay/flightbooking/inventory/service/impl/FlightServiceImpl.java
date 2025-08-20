package com.liajay.flightbooking.inventory.service.impl;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.FlightStatus;
import com.liajay.flightbooking.inventory.dal.repository.FlightRepository;
import com.liajay.flightbooking.inventory.dal.repository.SeatRepository;
import com.liajay.flightbooking.inventory.model.vo.FlightVO;
import com.liajay.flightbooking.inventory.service.FlightService;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * 航班服务实现类
 */
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public FlightServiceImpl(FlightRepository flightRepository, SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public Flight findByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber).orElse(null);
    }

    @Override
    public FlightQueryResultDTO queryFlights(FlightQueryDTO queryDTO) {
        Pageable pageable = createPageable(queryDTO);
        
        Page<Flight> flightPage;
        
        // 根据查询条件选择合适的查询方法
        if (hasCompleteRouteAndDateInfo(queryDTO)) {
            // 完整的出发地、目的地、日期查询
            FlightStatus status = parseFlightStatus(queryDTO.getStatus());
            flightPage = flightRepository.findFlights(
                queryDTO.getDepartureCity(),
                queryDTO.getArrivalCity(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime(),
                status,
                pageable
            );
        } else if (hasBothCities(queryDTO)) {
            // 出发地和目的地查询
            FlightStatus status = parseFlightStatus(queryDTO.getStatus());
            flightPage = flightRepository.findByDepartureCityAndArrivalCityAndStatus(
                queryDTO.getDepartureCity(),
                queryDTO.getArrivalCity(),
                status,
                pageable
            );
        } else if (hasDateRange(queryDTO)) {
            // 日期范围查询
            FlightStatus status = parseFlightStatus(queryDTO.getStatus());
            flightPage = flightRepository.findByDepartureTimeBetweenAndStatus(
                queryDTO.getStartTime(),
                queryDTO.getEndTime(),
                status,
                pageable
            );
        } else {
            // 默认查询所有有效航班
            FlightStatus status = parseFlightStatus(queryDTO.getStatus());
            flightPage = flightRepository.findByStatus(status, pageable);
        }

        Page<FlightVO> flightVOPage = flightPage.map(this::convertToVO);
        return new FlightQueryResultDTO(flightVOPage);
    }

    @Override
    public FlightQueryResultDTO queryFlightsByRoute(String departureCity, String arrivalCity, FlightQueryDTO queryDTO) {
        Pageable pageable = createPageable(queryDTO);
        FlightStatus status = parseFlightStatus(queryDTO.getStatus());
        
        Page<Flight> flightPage = flightRepository.findByDepartureCityAndArrivalCityAndStatus(
            departureCity, arrivalCity, status, pageable
        );

        Page<FlightVO> flightVOPage = flightPage.map(this::convertToVO);
        return new FlightQueryResultDTO(flightVOPage);
    }

    @Override
    public FlightQueryResultDTO queryFlightsByAirline(String airline, FlightQueryDTO queryDTO) {
        Pageable pageable = createPageable(queryDTO);
        FlightStatus status = parseFlightStatus(queryDTO.getStatus());
        
        Page<Flight> flightPage = flightRepository.findByAirlineAndStatus(airline, status, pageable);

        Page<FlightVO> flightVOPage = flightPage.map(this::convertToVO);
        return new FlightQueryResultDTO(flightVOPage);
    }

    @Override
    public FlightQueryResultDTO queryFlightsByDateRange(FlightQueryDTO queryDTO) {
        Pageable pageable = createPageable(queryDTO);
        FlightStatus status = parseFlightStatus(queryDTO.getStatus());
        
        Page<Flight> flightPage = flightRepository.findByDepartureTimeBetweenAndStatus(
            queryDTO.getStartTime(), queryDTO.getEndTime(), status, pageable
        );

        Page<FlightVO> flightVOPage = flightPage.map(this::convertToVO);
        return new FlightQueryResultDTO(flightVOPage);
    }

    @Override
    public FlightQueryResultDTO queryActiveFlights(FlightQueryDTO queryDTO) {
        Pageable pageable = createPageable(queryDTO);
        Page<Flight> flightPage = flightRepository.findByStatus(FlightStatus.SCHEDULED, pageable);

        Page<FlightVO> flightVOPage = flightPage.map(this::convertToVO);
        return new FlightQueryResultDTO(flightVOPage);
    }

    /**
     * 创建分页对象
     */
    private Pageable createPageable(FlightQueryDTO queryDTO) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(queryDTO.getSortDirection()) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, queryDTO.getSortBy());
        return PageRequest.of(queryDTO.getPage(), queryDTO.getSize(), sort);
    }

    /**
     * 解析航班状态
     */
    private FlightStatus parseFlightStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return FlightStatus.SCHEDULED;
        }
        try {
            return FlightStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return FlightStatus.SCHEDULED;
        }
    }

    /**
     * 检查是否有完整的路线和日期信息
     */
    private boolean hasCompleteRouteAndDateInfo(FlightQueryDTO queryDTO) {
        return queryDTO.getDepartureCity() != null && 
               queryDTO.getArrivalCity() != null &&
               queryDTO.getStartTime() != null && 
               queryDTO.getEndTime() != null;
    }

    /**
     * 检查是否有出发地和目的地
     */
    private boolean hasBothCities(FlightQueryDTO queryDTO) {
        return queryDTO.getDepartureCity() != null && queryDTO.getArrivalCity() != null;
    }

    /**
     * 检查是否有日期范围
     */
    private boolean hasDateRange(FlightQueryDTO queryDTO) {
        return queryDTO.getStartTime() != null && queryDTO.getEndTime() != null;
    }

    /**
     * 转换为VO对象
     */
    private FlightVO convertToVO(Flight flight) {
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
            flight.getStatus().getDescription(),
            flight.getCreatedTime()
        );

        // 添加座位统计信息
        Long availableSeats = seatRepository.countAvailableSeatsByFlightId(flight.getId());
        vo.setAvailableSeats(availableSeats);

        return vo;
    }
}
