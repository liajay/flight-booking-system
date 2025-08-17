// 存储工具
export const storage = {
  // 设置token
  setToken(token) {
    localStorage.setItem('token', token)
  },

  // 获取token
  getToken() {
    return localStorage.getItem('token')
  },

  // 删除token
  removeToken() {
    localStorage.removeItem('token')
  },

  // 设置用户信息
  setUser(user) {
    localStorage.setItem('user', JSON.stringify(user))
  },

  // 获取用户信息
  getUser() {
    const user = localStorage.getItem('user')
    return user ? JSON.parse(user) : null
  },

  // 删除用户信息
  removeUser() {
    localStorage.removeItem('user')
  },

  // 清除所有数据
  clear() {
    this.removeToken()
    this.removeUser()
  }
}

// 消息提示工具
export const message = {
  success(msg) {
    console.log(`✅ ${msg}`)
    // 这里可以集成第三方消息组件，如Element Plus的Message
  },

  error(msg) {
    console.error(`❌ ${msg}`)
    // 这里可以集成第三方消息组件
  },

  warning(msg) {
    console.warn(`⚠️ ${msg}`)
    // 这里可以集成第三方消息组件
  }
}

// 验证工具
export const validator = {
  // 验证邮箱
  isEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return re.test(email)
  },

  // 验证用户名（字母数字下划线，3-20位）
  isUsername(username) {
    const re = /^[a-zA-Z0-9_]{3,20}$/
    return re.test(username)
  },

  // 验证密码（至少6位）
  isPassword(password) {
    return password && password.length >= 6
  }
}
