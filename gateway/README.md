# Flight Booking Gateway

这是一个基于Spring MVC实现的简单网关服务，用于转发请求到各个微服务。

## 功能特性

- **JWT认证过滤器**: 验证请求头中的JWT token格式和基本有效性
- **路由转发**: 根据URL路径自动转发到对应的后端服务
- **请求代理**: 完整转发HTTP请求（包括请求头、请求体、查询参数）

## 路由规则

- `/api/user/**` → user-service (localhost:8081)
- `/api/order/**` → order-service (localhost:8082)  
- `/api/inventory/**` → inventory-service (localhost:8083)

## 公共接口（无需JWT验证）

- `/api/user/login` - 用户登录
- `/api/user/register` - 用户注册
- `/health` - 健康检查
- `/` - 网关信息

## 启动方式

### 使用Maven启动
```bash
cd gateway/bootstrap
mvn spring-boot:run
```

### 使用JAR包启动
```bash
cd gateway/bootstrap
mvn clean package
java -jar target/gateway-bootstrap-1.0-SNAPSHOT.jar
```

## API测试示例

### 1. 健康检查
```bash
curl http://localhost:8080/health
```

### 2. 用户登录（无需JWT）
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"password"}'
```

### 3. 访问需要JWT的接口
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
```

## 配置说明

网关配置位于 `application.yml` 文件中：

```yaml
server:
  port: 8080  # 网关端口

gateway:
  services:
    user-service:
      url: http://localhost:8081
    order-service:  
      url: http://localhost:8082
    inventory-service:
      url: http://localhost:8083
```

## JWT验证说明

当前实现了简单的JWT格式验证：
- 验证Authorization头是否存在且以"Bearer "开头
- 验证JWT是否包含三个用点分隔的部分
- 验证各部分是否为有效格式

**注意**: 当前未实现完整的JWT签名验证，生产环境需要添加密钥验证逻辑。

## 错误处理

- `401 Unauthorized`: JWT验证失败
- `404 Not Found`: 路由规则不匹配
- `500 Internal Server Error`: 转发失败或后端服务不可用

## 日志配置

网关提供详细的调试日志，可以通过修改 `application.yml` 中的日志级别来控制：

```yaml
logging:
  level:
    com.liajay.flightbooking.gateway: DEBUG
```
