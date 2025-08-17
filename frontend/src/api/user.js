import api from './request.js'

// 用户相关API
export const userApi = {
  // 用户注册
  register(data) {
    return api.post('/users/register', data)
  },

  // 用户登录
  login(data) {
    return api.post('/users/login', data)
  },

  // 获取用户信息
  getUserInfo(userId) {
    return api.get(`/users/${userId}`)
  }
}

export default userApi
