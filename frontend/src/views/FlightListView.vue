<template>
  <div class="flight-list-container">
    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <h3>🔍 航班搜索</h3>
      <div class="search-form">
        <div class="form-row">
          <div class="form-group">
            <label>出发城市</label>
            <input
              v-model="searchParams.departureCity"
              type="text"
              placeholder="请输入出发城市"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>到达城市</label>
            <input
              v-model="searchParams.arrivalCity"
              type="text"
              placeholder="请输入到达城市"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>航空公司</label>
            <input
              v-model="searchParams.airline"
              type="text"
              placeholder="请输入航空公司"
              class="form-input"
            />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>航班状态</label>
            <select v-model="searchParams.status" class="form-select">
              <option value="">全部状态</option>
              <option value="SCHEDULED">已安排</option>
              <option value="DELAYED">延误</option>
              <option value="CANCELLED">取消</option>
              <option value="DEPARTED">已起飞</option>
              <option value="ARRIVED">已到达</option>
            </select>
          </div>
          <div class="form-group">
            <label>排序方式</label>
            <select v-model="searchParams.sortBy" class="form-select">
              <option value="departureTime">出发时间</option>
              <option value="arrivalTime">到达时间</option>
              <option value="flightNumber">航班号</option>
              <option value="airline">航空公司</option>
              <option value="basePrice">价格</option>
            </select>
          </div>
          <div class="form-group">
            <label>排序方向</label>
            <select v-model="searchParams.sortDirection" class="form-select">
              <option value="ASC">升序</option>
              <option value="DESC">降序</option>
            </select>
          </div>
        </div>
        
        <div class="search-buttons">
          <button @click="searchFlights" class="search-btn" :disabled="loading">
            {{ loading ? '搜索中...' : '搜索航班' }}
          </button>
          <button @click="resetSearch" class="reset-btn">重置</button>
          <button @click="loadActiveFlights" class="active-btn">查看有效航班</button>
        </div>
      </div>
    </div>

    <!-- 航班列表区域 -->
    <div class="flights-section">
      <div class="section-header">
        <h3>✈️ 航班列表</h3>
        <div class="flight-stats" v-if="flightData">
          <span>共找到 {{ flightData.totalElements }} 个航班</span>
          <span>第 {{ flightData.currentPage + 1 }} / {{ flightData.totalPages }} 页</span>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p>正在加载航班信息...</p>
      </div>

      <!-- 错误信息 -->
      <div v-else-if="error" class="error-message">
        <p>{{ error }}</p>
        <button @click="loadFlights" class="retry-btn">重试</button>
      </div>

      <!-- 空状态 -->
      <div v-else-if="!flightData || flightData.flightList.length === 0" class="empty-state">
        <div class="empty-icon">✈️</div>
        <h4>暂无航班信息</h4>
        <p>请尝试调整搜索条件或点击"查看有效航班"</p>
      </div>

      <!-- 航班卡片列表 -->
      <div v-else class="flight-cards">
        <div
          v-for="flight in flightData.flightList"
          :key="flight.id"
          class="flight-card"
          :class="{ 'cancelled': flight.status === 'CANCELLED' }"
        >
          <div class="flight-header">
            <div class="flight-number">
              <span class="flight-code">{{ flight.flightNumber }}</span>
              <span class="airline">{{ flight.airline }}</span>
            </div>
            <div class="flight-status" :class="getStatusClass(flight.status)">
              {{ getStatusText(flight.status) }}
            </div>
          </div>

          <div class="flight-route">
            <div class="departure">
              <div class="city">{{ flight.departureCity }}</div>
              <div class="time">{{ formatDateTime(flight.departureTime) }}</div>
            </div>
            <div class="route-line">
              <div class="airplane">✈️</div>
            </div>
            <div class="arrival">
              <div class="city">{{ flight.arrivalCity }}</div>
              <div class="time">{{ formatDateTime(flight.arrivalTime) }}</div>
            </div>
          </div>

          <div class="flight-details">
            <div class="price">
              <span class="price-label">起价</span>
              <span class="price-value">¥{{ flight.basePrice }}</span>
            </div>
            <div class="seats" v-if="flight.totalSeats !== undefined">
              <span class="seats-label">余票</span>
              <span class="seats-value">{{ flight.availableSeats }}/{{ flight.totalSeats }}</span>
            </div>
            <div class="duration">
              <span class="duration-label">飞行时长</span>
              <span class="duration-value">{{ calculateDuration(flight.departureTime, flight.arrivalTime) }}</span>
            </div>
          </div>

          <div class="flight-actions">
            <button 
              class="book-btn" 
              :disabled="flight.status === 'CANCELLED' || flight.availableSeats === 0"
              @click="bookFlight(flight)"
            >
              {{ flight.status === 'CANCELLED' ? '已取消' : 
                 flight.availableSeats === 0 ? '已售罄' : '预订' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 分页控件 -->
      <div v-if="flightData && flightData.totalPages > 1" class="pagination">
        <button
          @click="goToPage(flightData.currentPage - 1)"
          :disabled="flightData.currentPage === 0"
          class="page-btn"
        >
          上一页
        </button>
        
        <div class="page-numbers">
          <button
            v-for="page in getPageNumbers()"
            :key="page"
            @click="goToPage(page)"
            :class="{ active: page === flightData.currentPage }"
            class="page-number"
          >
            {{ page + 1 }}
          </button>
        </div>
        
        <button
          @click="goToPage(flightData.currentPage + 1)"
          :disabled="flightData.currentPage >= flightData.totalPages - 1"
          class="page-btn"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import flightApi from '../api/flight.js'

export default {
  name: 'FlightList',
  data() {
    return {
      loading: false,
      error: null,
      flightData: null,
      searchParams: {
        departureCity: '',
        arrivalCity: '',
        airline: '',
        status: '',
        page: 0,
        size: 10,
        sortBy: 'departureTime',
        sortDirection: 'ASC'
      }
    }
  },
  mounted() {
    this.loadFlights()
  },
  methods: {
    // 加载航班列表
    async loadFlights() {
      this.loading = true
      this.error = null
      
      try {
        const params = { ...this.searchParams }
        // 移除空值参数
        Object.keys(params).forEach(key => {
          if (params[key] === '' || params[key] == null) {
            delete params[key]
          }
        })
        
        const response = await flightApi.queryFlights(params)
        console.log('Flight API Response:', response)
        
        if (response.data && response.data.success) {
          // 适配新的API返回数据结构
          this.flightData = response.data.data
          console.log('Processed flight data:', this.flightData)
        } else {
          this.error = response.data?.message || '获取航班信息失败'
        }
      } catch (error) {
        console.error('Load flights error:', error)
        this.error = error.response?.data?.message || '网络请求失败，请检查服务是否启动'
      } finally {
        this.loading = false
      }
    },

    // 搜索航班
    async searchFlights() {
      this.searchParams.page = 0 // 重置到第一页
      await this.loadFlights() // 直接使用统一的加载方法
    },

    // 加载有效航班
    async loadActiveFlights() {
      this.loading = true
      this.error = null
      
      try {
        const params = {
          status: 'SCHEDULED', // 查询已安排状态的航班
          page: this.searchParams.page,
          size: this.searchParams.size,
          sortBy: this.searchParams.sortBy,
          sortDirection: this.searchParams.sortDirection
        }
        
        const response = await flightApi.queryFlights(params) // 使用统一的查询接口
        
        if (response.data && response.data.success) {
          // 适配新的API返回数据结构
          this.flightData = response.data.data
        } else {
          this.error = response.data?.message || '获取有效航班失败'
        }
      } catch (error) {
        console.error('Load active flights error:', error)
        this.error = error.response?.data?.message || '获取有效航班失败'
      } finally {
        this.loading = false
      }
    },

    // 重置搜索
    resetSearch() {
      this.searchParams = {
        departureCity: '',
        arrivalCity: '',
        airline: '',
        status: '',
        page: 0,
        size: 10,
        sortBy: 'departureTime',
        sortDirection: 'ASC'
      }
      this.loadFlights()
    },

    // 分页
    goToPage(page) {
      if (page >= 0 && page < this.flightData.totalPages) {
        this.searchParams.page = page
        this.searchFlights()
      }
    },

    // 获取页码数组
    getPageNumbers() {
      const totalPages = this.flightData.totalPages
      const currentPage = this.flightData.currentPage
      const pages = []
      
      const startPage = Math.max(0, currentPage - 2)
      const endPage = Math.min(totalPages - 1, currentPage + 2)
      
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i)
      }
      
      return pages
    },

    // 格式化日期时间
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return '--'
      
      try {
        // 处理 "2025-08-21 16:00:00" 格式
        let date
        if (dateTimeString.includes(' ')) {
          // 将空格替换为T，使其符合ISO格式
          const isoString = dateTimeString.replace(' ', 'T')
          date = new Date(isoString)
        } else {
          date = new Date(dateTimeString)
        }
        
        return date.toLocaleString('zh-CN', {
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
        })
      } catch (error) {
        console.error('Date format error:', error, dateTimeString)
        return dateTimeString // 返回原始字符串
      }
    },

    // 计算飞行时长
    calculateDuration(departureTime, arrivalTime) {
      if (!departureTime || !arrivalTime) return '--'
      
      try {
        let departure, arrival
        
        // 处理 "2025-08-21 16:00:00" 格式
        if (departureTime.includes(' ')) {
          departure = new Date(departureTime.replace(' ', 'T'))
        } else {
          departure = new Date(departureTime)
        }
        
        if (arrivalTime.includes(' ')) {
          arrival = new Date(arrivalTime.replace(' ', 'T'))
        } else {
          arrival = new Date(arrivalTime)
        }
        
        const diffMs = arrival - departure
        const diffHours = Math.floor(diffMs / (1000 * 60 * 60))
        const diffMinutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60))
        
        return `${diffHours}h${diffMinutes}m`
      } catch (error) {
        console.error('Duration calculation error:', error)
        return '--'
      }
    },

    // 获取状态样式类
    getStatusClass(status) {
      const statusClasses = {
        'SCHEDULED': 'status-scheduled',
        'DELAYED': 'status-delayed',
        'CANCELLED': 'status-cancelled',
        'DEPARTED': 'status-departed',
        'ARRIVED': 'status-arrived'
      }
      return statusClasses[status] || 'status-default'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusTexts = {
        'SCHEDULED': '已安排',
        'DELAYED': '延误',
        'CANCELLED': '取消',
        'DEPARTED': '已起飞',
        'ARRIVED': '已到达'
      }
      return statusTexts[status] || status
    },

    // 预订航班
    bookFlight(flight) {
      // 这里可以实现预订逻辑
      alert(`预订航班: ${flight.flightNumber}\n出发: ${flight.departureCity} → ${flight.arrivalCity}\n价格: ¥${flight.basePrice}`)
    }
  }
}
</script>

