package com.liajay.flightbooking.inventory.service.dto.result;

import com.liajay.flightbooking.inventory.model.vo.FlightVO;
import org.springframework.data.domain.Page;

/**
 * 航班查询结果DTO
 */
public class FlightQueryResultDTO {
    private Page<FlightVO> flights;
    private Long totalElements;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;

    public FlightQueryResultDTO() {}

    public FlightQueryResultDTO(Page<FlightVO> flights) {
        this.flights = flights;
        this.totalElements = flights.getTotalElements();
        this.totalPages = flights.getTotalPages();
        this.hasNext = flights.hasNext();
        this.hasPrevious = flights.hasPrevious();
    }

    // Getter and Setter methods
    public Page<FlightVO> getFlights() {
        return flights;
    }

    public void setFlights(Page<FlightVO> flights) {
        this.flights = flights;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}
