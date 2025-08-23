package com.liajay.flightbooking.inventory.model.vo;

import java.math.BigDecimal;

/**
 * 座位视图对象
 */
public class SeatVO {
    private Long id;
    private String flightId;
    private String seatNumber;
    private String seatClass;
    private String seatClassDescription;
    private Boolean isAvailable;
    private BigDecimal price;

    // 关联的航班信息
    private String flightNumber;
    private String airline;

    public SeatVO() {}

    public SeatVO(Long id, String flightId, String seatNumber, String seatClass, String seatClassDescription,
                  Boolean isAvailable, BigDecimal price) {
        this.id = id;
        this.flightId = flightId;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.seatClassDescription = seatClassDescription;
        this.isAvailable = isAvailable;
        this.price = price;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public String getSeatClassDescription() {
        return seatClassDescription;
    }

    public void setSeatClassDescription(String seatClassDescription) {
        this.seatClassDescription = seatClassDescription;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

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
}
