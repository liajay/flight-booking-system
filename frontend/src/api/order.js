import api from './request.js';

// 获取当前用户订单列表
// page: 页码（从0开始，后端需要）
// size: 页大小
export function fetchUserOrders(page = 0, size = 10) {
  return api.get(`/orders/user`, { params: { page, size } });
}

// 获取订单详情
export function fetchOrderDetail(orderNumber) {
  return api.get(`/orders/${orderNumber}`);
}
