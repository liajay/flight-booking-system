# 航班订票系统

基于 Spring Cloud Alibaba 微服务架构的航班订票系统，包含完整的用户管理、订单处理、库存管理等功能。

## 技术栈

- **后端**: Spring Boot 2.7.18 + Spring Cloud Alibaba 2021.0.5.0
- **前端**: Vue 3 + Vite
- **数据库**: MySQL 8.0
- **注册中心**: Nacos

## 项目结构

```
├── user-service/          # 用户服务
├── order-service/         # 订单服务  
├── inventory-service/     # 库存服务
├── gateway/              # 网关服务
├── frontend/             # 前端项目
└── docker/               # Docker 配置
```

## 快速启动


#### 前置条件
- JDK 17
- Maven 3.6+
- Node.js 16+
- MySQL 8.0
- Redis

#### 启动步骤

1. **启动基础服务（仅数据库等）**
   ```bash
   cd docker
   docker-compose up -d mysql redis nacos kafka seata
   ```

2. **编译后端项目**
   ```bash
   # 在项目根目录
   mvn clean compile
   ```

3. **启动后端服务**
   ```bash
   # 启动网关服务
   cd gateway/bootstrap
   mvn spring-boot:run
   
   # 启动用户服务
   cd user-service/bootstrap  
   mvn spring-boot:run
   
   # 启动订单服务
   cd order-service/bootstrap
   mvn spring-boot:run
   
   # 启动库存服务
   cd inventory-service/bootstrap
   mvn spring-boot:run
   ```

4. **启动前端项目**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

## 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| 前端 | 5173 | Vue 开发服务器 |
| 网关 | 8080 | API 网关 |
| 用户服务 | 8081 | 用户注册、登录等 |
| 订单服务 | 8082 | 订单管理 |
| 库存服务 | 8083 | 座位库存管理 |
| Nacos | 8848 | 服务注册发现 |
| MySQL | 3306 | 数据库 |

## 访问地址

- **前端页面**: http://localhost:3000
- **API 接口**: http://localhost:8080
- **Nacos 控制台**: http://localhost:8848/nacos

```



