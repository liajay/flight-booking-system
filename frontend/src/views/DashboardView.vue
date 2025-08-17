<template>
  <div class="dashboard-container">
    <header class="header">
      <div class="header-content">
        <h1>航班订票系统</h1>
        <div class="user-info">
          <span>欢迎，{{ userInfo.username || '用户' }}</span>
          <button @click="logout" class="logout-btn">退出登录</button>
        </div>
      </div>
    </header>
    
    <main class="main-content">
      <div class="welcome-section">
        <h2>欢迎使用航班订票系统</h2>
        <p>您已成功登录系统</p>
      </div>
      
      <div class="feature-cards">
        <div class="card">
          <h3>🛫 航班查询</h3>
          <p>查看可用航班信息</p>
          <button class="card-btn">即将上线</button>
        </div>
        
        <div class="card">
          <h3>🎫 我的订单</h3>
          <p>管理您的订票记录</p>
          <button class="card-btn">即将上线</button>
        </div>
        
        <div class="card">
          <h3>👤 个人中心</h3>
          <p>管理个人信息</p>
          <button class="card-btn" @click="showUserInfo">查看信息</button>
        </div>
      </div>
      
      <!-- 用户信息模态框 -->
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
          <h3>用户信息</h3>
          <div class="user-details">
            <p><strong>用户名:</strong> {{ userInfo.username || '未知' }}</p>
            <p><strong>登录状态:</strong> 已登录</p>
            <p><strong>Token:</strong> {{ tokenStatus }}</p>
          </div>
          <button @click="closeModal" class="close-btn">关闭</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { storage } from '../utils/index.js'

export default {
  name: 'DashboardView',
  data() {
    return {
      userInfo: {},
      showModal: false
    }
  },
  computed: {
    tokenStatus() {
      const token = storage.getToken()
      return token ? '有效' : '无效'
    }
  },
  mounted() {
    this.userInfo = storage.getUser() || {}
  },
  methods: {
    // 退出登录
    logout() {
      if (confirm('确定要退出登录吗？')) {
        storage.clear()
        this.$router.push('/login')
      }
    },
    
    // 显示用户信息
    showUserInfo() {
      this.showModal = true
    },
    
    // 关闭模态框
    closeModal() {
      this.showModal = false
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '未知'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info span {
  font-size: 16px;
}

.logout-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.welcome-section {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-section h2 {
  color: #333;
  margin-bottom: 10px;
  font-size: 28px;
  font-weight: 600;
}

.welcome-section p {
  color: #666;
  font-size: 16px;
}

.feature-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.card {
  background: white;
  border-radius: 10px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.card h3 {
  color: #333;
  margin-bottom: 15px;
  font-size: 20px;
}

.card p {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.6;
}

.card-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: transform 0.2s ease;
}

.card-btn:hover {
  transform: translateY(-1px);
}

.card-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 10px;
  padding: 30px;
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content h3 {
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.user-details {
  margin-bottom: 20px;
}

.user-details p {
  margin-bottom: 10px;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.user-details p:last-child {
  border-bottom: none;
}

.close-btn {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
}

.close-btn:hover {
  opacity: 0.9;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
  
  .feature-cards {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .main-content {
    padding: 20px 15px;
  }
}
</style>
