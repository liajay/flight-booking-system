package com.liajay.flightbooking.inventory.service.dto.result;

import com.liajay.flightbooking.inventory.model.vo.SeatVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 座位查询结果DTO
 */
public class SeatQueryResultDTO {
    private Page<SeatVO> seats;
    private List<SeatVO> seatList;
    private Long totalElements;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;

    // 座位统计信息
    private Long totalSeats;
    private Long availableSeats;
    private Long occupiedSeats;

    public SeatQueryResultDTO() {}

    public SeatQueryResultDTO(Page<SeatVO> seats) {
        this.seats = seats;
        this.seatList = seats.getContent();
        this.totalElements = seats.getTotalElements();
        this.totalPages = seats.getTotalPages();
        this.hasNext = seats.hasNext();
        this.hasPrevious = seats.hasPrevious();
    }

    public SeatQueryResultDTO(List<SeatVO> seatList) {
        this.seatList = seatList;
        this.totalElements = (long) seatList.size();
        this.totalPages = 1;
        this.hasNext = false;
        this.hasPrevious = false;
    }

    // Getter and Setter methods
    public Page<SeatVO> getSeats() {
        return seats;
    }

    public void setSeats(Page<SeatVO> seats) {
        this.seats = seats;
    }

    public List<SeatVO> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<SeatVO> seatList) {
        this.seatList = seatList;
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

    public Long getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Long totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Long getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Long availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Long getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(Long occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }
}
