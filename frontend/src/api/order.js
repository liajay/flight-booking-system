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

// 创建订单并自动分配座位
export function createOrderWithSeatAllocation(data) {
  console.log('=== 发送订单请求 ===')
  console.log('请求URL:', '/orders/with-seat-allocation')
  console.log('请求数据:', JSON.stringify(data, null, 2))
  console.log('请求大小:', JSON.stringify(data).length, '字节')
  
  return api.post('/orders/with-seat-allocation', data)
    .then(response => {
      console.log('=== API响应成功 ===')
      console.log('响应状态:', response.status)
      console.log('响应数据:', response.data)
      return response
    })
    .catch(error => {
      console.error('=== API响应失败 ===')
      console.error('错误状态:', error.response?.status)
      console.error('错误信息:', error.response?.data)
      console.error('完整错误:', error)
      throw error
    })
}

// 默认导出
export default {
  fetchUserOrders,
  fetchOrderDetail,
  createOrderWithSeatAllocation
};
