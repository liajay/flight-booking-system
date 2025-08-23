package com.liajay.flightbooking.inventory.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 航班查询DTO - MyBatis增强版
 * 支持更丰富的查询条件和分页功能
 */
public class FlightQueryDTO {
    private String flightNumber;
    private String airline;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "departureTime";
    private String sortDirection = "ASC";

    private Boolean enablePaging = true;

    public FlightQueryDTO() {}

    public FlightQueryDTO(String departureCity, String arrivalCity, Boolean isRoute) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
    }

    public FlightQueryDTO(String airline) {
        this.airline = airline;
    }

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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
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

    public Boolean getEnablePaging() {
        return enablePaging;
    }

    public void setEnablePaging(Boolean enablePaging) {
        this.enablePaging = enablePaging;
    }

    /**
     * 判断是否需要分页
     */
    public boolean isPaginationEnabled() {
        return enablePaging != null && enablePaging && page != null && size != null && size > 0;
    }

    /**
     * 获取排序字段的数据库列名
     */
    public String getSortColumn() {
        if (sortBy == null) {
            return "departure_time";
        }
        switch (sortBy) {
            case "id":
                return "id";
            case "flightNumber":
                return "flight_number";
            case "airline":
                return "airline";
            case "departureCity":
                return "departure_city";
            case "arrivalCity":
                return "arrival_city";
            case "departureTime":
                return "departure_time";
            case "arrivalTime":
                return "arrival_time";
            case "basePrice":
                return "base_price";
            case "status":
                return "status";
            default:
                return "departure_time";
        }
    }

    @Override
    public String toString() {
        return "FlightQueryDTO{" +
                "flightNumber='" + flightNumber + '\'' +
                ", airline='" + airline + '\'' +
                ", departureCity='" + departureCity + '\'' +
                ", arrivalCity='" + arrivalCity + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", enablePaging=" + enablePaging +
                '}';
    }
}
