package com.liajay.flightbooking.inventory.web.controller;

import com.liajay.flightbooking.inventory.dal.dataobject.Flight;
import com.liajay.flightbooking.inventory.service.FlightService;
import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.service.dto.result.FlightQueryResultDTO;
import com.liajay.flightbooking.inventory.web.convertor.FlightConvertor;
import com.liajay.flightbooking.inventory.web.request.FlightQueryRequest;
import com.liajay.flightbooking.inventory.web.response.HttpResponse;
import com.liajay.flightbooking.inventory.web.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 航班控制器
 * Web层 - 处理HTTP请求和响应
 */
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }
    
    /**
     * 分页查询航班列表
     */
    @GetMapping
    public HttpResponse<FlightQueryResultDTO> queryFlights(@Valid FlightQueryRequest request) {
        try {
            // 手动解码URL编码的参数
            decodeRequestParameters(request);
            
            // 添加调试日志
            logger.info("收到航班查询请求: departureCity={}, arrivalCity={}, airline={}, status={}", 
                       request.getDepartureCity(), request.getArrivalCity(), request.getAirline(), request.getStatus());
            
            FlightQueryDTO queryDTO = FlightConvertor.convertToDTO(request);
            FlightQueryResultDTO result = flightService.queryFlights(queryDTO);
            return HttpResponse.success(result);
        } catch (Exception e) {
            logger.error("查询航班列表失败", e);
            return HttpResponse.error("查询航班列表失败: " + e.getMessage());
        }
    }

    /**
     * 手动解码请求参数中的URL编码
     */
    private void decodeRequestParameters(FlightQueryRequest request) {
        // 使用安全解码，只有当参数看起来像URL编码时才解码
        request.setDepartureCity(UrlUtils.safeDecodeIfNeeded(request.getDepartureCity()));
        request.setArrivalCity(UrlUtils.safeDecodeIfNeeded(request.getArrivalCity()));
        request.setAirline(UrlUtils.safeDecodeIfNeeded(request.getAirline()));
        request.setFlightNumber(UrlUtils.safeDecodeIfNeeded(request.getFlightNumber()));
        
        logger.debug("解码后的参数: departureCity={}, arrivalCity={}, airline={}, flightNumber={}", 
                    request.getDepartureCity(), request.getArrivalCity(), 
                    request.getAirline(), request.getFlightNumber());
    }

}
