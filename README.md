# 航班订票系统

基于 Spring Cloud Alibaba 微服务架构的航班订票系统，包含完整的用户管理、订单处理、库存管理等功能。

## 项目架构

### 技术栈
- **服务框架**: Spring Boot 2.7.18, Spring Cloud 2021.0.8
- **微服务**: Spring Cloud Alibaba 2021.0.5.0
- **服务注册与发现**: Nacos 2.2.3
- **配置中心**: Nacos Config
- **API 网关**: Spring Cloud Gateway
- **数据库**: MySQL 8.0.42
- **ORM 框架**: MyBatis Plus 3.5.3.1
- **连接池**: Druid 1.2.16
- **缓存**: Redis 7.0
- **消息队列**: Apache Kafka 3.4
- **分布式事务**: Seata 1.7.0
- **负载均衡**: Spring Cloud LoadBalancer
- **服务调用**: OpenFeign
- **分布式锁**: Redisson
- **API 文档**: Swagger 3.0.0
- **工具类**: Hutool 5.8.18

### 模块说明

#### 1. common (公共模块)
- 通用实体类 (Entity/DTO/VO)
- 工具类 (Utils)
- 异常处理 (Exception Handler)
- 通用响应格式 (Result)
- 常量定义 (Constants)

#### 2. gateway (网关服务)
- API 路由与转发
- 统一认证与鉴权
- 限流与熔断
- 跨域处理
- API 文档聚合

#### 3. user-service (用户服务)
- 用户注册与登录
- 用户信息管理
- JWT Token 生成与验证
- 权限管理

#### 4. order-service (订单服务)
- 订单创建与管理
- 订单状态流转
- 订单支付处理
- 订单事件发布 (Kafka)
- 分布式事务协调 (Seata)

#### 5. inventory-service (库存服务)
- 航班信息管理
- 座位库存管理
- 库存锁定与释放
- 分布式锁控制
- 高并发座位预订

## 部署架构

### 基础设施
- **负载均衡器**: 处理外部流量分发
- **API 网关**: 统一入口，服务路由
- **服务注册中心**: Nacos 集群
- **配置中心**: Nacos Config
- **数据库**: MySQL 主从架构
- **缓存**: Redis 集群
- **消息队列**: Kafka 集群
- **事务协调器**: Seata Server

### 服务部署
所有服务支持容器化部署 (Docker)，使用 Docker Compose 进行本地开发环境搭建。

## 开发规范

### 1. 代码结构
```
服务模块/
├── src/main/java/com/liajay/模块名/
│   ├── controller/     # 控制器层
│   ├── service/        # 业务逻辑层
│   ├── mapper/         # 数据访问层
│   ├── entity/         # 实体类
│   ├── dto/            # 数据传输对象
│   ├── vo/             # 视图对象
│   ├── config/         # 配置类
│   └── Application.java # 启动类
├── src/main/resources/
│   ├── mapper/         # MyBatis XML 文件
│   ├── bootstrap.yml   # 启动配置
│   └── application.yml # 应用配置
└── pom.xml
```

### 2. 版本管理
- 统一在父 POM 中管理所有依赖版本
- 使用 properties 定义版本号
- 子模块继承父 POM 配置

### 3. 配置管理
- 使用 Nacos Config 统一管理配置
- 本地开发使用 bootstrap.yml
- 生产环境配置存储在 Nacos

## 快速开始

### 1. 环境准备
- JDK 17+
- Maven 3.6+
- Docker & Docker Compose

### 2. 启动基础设施
```bash
cd docker
docker-compose up -d
```

### 3. 编译项目
```bash
mvn clean install
```

### 4. 启动服务
按顺序启动各个服务：
1. gateway
2. user-service
3. inventory-service
4. order-service

### 5. 访问系统
- API 网关: http://localhost:8080
- Nacos 控制台: http://localhost:8848/nacos (nacos/nacos)
- Seata 控制台: http://localhost:7091

## API 文档
各服务启动后，可通过以下地址访问 API 文档：
- 用户服务: http://localhost:8081/swagger-ui/
- 订单服务: http://localhost:8082/swagger-ui/
- 库存服务: http://localhost:8083/swagger-ui/

## 监控与日志
- Spring Boot Actuator 提供健康检查
- 日志统一输出到控制台和文件
- 可集成 ELK 或其他日志系统

## 注意事项
1. 确保 Nacos、MySQL、Redis、Kafka 等基础组件正常启动
2. 数据库需要预先创建相关表结构
3. Seata 需要在数据库中创建相关事务表
4. 生产环境需要调整各组件的资源配置