<style scoped>
.flight-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 搜索区域样式 */
.search-section {
  background: white;
  border-radius: 10px;
  padding: 25px;
  margin-bottom: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.search-section h3 {
  color: #333;
  margin-bottom: 20px;
  font-size: 18px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  color: #555;
  margin-bottom: 5px;
  font-size: 14px;
  font-weight: 500;
}

.form-input,
.form-select {
  padding: 10px 12px;
  border: 2px solid #e1e5e9;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.form-input:focus,
.form-select:focus {
  outline: none;
  border-color: #667eea;
}

.search-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.search-btn,
.reset-btn,
.active-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.search-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.search-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.reset-btn {
  background: #f8f9fa;
  color: #6c757d;
  border: 2px solid #e9ecef;
}

.reset-btn:hover {
  background: #e9ecef;
}

.active-btn {
  background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
  color: white;
}

.active-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(40, 167, 69, 0.4);
}

/* 航班列表区域样式 */
.flights-section {
  background: white;
  border-radius: 10px;
  padding: 25px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  flex-wrap: wrap;
  gap: 15px;
}

.section-header h3 {
  color: #333;
  font-size: 18px;
}

.flight-stats {
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

/* 加载和错误状态 */
.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 15px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  text-align: center;
  padding: 40px;
  color: #dc3545;
}

.retry-btn {
  margin-top: 15px;
  padding: 10px 20px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.empty-state h4 {
  margin-bottom: 10px;
  color: #333;
}

/* 航班卡片样式 */
.flight-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.flight-card {
  border: 2px solid #e9ecef;
  border-radius: 10px;
  padding: 20px;
  transition: all 0.3s ease;
  background: #fff;
}

.flight-card:hover {
  border-color: #667eea;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.1);
}

.flight-card.cancelled {
  opacity: 0.6;
  background: #f8f9fa;
}

.flight-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.flight-number {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.flight-code {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.airline {
  font-size: 14px;
  color: #666;
}

.flight-status {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  text-transform: uppercase;
}

.status-scheduled {
  background: #e7f3ff;
  color: #0066cc;
}

.status-delayed {
  background: #fff3cd;
  color: #856404;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
}

.status-departed {
  background: #d1ecf1;
  color: #0c5460;
}

.status-arrived {
  background: #d4edda;
  color: #155724;
}

.flight-route {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 20px;
  align-items: center;
  margin-bottom: 20px;
  text-align: center;
}

.departure,
.arrival {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.city {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.time {
  font-size: 14px;
  color: #666;
}

.route-line {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.route-line::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 2px;
  background: #e9ecef;
  z-index: 1;
}

.airplane {
  background: white;
  padding: 0 10px;
  font-size: 20px;
  z-index: 2;
  position: relative;
}

.flight-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.price,
.seats,
.duration {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.price-label,
.seats-label,
.duration-label {
  font-size: 12px;
  color: #666;
  text-transform: uppercase;
}

.price-value {
  font-size: 18px;
  font-weight: 600;
  color: #dc3545;
}

.seats-value,
.duration-value {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.flight-actions {
  display: flex;
  justify-content: flex-end;
}

.book-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.book-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.book-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
}

/* 分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 30px;
}

.page-btn,
.page-number {
  padding: 8px 12px;
  border: 2px solid #e9ecef;
  background: white;
  color: #333;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled),
.page-number:hover {
  border-color: #667eea;
  background: #667eea;
  color: white;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-number.active {
  background: #667eea;
  color: white;
  border-color: #667eea;
}

.page-numbers {
  display: flex;
  gap: 5px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .flight-list-container {
    padding: 15px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .search-buttons {
    flex-direction: column;
  }
  
  .search-btn,
  .reset-btn,
  .active-btn {
    width: 100%;
  }
  
  .section-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .flight-stats {
    flex-direction: column;
    gap: 5px;
  }
  
  .flight-route {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .route-line {
    order: 2;
  }
  
  .arrival {
    order: 3;
  }
  
  .flight-details {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .pagination {
    flex-wrap: wrap;
  }
  
  .page-numbers {
    order: 1;
    width: 100%;
    justify-content: center;
    margin-bottom: 10px;
  }
}
</style>
