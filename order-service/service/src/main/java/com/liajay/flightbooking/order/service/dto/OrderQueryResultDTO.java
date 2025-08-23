package com.liajay.flightbooking.order.service.dto;

import com.liajay.flightbooking.order.model.vo.OrderVO;

import java.util.List;

/**
 * 订单查询结果DTO
 * 
 * @author liajay
 */
public class OrderQueryResultDTO {
    
    /**
     * 订单列表
     */
    private List<OrderVO> orders;
    
    /**
     * 总数
     */
    private long total;
    
    /**
     * 当前页
     */
    private int currentPage;
    
    /**
     * 页大小
     */
    private int pageSize;
    
    /**
     * 总页数
     */
    private int totalPages;

    // 构造函数
    public OrderQueryResultDTO() {
    }

    public OrderQueryResultDTO(List<OrderVO> orders, long total, int currentPage, int pageSize) {
        this.orders = orders;
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) total / pageSize) : 0;
    }

    // 静态工厂方法
    public static OrderQueryResultDTO fromList(List<OrderVO> orders) {
        OrderQueryResultDTO result = new OrderQueryResultDTO();
        result.setOrders(orders);
        result.setTotal(orders.size());
        result.setCurrentPage(1);
        result.setPageSize(orders.size());
        result.setTotalPages(1);
        return result;
    }

    // Getter和Setter方法
    public List<OrderVO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderVO> orders) {
        this.orders = orders;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
