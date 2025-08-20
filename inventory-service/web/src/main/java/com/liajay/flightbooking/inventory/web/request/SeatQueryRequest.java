package com.liajay.flightbooking.inventory.web.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 座位查询请求
 */
public class SeatQueryRequest {
    
    private Long flightId;
    
    private String flightNumber;
    
    @Pattern(regexp = "ECONOMY|BUSINESS|FIRST", 
             message = "座位舱位等级必须是 ECONOMY, BUSINESS, FIRST 之一")
    private String seatClass;
    
    private Boolean isAvailable;
    
    @Min(value = 0, message = "页码不能小于0")
    private Integer page = 0;
    
    @Min(value = 1, message = "每页大小不能小于1")
    private Integer size = 50;
    
    @Pattern(regexp = "seatNumber|seatClass|price|isAvailable", 
             message = "排序字段必须是 seatNumber, seatClass, price, isAvailable 之一")
    private String sortBy = "seatNumber";
    
    @Pattern(regexp = "ASC|DESC", message = "排序方向必须是 ASC 或 DESC")
    private String sortDirection = "ASC";

    public SeatQueryRequest() {}

    // Getter and Setter methods
    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
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
