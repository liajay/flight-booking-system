package com.liajay.flightbooking.order.web.controller;

import com.liajay.flightbooking.order.model.vo.OrderVO;
import com.liajay.flightbooking.order.service.OrderService;
import com.liajay.flightbooking.order.service.dto.CreateOrderDTO;
import com.liajay.flightbooking.order.service.dto.OrderQueryResultDTO;
import com.liajay.flightbooking.order.util.UserContextUtil;
import com.liajay.flightbooking.order.web.request.CreateOrderRequest;
import com.liajay.flightbooking.order.web.response.HttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 订单控制器
 * 
 * @author liajay
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;

    private final UserContextUtil userContextUtil;

    public OrderController(OrderService orderService, UserContextUtil userContextUtil) {
        this.orderService = orderService;
        this.userContextUtil = userContextUtil;
    }

    /**
     * 创建订单
     * 
     * @param request 创建订单请求
     * @return 创建结果
     */
    @PostMapping
    public HttpResponse<OrderVO> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            CreateOrderDTO createOrderDTO = new CreateOrderDTO(
                    request.getUserId(),
                    request.getFlightNumber(),
                    request.getSeatNumber(),
                    request.getAmount()
            );
            
            OrderVO orderVO = orderService.createOrder(createOrderDTO);
            return HttpResponse.success(orderVO);
        } catch (Exception e) {
            return HttpResponse.error("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 根据订单编号查询订单
     * 
     * @param orderNumber 订单编号
     * @return 订单信息
     */
    @GetMapping("/{orderNumber}")
    public HttpResponse<OrderVO> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            OrderVO orderVO = orderService.getOrderByNumber(orderNumber);
            if (orderVO == null) {
                return HttpResponse.error("订单不存在");
            }
            return HttpResponse.success(orderVO);
        } catch (Exception e) {
            return HttpResponse.error("查询订单失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID分页查询订单
     *
     * @param page 页码（从0开始，默认0）
     * @param size 页大小（默认10）
     * @return 订单列表
     */
    @GetMapping("/user")
    public HttpResponse<OrderQueryResultDTO> getOrdersByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Long userId = UserContextUtil.getCurrentUserId();
            OrderQueryResultDTO result = orderService.getOrdersByUserId(userId, page, size);
            return HttpResponse.success(result);
        } catch (Exception e) {
            return HttpResponse.error("查询订单列表失败：" + e.getMessage());
        }
    }
}
