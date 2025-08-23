import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',  // 使用代理，请求会转发到http://localhost:8081/api
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    console.log('API Response:', response)
    return response
  },
  error => {
    console.error('API Error:', error)
    
    // 只有在401未授权且不在登录页面时才跳转到登录页
    if (error.response && error.response.status === 401) {
      // Token过期或无效，清除本地存储
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      
      // 只有当前不在登录页面时才跳转到登录页
      // 如果已经在登录页面，让组件自己处理401错误显示
      if (window.location.pathname !== '/login' && window.location.pathname !== '/') {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default api
