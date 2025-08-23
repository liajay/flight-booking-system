# 订单服务 (Order Service)

航班订票系统的订单管理服务，负责订单的创建和查询功能。

## 功能特性

### 订单管理功能
- ✅ 创建订单
- ✅ 根据订单编号查询订单
- ✅ 根据用户ID分页查询订单列表

## 项目结构

```
order-service/
├── bootstrap/           # 启动模块
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/order/
│   │       ├── OrderServiceApplication.java        # 启动类
│   │       └── GlobalExceptionHandler.java         # 全局异常处理
│   └── pom.xml
├── web/                 # Web层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/order/web/
│   │       ├── controller/    # 控制器
│   │       ├── request/       # 请求对象
│   │       └── response/      # 响应对象
│   └── pom.xml
├── service/             # 服务层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/order/service/
│   │       ├── OrderService.java     # 订单服务接口
│   │       ├── impl/                 # 服务实现
│   │       └── dto/                  # 数据传输对象
│   └── pom.xml
├── dal/                 # 数据访问层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/order/dal/
│   │       ├── dataobject/           # 数据对象
│   │       └── mapper/               # MyBatis Mapper
│   └── pom.xml
├── model/               # 模型层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/order/model/
│   │       └── vo/                   # 视图对象
│   └── pom.xml
├── util/                # 工具层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/order/util/
│   │       └── exception/            # 异常类
│   └── pom.xml
└── pom.xml              # 主POM文件
```

## 数据模型

### 订单表 (orders)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| order_number | VARCHAR(32) | 订单编号（唯一） |
| user_id | BIGINT | 用户ID |
| flight_number | VARCHAR(20) | 航班号 |
| seat_number | VARCHAR(10) | 座位号 |
| amount | DECIMAL(10,2) | 订单金额 |
| status | VARCHAR(20) | 订单状态 |
| gmt_create | DATETIME | 创建时间 |
| gmt_modified | DATETIME | 更新时间 |

## API接口

### 订单相关接口

#### 1. 创建订单
```
POST /api/orders
```

请求体：
```json
{
  "userId": 1,
  "flightNumber": "CA1234",
  "seatNumber": "1A",
  "amount": 1200.00
}
```

#### 2. 根据订单编号查询订单
```
GET /api/orders/{orderNumber}
```

#### 3. 根据用户ID分页查询订单
```
GET /api/orders/user/{userId}?page=0&size=10
```

## 技术栈

- **Java 17**
- **Spring Boot 2.x**
- **MyBatis**
- **MySQL 8.0**
- **Maven 3.x**

## 枚举定义

### 订单状态 (OrderStatus)
- `PENDING`: 待支付
- `PAID`: 已支付
- `CANCELLED`: 已取消
- `COMPLETED`: 已完成

## 启动方式

1. 确保MySQL数据库已启动
2. 确保数据库中已创建orders表
3. 配置数据库连接信息
4. 运行`OrderServiceApplication`启动服务
5. 服务默认端口: 8083

## 示例请求

### 创建订单
```bash
curl -X POST "http://localhost:8083/api/orders" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "flightNumber": "CA1234",
    "seatNumber": "1A",
    "amount": 1200.00
  }'
```

### 查询订单
```bash
curl -X GET "http://localhost:8083/api/orders/ORD20250823000001"
```

### 查询用户订单列表
```bash
curl -X GET "http://localhost:8083/api/orders/user/1?page=0&size=10"
```

## 返回格式

所有接口都使用统一的响应格式：

```json
{
  "success": true,
  "message": null,
  "data": {
    // 具体数据
  }
}
```

错误响应：
```json
{
  "success": false,
  "message": "错误信息",
  "data": null
}
```
