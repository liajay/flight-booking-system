package com.liajay.flightbooking.inventory.dal.dataobject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 座位实体类
 * DAL层 - 数据访问对象
 */
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_class", nullable = false, length = 20)
    private SeatClass seatClass;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // 无参构造函数
    public Seat() {}

    // 构造函数
    public Seat(String flightId, String seatNumber, SeatClass seatClass, BigDecimal price) {
        this.flightNumber = flightId;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.price = price;
        this.isAvailable = true;
    }

    @PrePersist
    protected void onCreate() {
        if (isAvailable == null) {
            isAvailable = true;
        }
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(id, seat.id) &&
               Objects.equals(flightNumber, seat.flightNumber) &&
               Objects.equals(seatNumber, seat.seatNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightNumber, seatNumber);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", flightNumber=" + flightNumber +
                ", seatNumber='" + seatNumber + '\'' +
                ", seatClass=" + seatClass +
                ", isAvailable=" + isAvailable +
                ", price=" + price +
                '}';
    }
}
