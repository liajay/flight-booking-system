package com.liajay.flightbooking.inventory.service.dto.result;

import com.liajay.flightbooking.inventory.model.vo.FlightVO;
import com.liajay.flightbooking.inventory.service.dto.PageResult;

import java.util.List;

/**
 * 航班查询结果DTO - MyBatis版本
 */
public class FlightQueryResultDTO {
    private List<FlightVO> flightList;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean isFirst;
    private Boolean isLast;

    public FlightQueryResultDTO() {}

    /**
     * 基于PageResult构造
     */
    public FlightQueryResultDTO(PageResult<FlightVO> pageResult) {
        this.flightList = pageResult.getList();
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
    public FlightQueryResultDTO(List<FlightVO> flightList) {
        this.flightList = flightList;
        this.totalElements = (long) flightList.size();
        this.totalPages = 1;
        this.currentPage = 0;
        this.pageSize = flightList.size();
        this.hasNext = false;
        this.hasPrevious = false;
        this.isFirst = true;
        this.isLast = true;
    }

    /**
     * 从PageResult创建结果
     */
    public static FlightQueryResultDTO fromPageResult(PageResult<FlightVO> pageResult) {
        return new FlightQueryResultDTO(pageResult);
    }

    /**
     * 从普通List创建结果（非分页）
     */
    public static FlightQueryResultDTO fromList(List<FlightVO> flights) {
        FlightQueryResultDTO result = new FlightQueryResultDTO();
        result.setFlightList(flights);
        result.setTotalElements((long) flights.size());
        result.setTotalPages(1);
        result.setCurrentPage(0);
        result.setPageSize(flights.size());
        result.setHasNext(false);
        result.setHasPrevious(false);
        result.setIsFirst(true);
        result.setIsLast(true);
        return result;
    }

    // Getter and Setter methods
    public List<FlightVO> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<FlightVO> flightList) {
        this.flightList = flightList;
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
}
