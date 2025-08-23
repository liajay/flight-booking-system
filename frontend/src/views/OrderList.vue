<template>
  <div class="order-list">
    <h2>我的订单</h2>
    <div v-if="loading">加载中...</div>
    <div v-else>
      <table v-if="orders.length">
        <thead>
          <tr>
            <th>订单编号</th>
            <th>航班号</th>
            <th>座位号</th>
            <th>金额</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.orderNumber">
            <td>{{ order.orderNumber }}</td>
            <td>{{ order.flightNumber }}</td>
            <td>{{ order.seatNumber }}</td>
            <td>¥{{ order.amount }}</td>
          </tr>
        </tbody>
      </table>
      <div v-else>暂无订单</div>
      <div class="pagination">
        <button :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">上一页</button>
        <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
        <button :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)">下一页</button>
      </div>
    </div>
  </div>
</template>

<script>
import { fetchUserOrders } from '../api/order';

export default {
  name: 'OrderList',
  data() {
    return {
      orders: [],
      currentPage: 1,
      pageSize: 10,
      totalPages: 1,
      total: 0,
      loading: false,
    };
  },
  mounted() {
    this.loadOrders();
  },
  methods: {
    async loadOrders() {
      this.loading = true;
      try {
        // 注意：后端分页从1开始，前端显示也从1开始
        const res = await fetchUserOrders(this.currentPage - 1, this.pageSize);
        if (res.data.success) {
          const data = res.data.data;
          this.orders = data.orders || [];
          this.total = data.total || 0;
          this.currentPage = data.currentPage || 1;
          this.pageSize = data.pageSize || 10;
          this.totalPages = data.totalPages || 1;
        } else {
          alert(res.data.message || '获取订单失败');
        }
      } catch (e) {
        console.error('获取订单失败:', e);
        alert('网络错误');
      }
      this.loading = false;
    },
    changePage(newPage) {
      if (newPage >= 1 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.loadOrders();
      }
    },
  },
};
</script>

<style scoped>
.order-list {
  max-width: 800px;
  margin: 0 auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
}
th, td {
  border: 1px solid #ddd;
  padding: 8px;
}
.pagination {
  margin-bottom: 16px;
}
</style>
