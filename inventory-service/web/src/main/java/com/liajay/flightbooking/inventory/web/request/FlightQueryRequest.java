package com.liajay.flightbooking.inventory.web.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 航班查询请求
 */
public class FlightQueryRequest {
    
    private String flightNumber;
    
    private String airline;
    
    private String departureCity;
    
    private String arrivalCity;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @Pattern(regexp = "SCHEDULED|DELAYED|CANCELLED|DEPARTED|ARRIVED", 
             message = "航班状态必须是 SCHEDULED, DELAYED, CANCELLED, DEPARTED, ARRIVED 之一")
    private String status = "SCHEDULED";
    
    @Min(value = 0, message = "页码不能小于0")
    private Integer page = 0;
    
    @Min(value = 1, message = "每页大小不能小于1")
    private Integer size = 20;
    
    @Pattern(regexp = "departureTime|arrivalTime|flightNumber|airline|basePrice", 
             message = "排序字段必须是 departureTime, arrivalTime, flightNumber, airline, basePrice 之一")
    private String sortBy = "departureTime";
    
    @Pattern(regexp = "ASC|DESC", message = "排序方向必须是 ASC 或 DESC")
    private String sortDirection = "ASC";

    public FlightQueryRequest() {}

    // Getter and Setter methods
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
