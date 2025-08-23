# 航班预订系统 HTTP 接口文档

## 1. 概述

本文档描述了航班预订系统中库存服务（Inventory Service）和用户服务（User Service）的HTTP接口规范。

### 1.1 服务列表

- **库存服务 (Inventory Service)**: 负责航班和座位信息的管理
- **用户服务 (User Service)**: 负责用户注册、登录等用户管理功能

### 1.2 通用响应格式

所有接口都使用统一的响应格式：

```json
{
  "success": true,
  "message": "错误信息（仅在失败时返回）",
  "data": {
    // 具体的响应数据
  }
}
```

## 2. 库存服务接口

### 2.1 航班管理接口

#### 2.1.1 根据航班号查询航班详情

**请求信息**
- **URL**: `GET /api/flights/{flightNumber}`
- **路径参数**:
  - `flightNumber`: 航班号（字符串）

**响应数据**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "flightNumber": "CA1234",
    "airline": "中国国际航空",
    "departureCity": "北京",
    "arrivalCity": "上海",
    "departureTime": "2025-08-24T10:00:00",
    "arrivalTime": "2025-08-24T12:30:00",
    "basePrice": 800.00,
    "status": "SCHEDULED"
  }
}
```

#### 2.1.2 分页查询航班列表

**请求信息**
- **URL**: `GET /api/flights`
- **查询参数**:
  - `flightNumber`: 航班号（可选）
  - `airline`: 航空公司（可选）
  - `departureCity`: 出发城市（可选）
  - `arrivalCity`: 到达城市（可选）
  - `startTime`: 开始时间（可选，格式：yyyy-MM-ddTHH:mm:ss）
  - `endTime`: 结束时间（可选，格式：yyyy-MM-ddTHH:mm:ss）
  - `status`: 航班状态（可选，值：SCHEDULED/DELAYED/CANCELLED/DEPARTED/ARRIVED，默认：SCHEDULED）
  - `page`: 页码（可选，默认：0）
  - `size`: 每页大小（可选，默认：20）
  - `sortBy`: 排序字段（可选，值：departureTime/arrivalTime/flightNumber/airline/basePrice，默认：departureTime）
  - `sortDirection`: 排序方向（可选，值：ASC/DESC，默认：ASC）

**响应数据**
```json
{
  "success": true,
  "data": {
    "flightList": [
      {
        "id": 1,
        "flightNumber": "CA1234",
        "airline": "中国国际航空",
        "departureCity": "北京",
        "arrivalCity": "上海",
        "departureTime": "2025-08-24T10:00:00",
        "arrivalTime": "2025-08-24T12:30:00",
        "basePrice": 800.00,
        "status": "SCHEDULED"
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "currentPage": 0,
    "pageSize": 20,
    "hasNext": true,
    "hasPrevious": false,
    "isFirst": true,
    "isLast": false
  }
}
```

#### 2.1.3 根据出发地和目的地查询航班

**请求信息**
- **URL**: `GET /api/flights/route`
- **查询参数**:
  - `departureCity`: 出发城市（必填）
  - `arrivalCity`: 到达城市（必填）
  - 其他参数同 2.1.2

**响应数据**: 同 2.1.2

#### 2.1.4 根据航空公司查询航班

**请求信息**
- **URL**: `GET /api/flights/airline/{airline}`
- **路径参数**:
  - `airline`: 航空公司名称
- **查询参数**: 同 2.1.2（除了airline）

**响应数据**: 同 2.1.2

#### 2.1.5 根据日期范围查询航班

**请求信息**
- **URL**: `GET /api/flights/date-range`
- **查询参数**:
  - `startTime`: 开始时间（必填，格式：yyyy-MM-ddTHH:mm:ss）
  - `endTime`: 结束时间（必填，格式：yyyy-MM-ddTHH:mm:ss）
  - 其他参数同 2.1.2

**响应数据**: 同 2.1.2

#### 2.1.6 获取所有有效航班

**请求信息**
- **URL**: `GET /api/flights/active`
- **查询参数**: 同 2.1.2（status固定为SCHEDULED）

**响应数据**: 同 2.1.2

### 2.2 座位管理接口

#### 2.2.1 根据航班号查询座位

**请求信息**
- **URL**: `GET /api/seats/flight-number/{flightNumber}`
- **路径参数**:
  - `flightNumber`: 航班号
- **查询参数**:
  - `seatClass`: 座位舱位等级（可选，值：ECONOMY/BUSINESS/FIRST）
  - `isAvailable`: 是否可用（可选，布尔值）
  - `page`: 页码（可选，默认：0）
  - `size`: 每页大小（可选，默认：50）
  - `sortBy`: 排序字段（可选，值：seatNumber/seatClass/price/isAvailable，默认：seatNumber）
  - `sortDirection`: 排序方向（可选，值：ASC/DESC，默认：ASC）

**响应数据**
```json
{
  "success": true,
  "data": {
    "seatList": [
      {
        "id": 1,
        "flightNumber": "CA1234",
        "seatNumber": "1A",
        "seatClass": "ECONOMY",
        "isAvailable": true,
        "price": 800.00
      }
    ],
    "totalElements": 180,
    "totalPages": 4,
    "currentPage": 0,
    "pageSize": 50,
    "hasNext": true,
    "hasPrevious": false,
    "isFirst": true,
    "isLast": false,
    "totalSeats": 180,
    "availableSeats": 120,
    "occupiedSeats": 60
  }
}
```

#### 2.2.2 根据航班号查询可用座位

**请求信息**
- **URL**: `GET /api/seats/flight-number/{flightNumber}/available`
- **路径参数**:
  - `flightNumber`: 航班号
- **查询参数**: 同 2.2.1（isAvailable固定为true）

**响应数据**: 同 2.2.1

#### 2.2.3 根据航班号和舱位等级查询座位

**请求信息**
- **URL**: `GET /api/seats/flight/{flightNumber}/class/{seatClass}`
- **路径参数**:
  - `flightNumber`: 航班号
  - `seatClass`: 舱位等级（ECONOMY/BUSINESS/FIRST）
- **查询参数**: 同 2.2.1（除了seatClass）

**响应数据**: 同 2.2.1

#### 2.2.4 根据航班号和座位号查询特定座位

**请求信息**
- **URL**: `GET /api/seats/flight/{flightNumber}/seat/{seatNumber}`
- **路径参数**:
  - `flightNumber`: 航班号
  - `seatNumber`: 座位号

**响应数据**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "flightNumber": "CA1234",
    "seatNumber": "1A",
    "seatClass": "ECONOMY",
    "isAvailable": true,
    "price": 800.00
  }
}
```

#### 2.2.5 获取航班座位统计信息

**请求信息**
- **URL**: `GET /api/seats/flight/{flightNumber}/statistics`
- **路径参数**:
  - `flightNumber`: 航班号

**响应数据**: 同 2.2.1（包含统计信息）

#### 2.2.6 根据查询条件查询座位

**请求信息**
- **URL**: `GET /api/seats`
- **查询参数**:
  - `flightNumber`: 航班号（必填）
  - 其他参数同 2.2.1

**响应数据**: 同 2.2.1

## 3. 用户服务接口

### 3.1 用户注册

**请求信息**
- **URL**: `POST /api/users/register`
- **Content-Type**: `application/json`
- **请求体**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```

**请求参数验证规则**:
- `username`: 不能为空，需符合用户名格式要求
- `password`: 不能为空，需符合密码强度要求

**响应数据**
```json
{
  "success": true,
  "data": {
    "success": true
  }
}
```

### 3.2 用户登录

**请求信息**
- **URL**: `POST /api/users/login`
- **Content-Type**: `application/json`
- **请求体**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```

**响应数据**
```json
{
  "success": true,
  "data": {
    "token": "JWT令牌字符串"
  }
}
```

### 3.3 健康检查

**请求信息**
- **URL**: `GET /api/users/health`

**响应数据**
```json
{
  "success": true,
  "data": "User service is running"
}
```

## 4. 错误码说明

### 4.1 通用错误格式

当接口调用失败时，返回格式如下：

```json
{
  "success": false,
  "message": "具体的错误信息",
  "data": null
}
```

### 4.2 常见错误情况

- **航班不存在**: 当查询的航班号不存在时
- **座位不存在**: 当查询的座位不存在时
- **参数验证失败**: 当请求参数不符合验证规则时
- **用户名或密码错误**: 登录时凭据不正确
- **用户已存在**: 注册时用户名已被占用

## 5. 数据字典

### 5.1 航班状态 (FlightStatus)

| 值 | 说明 |
|---|---|
| SCHEDULED | 已计划 |
| DELAYED | 延误 |
| CANCELLED | 取消 |
| DEPARTED | 已起飞 |
| ARRIVED | 已到达 |

### 5.2 座位舱位等级 (SeatClass)

| 值 | 说明 |
|---|---|
| ECONOMY | 经济舱 |
| BUSINESS | 商务舱 |
| FIRST | 头等舱 |

## 6. 注意事项

1. 所有时间格式都使用 ISO 8601 格式：`yyyy-MM-ddTHH:mm:ss`
2. 分页参数中 `page` 从 0 开始计数
3. 价格字段使用 BigDecimal 类型，保证精度
4. 座位查询时必须提供航班号参数
5. 用户注册和登录的密码在传输时需要注意安全性
6. JWT令牌用于后续需要身份验证的接口调用

## 7. 示例调用

### 7.1 查询北京到上海的航班

```bash
curl -X GET "http://localhost:8080/api/flights/route?departureCity=北京&arrivalCity=上海&page=0&size=10"
```

### 7.2 查询特定航班的可用座位

```bash
curl -X GET "http://localhost:8080/api/seats/flight-number/CA1234/available?seatClass=ECONOMY"
```

### 7.3 用户注册

```bash
curl -X POST "http://localhost:8081/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"TestPass123"}'
```

### 7.4 用户登录

```bash
curl -X POST "http://localhost:8081/api/users/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"TestPass123"}'
```
