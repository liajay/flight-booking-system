package com.liajay.flightbooking.inventory.service.impl;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.dal.dataobject.SeatClass;
import com.liajay.flightbooking.inventory.dal.repository.FlightRepository;
import com.liajay.flightbooking.inventory.dal.repository.SeatRepository;
import com.liajay.flightbooking.inventory.model.vo.SeatVO;
import com.liajay.flightbooking.inventory.service.SeatService;
import com.liajay.flightbooking.inventory.service.dto.SeatQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.SeatQueryResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位服务实现类
 */
@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;

    public SeatServiceImpl(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public SeatQueryResultDTO findSeatsByFlightId(Long flightId, SeatQueryDTO queryDTO) {
        if (queryDTO.getPage() != null && queryDTO.getSize() != null) {
            // 分页查询
            Pageable pageable = createPageable(queryDTO);
            Page<Seat> seatPage = seatRepository.findByFlightId(flightId, pageable);
            Page<SeatVO> seatVOPage = seatPage.map(seat -> convertToVO(seat));
            return new SeatQueryResultDTO(seatVOPage);
        } else {
            // 非分页查询
            List<Seat> seats = seatRepository.findByFlightId(flightId);
            List<SeatVO> seatVOs = seats.stream().map(this::convertToVO).collect(Collectors.toList());
            return new SeatQueryResultDTO(seatVOs);
        }
    }

    @Override
    public SeatQueryResultDTO findSeatsByFlightNumber(String flightNumber, SeatQueryDTO queryDTO) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber).orElse(null);
        if (flight == null) {
            return new SeatQueryResultDTO(List.of());
        }
        return findSeatsByFlightId(flight.getId(), queryDTO);
    }

    @Override
    public SeatQueryResultDTO findAvailableSeatsByFlightId(Long flightId, SeatQueryDTO queryDTO) {
        if (queryDTO.getPage() != null && queryDTO.getSize() != null) {
            // 分页查询
            Pageable pageable = createPageable(queryDTO);
            Page<Seat> seatPage = seatRepository.findByFlightIdAndIsAvailable(flightId, true, pageable);
            Page<SeatVO> seatVOPage = seatPage.map(seat -> convertToVO(seat));
            return new SeatQueryResultDTO(seatVOPage);
        } else {
            // 非分页查询
            List<Seat> seats = seatRepository.findByFlightIdAndIsAvailable(flightId, true);
            List<SeatVO> seatVOs = seats.stream().map(this::convertToVO).collect(Collectors.toList());
            return new SeatQueryResultDTO(seatVOs);
        }
    }

    @Override
    public SeatQueryResultDTO findAvailableSeatsByFlightNumber(String flightNumber, SeatQueryDTO queryDTO) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber).orElse(null);
        if (flight == null) {
            return new SeatQueryResultDTO(List.of());
        }
        return findAvailableSeatsByFlightId(flight.getId(), queryDTO);
    }

    @Override
    public SeatQueryResultDTO findSeatsByFlightIdAndClass(Long flightId, String seatClass, SeatQueryDTO queryDTO) {
        SeatClass seatClassEnum = parseSeatClass(seatClass);
        if (seatClassEnum == null) {
            return new SeatQueryResultDTO(List.of());
        }

        if (queryDTO.getPage() != null && queryDTO.getSize() != null) {
            // 分页查询
            Pageable pageable = createPageable(queryDTO);
            Page<Seat> seatPage = seatRepository.findByFlightIdAndSeatClass(flightId, seatClassEnum, pageable);
            Page<SeatVO> seatVOPage = seatPage.map(seat -> convertToVO(seat));
            return new SeatQueryResultDTO(seatVOPage);
        } else {
            // 非分页查询
            Boolean isAvailable = queryDTO.getIsAvailable();
            List<Seat> seats;
            if (isAvailable != null) {
                seats = seatRepository.findByFlightIdAndSeatClassAndIsAvailable(flightId, seatClassEnum, isAvailable);
            } else {
                seats = seatRepository.findByFlightIdAndSeatClass(flightId, seatClassEnum, Pageable.unpaged()).getContent();
            }
            List<SeatVO> seatVOs = seats.stream().map(this::convertToVO).collect(Collectors.toList());
            return new SeatQueryResultDTO(seatVOs);
        }
    }

    @Override
    public Seat findSeatByFlightIdAndSeatNumber(Long flightId, String seatNumber) {
        return seatRepository.findByFlightIdAndSeatNumber(flightId, seatNumber).orElse(null);
    }

    @Override
    public SeatQueryResultDTO getFlightSeatStatistics(Long flightId) {
        List<Seat> allSeats = seatRepository.findByFlightId(flightId);
        
        SeatQueryResultDTO result = new SeatQueryResultDTO();
        result.setTotalSeats((long) allSeats.size());
        
        long availableCount = allSeats.stream()
            .mapToLong(seat -> seat.getIsAvailable() ? 1L : 0L)
            .sum();
        
        result.setAvailableSeats(availableCount);
        result.setOccupiedSeats(result.getTotalSeats() - availableCount);
        
        // 转换为VO列表
        List<SeatVO> seatVOs = allSeats.stream().map(this::convertToVO).collect(Collectors.toList());
        result.setSeatList(seatVOs);
        
        return result;
    }

    /**
     * 创建分页对象
     */
    private Pageable createPageable(SeatQueryDTO queryDTO) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(queryDTO.getSortDirection()) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, queryDTO.getSortBy());
        return PageRequest.of(queryDTO.getPage(), queryDTO.getSize(), sort);
    }

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
            seat.getFlightId(),
            seat.getSeatNumber(),
            seat.getSeatClass().name(),
            seat.getSeatClass().getDescription(),
            seat.getIsAvailable(),
            seat.getPrice(),
            seat.getCreatedTime()
        );

        // 添加航班信息
        Flight flight = flightRepository.findById(seat.getFlightId()).orElse(null);
        if (flight != null) {
            vo.setFlightNumber(flight.getFlightNumber());
            vo.setAirline(flight.getAirline());
        }

        return vo;
    }
}
