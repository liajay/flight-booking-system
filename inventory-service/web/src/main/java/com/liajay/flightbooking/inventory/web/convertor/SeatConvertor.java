package com.liajay.flightbooking.inventory.web.convertor;

import com.liajay.flightbooking.inventory.service.dto.SeatQueryDTO;
import com.liajay.flightbooking.inventory.web.request.SeatQueryRequest;

/**
 * 座位转换器
 */
public class SeatConvertor {

    public static SeatQueryDTO convertToDTO(SeatQueryRequest request) {
        SeatQueryDTO dto = new SeatQueryDTO();
        dto.setFlightId(request.getFlightId());
        dto.setFlightNumber(request.getFlightNumber());
        dto.setSeatClass(request.getSeatClass());
        dto.setIsAvailable(request.getIsAvailable());
        dto.setPage(request.getPage());
        dto.setSize(request.getSize());
        dto.setSortBy(request.getSortBy());
        dto.setSortDirection(request.getSortDirection());
        return dto;
    }
}
