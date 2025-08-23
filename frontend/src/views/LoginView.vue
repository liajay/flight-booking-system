<template>
  <div class="login-container">
    <div class="login-box">
      <div class="logo">
        <h1>航班订票系统</h1>
      </div>
      
      <!-- 登录表单 -->
      <div v-if="!isRegisterMode" class="form-container">
        <h2>登录</h2>
        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <input
              v-model="loginForm.username"
              type="text"
              placeholder="用户名"
              required
              class="form-input"
            />
          </div>
          <div class="form-group">
            <input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              required
              class="form-input"
            />
          </div>
          <button type="submit" class="btn-primary" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        
        <div class="form-footer">
          <p>还没有账号？ 
            <a @click="switchToRegister" class="link">立即注册</a>
          </p>
        </div>
      </div>
      
      <!-- 注册表单 -->
      <div v-else class="form-container">
        <h2>注册</h2>
        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <input
              v-model="registerForm.username"
              type="text"
              placeholder="用户名"
              required
              class="form-input"
            />
          </div>
          <div class="form-group">
            <input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              required
              class="form-input"
            />
          </div>
          <button type="submit" class="btn-primary" :disabled="loading">
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>
        
        <div class="form-footer">
          <p>已有账号？ 
            <a @click="switchToLogin" class="link">立即登录</a>
          </p>
        </div>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      
      <!-- 成功提示 -->
      <div v-if="successMessage" class="success-message">
        {{ successMessage }}
      </div>
    </div>
  </div>
</template>

<script>
import { userApi } from '../api/user.js'
import { storage } from '../utils/index.js'

export default {
  name: 'LoginView',
  data() {
    return {
      isRegisterMode: false,
      loading: false,
      errorMessage: '',
      successMessage: '',
      loginForm: {
        username: '',
        password: ''
      },
      registerForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    // 切换到注册模式
    switchToRegister() {
      this.isRegisterMode = true
      this.clearMessages()
      this.resetForms()
    },
    
    // 切换到登录模式
    switchToLogin() {
      this.isRegisterMode = false
      this.clearMessages()
      this.resetForms()
    },
    
    // 清除消息
    clearMessages() {
      this.errorMessage = ''
      this.successMessage = ''
    },
    
    // 重置表单
    resetForms() {
      this.loginForm = { username: '', password: '' }
      this.registerForm = { username: '', password: '' }
    },
    
    // 处理登录
    async handleLogin() {
      this.loading = true
      this.clearMessages()
      
      try {
        const response = await userApi.login({
          username: this.loginForm.username,
          password: this.loginForm.password
        })
        
        console.log('登录响应:', response.data) // 添加调试日志
        
        if (response.data.success) {
          this.successMessage = '登录成功！'
          // 保存JWT token（后端只返回token）
          storage.setToken(response.data.data.token)
          // 保存用户名到本地存储
          storage.setUser({ username: this.loginForm.username })
          
          // 跳转到主页面
          setTimeout(() => {
            this.$router.push('/dashboard')
          }, 1000)
        } else {
          // 登录失败，显示错误消息
          this.errorMessage = response.data.message || '登录失败'
          console.log('设置错误消息:', this.errorMessage) // 添加调试日志
          
          // 确保错误消息持续显示5秒
          setTimeout(() => {
            this.errorMessage = ''
          }, 5000)
        }
      } catch (error) {
        console.error('登录错误:', error)
        
        // 更详细的错误处理
        if (error.response) {
          // 服务器返回了错误状态码
          this.errorMessage = error.response.data?.message || `服务器错误: ${error.response.status}`
        } else if (error.request) {
          // 请求发出了但没有收到响应
          this.errorMessage = '网络连接失败，请检查网络'
        } else {
          // 其他错误
          this.errorMessage = '请求配置错误'
        }
        
        // 确保错误消息持续显示5秒
        setTimeout(() => {
          this.errorMessage = ''
        }, 5000)
      } finally {
        this.loading = false
      }
    },
    
    // 处理注册
    async handleRegister() {
      this.loading = true
      this.clearMessages()
      
      try {
        const response = await userApi.register({
          username: this.registerForm.username,
          password: this.registerForm.password
        })
        
        if (response.data.success && response.data.data.success) {
          this.successMessage = '注册成功！请登录'
          setTimeout(() => {
            this.switchToLogin()
          }, 1500)
        } else {
          this.errorMessage = response.data.message || '注册失败'
        }
      } catch (error) {
        console.error('注册错误:', error)
        this.errorMessage = error.response?.data?.message || '网络错误，请稍后重试'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
  margin: 20px;
}

.logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo h1 {
  color: #333;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.form-container h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-weight: 500;
}

.form-group {
  margin-bottom: 20px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 6px;
  font-size: 16px;
  transition: border-color 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: #667eea;
}

.form-input::placeholder {
  color: #999;
}

.btn-primary {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-1px);
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
}

.form-footer p {
  color: #666;
  margin: 0;
}

.link {
  color: #667eea;
  cursor: pointer;
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}

.error-message {
  background: #fee;
  color: #c33;
  padding: 12px;
  border-radius: 6px;
  margin-top: 15px;
  text-align: center;
  border: 1px solid #fcc;
}

.success-message {
  background: #efe;
  color: #363;
  padding: 12px;
  border-radius: 6px;
  margin-top: 15px;
  text-align: center;
  border: 1px solid #cfc;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-box {
    padding: 30px 20px;
    margin: 10px;
  }
  
  .logo h1 {
    font-size: 20px;
  }
}
</style>
