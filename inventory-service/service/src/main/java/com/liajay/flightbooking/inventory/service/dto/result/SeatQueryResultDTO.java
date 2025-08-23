package com.liajay.flightbooking.inventory.service.dto.result;

import com.liajay.flightbooking.inventory.model.vo.SeatVO;
import com.liajay.flightbooking.inventory.service.dto.PageResult;

import java.util.List;

/**
 * 座位查询结果DTO - MyBatis版本
 */
public class SeatQueryResultDTO {
    // 座位列表
    private List<SeatVO> seatList;
    
    // 分页信息
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean isFirst;
    private Boolean isLast;

    // 座位统计信息
    private Long totalSeats;
    private Long availableSeats;
    private Long occupiedSeats;

    public SeatQueryResultDTO() {}

    /**
     * 基于PageResult构造
     */
    public SeatQueryResultDTO(PageResult<SeatVO> pageResult) {
        this.seatList = pageResult.getList();
        this.totalElements = pageResult.getTotal();
        this.totalPages = pageResult.getPages();
        this.currentPage = pageResult.getPageNum() - 1; // PageResult从1开始，转换为从0开始
        this.pageSize = pageResult.getPageSize();
        this.hasNext = pageResult.isHasNextPage();
        this.hasPrevious = pageResult.isHasPreviousPage();
        this.isFirst = pageResult.isIsFirstPage();
        this.isLast = pageResult.isIsLastPage();
    }

    /**
     * 基于列表构造（非分页）
     */
    public SeatQueryResultDTO(List<SeatVO> seatList) {
        this.seatList = seatList;
        this.totalElements = (long) seatList.size();
        this.totalPages = 1;
        this.currentPage = 0;
        this.pageSize = seatList.size();
        this.hasNext = false;
        this.hasPrevious = false;
        this.isFirst = true;
        this.isLast = true;
    }

    /**
     * 创建分页结果的静态工厂方法
     */
    public static SeatQueryResultDTO fromPageResult(PageResult<SeatVO> pageResult) {
        return new SeatQueryResultDTO(pageResult);
    }

    /**
     * 创建非分页结果的静态工厂方法
     */
    public static SeatQueryResultDTO fromList(List<SeatVO> seatList) {
        return new SeatQueryResultDTO(seatList);
    }

    // Getter and Setter methods
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

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean first) {
        isFirst = first;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean last) {
        isLast = last;
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
