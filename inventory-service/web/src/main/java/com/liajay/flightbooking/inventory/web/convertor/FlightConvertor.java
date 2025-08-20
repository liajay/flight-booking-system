package com.liajay.flightbooking.inventory.web.convertor;

import com.liajay.flightbooking.inventory.service.dto.FlightQueryDTO;
import com.liajay.flightbooking.inventory.web.request.FlightQueryRequest;

/**
 * 航班转换器
 */
public class FlightConvertor {

    public static FlightQueryDTO convertToDTO(FlightQueryRequest request) {
        FlightQueryDTO dto = new FlightQueryDTO();
        dto.setFlightNumber(request.getFlightNumber());
        dto.setAirline(request.getAirline());
        dto.setDepartureCity(request.getDepartureCity());
        dto.setArrivalCity(request.getArrivalCity());
        dto.setStartTime(request.getStartTime());
        dto.setEndTime(request.getEndTime());
        dto.setStatus(request.getStatus());
        dto.setPage(request.getPage());
        dto.setSize(request.getSize());
        dto.setSortBy(request.getSortBy());
        dto.setSortDirection(request.getSortDirection());
        return dto;
    }
}
