# 航班订票系统 - 前端

基于 Vue 3 + Vite 构建的现代化前端应用。

## 🚀 快速开始

### 安装依赖
```bash
npm install
```

### 开发环境启动
```bash
npm run dev
```

访问 http://localhost:3000

### 生产环境构建
```bash
npm run build
```

### 预览生产构建
```bash
npm run preview
```

## 📁 项目结构

```
frontend/
├── public/              # 静态资源
├── src/
│   ├── api/            # API 接口
│   │   ├── request.js  # axios 配置
│   │   └── user.js     # 用户相关 API
│   ├── components/     # 公共组件
│   ├── utils/          # 工具函数
│   │   └── index.js    # 通用工具
│   ├── views/          # 页面组件
│   │   ├── LoginView.vue     # 登录页面
│   │   └── DashboardView.vue # 仪表板页面
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── index.html          # HTML 模板
├── vite.config.js      # Vite 配置
└── package.json        # 项目配置
```

## 🛠️ 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Vue Router** - Vue.js 官方路由
- **Axios** - HTTP 客户端

## 🔧 功能特性

- ✅ 用户注册/登录
- ✅ JWT Token 认证
- ✅ 路由守卫
- ✅ 响应式设计
- ✅ API 请求拦截
- ✅ 本地存储管理

## 🌐 API 接口

项目通过代理访问后端服务：
- 开发环境：`http://localhost:3000/api` → `http://localhost:8081/api`
- 后端服务需要运行在 8081 端口

## 📝 开发说明

1. **环境要求**
   - Node.js >= 16
   - npm >= 7

2. **开发流程**
   - 启动后端服务（端口 8081）
   - 启动前端开发服务器
   - 访问 http://localhost:3000

3. **构建部署**
   - 执行 `npm run build` 生成生产版本
   - 将 `dist` 目录部署到 Web 服务器
