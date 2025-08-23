package com.liajay.flightbooking.inventory.service.dto;

import java.math.BigDecimal;

/**
 * 座位查询DTO - MyBatis增强版
 * 支持更丰富的查询条件和分页功能
 */
public class SeatQueryDTO {
    private String flightNumber;
    private String seatClass;
    private Boolean isAvailable;
    
    // 价格范围查询
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    // 座位号范围查询（可选）
    private String seatNumberStart;
    private String seatNumberEnd;

    // 分页参数
    private Integer page = 0;
    private Integer size = 50;
    private String sortBy = "seatNumber";
    private String sortDirection = "ASC";
    
    // 是否启用分页
    private Boolean enablePaging = true;

    public SeatQueryDTO() {}

    public SeatQueryDTO(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public SeatQueryDTO(String flightNumber, Boolean isAvailable) {
        this.flightNumber = flightNumber;
        this.isAvailable = isAvailable;
    }

    public SeatQueryDTO(String flightNumber, String seatClass, Boolean isAvailable) {
        this.flightNumber = flightNumber;
        this.seatClass = seatClass;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
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

    public String getSeatNumberStart() {
        return seatNumberStart;
    }

    public void setSeatNumberStart(String seatNumberStart) {
        this.seatNumberStart = seatNumberStart;
    }

    public String getSeatNumberEnd() {
        return seatNumberEnd;
    }

    public void setSeatNumberEnd(String seatNumberEnd) {
        this.seatNumberEnd = seatNumberEnd;
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
            return "seat_number";
        }
        switch (sortBy) {
            case "id":
                return "id";
            case "flightNumber":
                return "flight_number";
            case "seatNumber":
                return "seat_number";
            case "seatClass":
                return "seat_class";
            case "isAvailable":
                return "is_available";
            case "price":
                return "price";
            default:
                return "seat_number";
        }
    }

    @Override
    public String toString() {
        return "SeatQueryDTO{" +
                "flightNumber='" + flightNumber + '\'' +
                ", seatClass='" + seatClass + '\'' +
                ", isAvailable=" + isAvailable +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", seatNumberStart='" + seatNumberStart + '\'' +
                ", seatNumberEnd='" + seatNumberEnd + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", enablePaging=" + enablePaging +
                '}';
    }
}
