package com.liajay.flightbooking.inventory.web.controller;

import com.liajay.flightbooking.inventory.dal.dataobject.Seat;
import com.liajay.flightbooking.inventory.service.SeatService;
import com.liajay.flightbooking.inventory.service.dto.SeatQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.SeatQueryResultDTO;
import com.liajay.flightbooking.inventory.web.convertor.SeatConvertor;
import com.liajay.flightbooking.inventory.web.request.SeatQueryRequest;
import com.liajay.flightbooking.inventory.web.response.HttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 座位控制器
 * Web层 - 处理HTTP请求和响应
 */
@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }


    /**
     * 根据航班号查询座位
     */
    @GetMapping("/flight-number/{flightNumber}")
    public HttpResponse<SeatQueryResultDTO> getSeatsByFlightNumber(
            @PathVariable String flightNumber,
            @Valid SeatQueryRequest request) {
        try {
            SeatQueryDTO queryDTO = SeatConvertor.convertToDTO(request);
            queryDTO.setFlightNumber(flightNumber);
            SeatQueryResultDTO result = seatService.querySeats(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("根据航班号查询座位失败: " + e.getMessage());
        }
    }

    /**
     * 根据航班号查询可用座位
     */
    @GetMapping("/flight-number/{flightNumber}/available")
    public HttpResponse<SeatQueryResultDTO> getAvailableSeatsByFlightNumber(
            @PathVariable String flightNumber,
            @Valid SeatQueryRequest request) {
        try {
            SeatQueryDTO queryDTO = SeatConvertor.convertToDTO(request);
            queryDTO.setFlightNumber(flightNumber);
            queryDTO.setIsAvailable(true);
            SeatQueryResultDTO result = seatService.querySeats(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("根据航班号查询可用座位失败: " + e.getMessage());
        }
    }

    /**
     * 根据航班ID和舱位等级查询座位
     */
    @GetMapping("/flight/{FlightNumber}/class/{seatClass}")
    public HttpResponse<SeatQueryResultDTO> getSeatsByFlightNumberAndClass(
            @PathVariable String flightNumber,
            @PathVariable String seatClass,
            @Valid SeatQueryRequest request) {
        try {
            SeatQueryDTO queryDTO = SeatConvertor.convertToDTO(request);
            queryDTO.setFlightNumber(flightNumber);
            queryDTO.setSeatClass(seatClass);
            SeatQueryResultDTO result = seatService.querySeats(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("根据航班ID和舱位等级查询座位失败: " + e.getMessage());
        }
    }

    /**
     * 根据航班ID和座位号查询特定座位
     */
    @GetMapping("/flight/{FlightNumber}/seat/{seatNumber}")
    public HttpResponse<Seat> getSeatByFlightNumberAndSeatNumber(
            @PathVariable String flightNumber,
            @PathVariable String seatNumber) {
        try {
            Seat seat = seatService.findSeatByFlightNumberAndSeatNumber(flightNumber, seatNumber);
            if (seat == null) {
                return HttpResponse.error("座位不存在: 航班ID=" + flightNumber + ", 座位号=" + seatNumber);
            }
            return HttpResponse.success(seat);
        } catch (Exception e) {
            return HttpResponse.error("查询特定座位失败: " + e.getMessage());
        }
    }

    /**
     * 获取航班座位统计信息
     */
    @GetMapping("/flight/{FlightNumber}/statistics")
    public HttpResponse<SeatQueryResultDTO> getFlightSeatStatistics(@PathVariable String flightNumber) {
        try {
            SeatQueryDTO queryDTO = new SeatQueryDTO();
            queryDTO.setFlightNumber(flightNumber);
            queryDTO.setEnablePaging(false); // 统计不需要分页
            SeatQueryResultDTO result = seatService.querySeats(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("获取航班座位统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据查询条件查询座位
     */
    @GetMapping
    public HttpResponse<SeatQueryResultDTO> querySeats(@Valid SeatQueryRequest request) {
        try {
            if (request.getFlightNumber() == null || request.getFlightNumber().trim().isEmpty()) {
                return HttpResponse.error("必须提供航班号");
            }
            
            SeatQueryDTO queryDTO = SeatConvertor.convertToDTO(request);
            SeatQueryResultDTO result = seatService.querySeats(queryDTO);
            
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("查询座位失败: " + e.getMessage());
        }
    }
}
