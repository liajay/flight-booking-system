# 库存服务 (Inventory Service)

航班订票系统的库存管理服务，负责航班和座位的查询功能。

## 功能特性

### 航班查询功能
- ✅ 根据航班号查询航班详情
- ✅ 根据出发地和目的地查询航班
- ✅ 根据航空公司查询航班  
- ✅ 根据日期范围查询航班
- ✅ 分页查询航班列表
- ✅ 查询所有有效航班

### 座位查询功能
- ✅ 根据航班ID查询所有座位
- ✅ 根据航班号查询所有座位
- ✅ 查询航班可用座位
- ✅ 根据舱位等级查询座位（经济舱、商务舱、头等舱）
- ✅ 根据座位号查询特定座位
- ✅ 获取航班座位统计信息

## 项目结构

```
inventory-service/
├── bootstrap/           # 启动模块
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/inventory/
│   │       ├── InventoryServiceApplication.java    # 启动类
│   │       └── GlobalExceptionHandler.java         # 全局异常处理
│   └── pom.xml
├── web/                 # Web层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/inventory/web/
│   │       ├── controller/    # 控制器
│   │       ├── request/       # 请求对象
│   │       ├── response/      # 响应对象
│   │       └── convertor/     # 转换器
│   └── pom.xml
├── service/             # 服务层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/inventory/service/
│   │       ├── FlightService.java     # 航班服务接口
│   │       ├── SeatService.java       # 座位服务接口
│   │       ├── impl/                  # 服务实现
│   │       └── dto/                   # 数据传输对象
│   └── pom.xml
├── dal/                 # 数据访问层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/inventory/dal/
│   │       ├── dataobject/            # 数据对象
│   │       └── repository/            # 数据仓库
│   └── pom.xml
├── model/               # 模型层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/inventory/model/
│   │       └── vo/                    # 视图对象
│   └── pom.xml
├── util/                # 工具层
│   ├── src/main/java/
│   │   └── com/liajay/flightbooking/inventory/util/
│   │       └── exception/             # 异常类
│   └── pom.xml
├── database-init.sql    # 数据库初始化脚本
├── 领域模型.puml         # UML领域模型图
└── pom.xml              # 主POM文件
```

## 数据模型

### 航班表 (flights)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| flight_number | VARCHAR(20) | 航班号（唯一） |
| airline | VARCHAR(100) | 航空公司 |
| departure_city | VARCHAR(100) | 出发城市 |
| arrival_city | VARCHAR(100) | 到达城市 |
| departure_time | DATETIME | 出发时间 |
| arrival_time | DATETIME | 到达时间 |
| base_price | DECIMAL(10,2) | 基础价格 |
| status | VARCHAR(20) | 航班状态 |
| created_time | DATETIME | 创建时间 |
| updated_time | DATETIME | 更新时间 |

### 座位表 (seats)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| flight_id | BIGINT | 航班ID |
| seat_number | VARCHAR(10) | 座位号 |
| seat_class | VARCHAR(20) | 舱位等级 |
| is_available | BOOLEAN | 是否可用 |
| price | DECIMAL(10,2) | 座位价格 |
| created_time | DATETIME | 创建时间 |
| updated_time | DATETIME | 更新时间 |

## API接口

### 航班相关接口

#### 1. 根据航班号查询航班
```
GET /api/flights/{flightNumber}
```

#### 2. 分页查询航班
```
GET /api/flights?page=0&size=20&sortBy=departureTime&sortDirection=ASC
```

#### 3. 根据路线查询航班
```
GET /api/flights/route?departureCity=北京&arrivalCity=上海
```

#### 4. 根据航空公司查询航班
```
GET /api/flights/airline/{airline}
```

#### 5. 根据日期范围查询航班
```
GET /api/flights/date-range?startTime=2025-08-21T00:00:00&endTime=2025-08-21T23:59:59
```

### 座位相关接口

#### 1. 根据航班ID查询座位
```
GET /api/seats/flight/{flightId}
```

#### 2. 根据航班号查询座位
```
GET /api/seats/flight-number/{flightNumber}
```

#### 3. 查询可用座位
```
GET /api/seats/flight/{flightId}/available
GET /api/seats/flight-number/{flightNumber}/available
```

#### 4. 根据舱位等级查询座位
```
GET /api/seats/flight/{flightId}/class/{seatClass}
```

#### 5. 查询特定座位
```
GET /api/seats/flight/{flightId}/seat/{seatNumber}
```

#### 6. 获取座位统计信息
```
GET /api/seats/flight/{flightId}/statistics
```

## 技术栈

- **Java 17**
- **Spring Boot 2.x**
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven 3.x**

## 枚举定义

### 航班状态 (FlightStatus)
- `SCHEDULED`: 已安排
- `DELAYED`: 延误
- `CANCELLED`: 取消
- `DEPARTED`: 已起飞
- `ARRIVED`: 已到达

### 座位舱位等级 (SeatClass)
- `ECONOMY`: 经济舱
- `BUSINESS`: 商务舱
- `FIRST`: 头等舱

## 启动方式

1. 确保MySQL数据库已启动
2. 执行`database-init.sql`初始化数据库表和示例数据
3. 配置数据库连接信息
4. 运行`InventoryServiceApplication`启动服务
5. 服务默认端口: 8080

## 示例请求

### 查询航班
```bash
curl -X GET "http://localhost:8080/api/flights/CA1234"
```

### 查询北京到上海的航班
```bash
curl -X GET "http://localhost:8080/api/flights/route?departureCity=北京&arrivalCity=上海"
```

### 查询航班可用座位
```bash
curl -X GET "http://localhost:8080/api/seats/flight-number/CA1234/available"
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
