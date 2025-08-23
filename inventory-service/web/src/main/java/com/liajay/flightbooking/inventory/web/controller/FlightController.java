package com.liajay.flightbooking.inventory.web.controller;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.service.FlightService;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;
import com.liajay.flightbooking.inventory.web.convertor.FlightConvertor;
import com.liajay.flightbooking.inventory.web.request.FlightQueryRequest;
import com.liajay.flightbooking.inventory.web.response.HttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 航班控制器
 * Web层 - 处理HTTP请求和响应
 */
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * 根据航班号查询航班详情
     */
    @GetMapping("/{flightNumber}")
    public HttpResponse<Flight> getFlightByNumber(@PathVariable String flightNumber) {
        try {
            Flight flight = flightService.findByFlightNumber(flightNumber);
            if (flight == null) {
                return HttpResponse.error("航班不存在: " + flightNumber);
            }
            return HttpResponse.success(flight);
        } catch (Exception e) {
            return HttpResponse.error("查询航班失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询航班列表
     */
    @GetMapping
    public HttpResponse<FlightQueryResultDTO> queryFlights(@Valid FlightQueryRequest request) {
        try {
            FlightQueryDTO queryDTO = FlightConvertor.convertToDTO(request);
            FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("查询航班列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据出发地和目的地查询航班
     */
    @GetMapping("/route")
    public HttpResponse<FlightQueryResultDTO> queryFlightsByRoute(
            @RequestParam String departureCity,
            @RequestParam String arrivalCity,
            @Valid FlightQueryRequest request) {
        try {
            FlightQueryDTO queryDTO = FlightConvertor.convertToDTO(request);
            // 设置路线查询条件
            queryDTO.setDepartureCity(departureCity);
            queryDTO.setArrivalCity(arrivalCity);
            FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("根据路线查询航班失败: " + e.getMessage());
        }
    }

    /**
     * 根据航空公司查询航班
     */
    @GetMapping("/airline/{airline}")
    public HttpResponse<FlightQueryResultDTO> queryFlightsByAirline(
            @PathVariable String airline,
            @Valid FlightQueryRequest request) {
        try {
            FlightQueryDTO queryDTO = FlightConvertor.convertToDTO(request);
            // 设置航空公司查询条件
            queryDTO.setAirline(airline);
            FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("根据航空公司查询航班失败: " + e.getMessage());
        }
    }

    /**
     * 根据日期范围查询航班
     */
    @GetMapping("/date-range")
    public HttpResponse<FlightQueryResultDTO> queryFlightsByDateRange(@Valid FlightQueryRequest request) {
        try {
            if (request.getStartTime() == null || request.getEndTime() == null) {
                return HttpResponse.error("开始时间和结束时间不能为空");
            }
            FlightQueryDTO queryDTO = FlightConvertor.convertToDTO(request);
            // 日期范围条件已在 convertToDTO 中设置
            FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("根据日期范围查询航班失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有有效航班
     */
    @GetMapping("/active")
    public HttpResponse<FlightQueryResultDTO> queryActiveFlights(@Valid FlightQueryRequest request) {
        try {
            FlightQueryDTO queryDTO = FlightConvertor.convertToDTO(request);
            // 设置查询有效航班的状态条件
            queryDTO.setStatus("SCHEDULED");
            FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("查询有效航班失败: " + e.getMessage());
        }
    }
}
