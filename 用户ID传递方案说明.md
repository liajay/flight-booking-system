# 微服务架构中JWT用户ID传递方案

## 概述

在我们的微服务架构中，JWT在网关处被验证，载荷中包含userId。为了让下游服务能够获取这个userId，我们采用了**HTTP请求头传递**的方案。

## 架构流程

```
客户端 → 网关(JWT验证) → 下游服务(获取userId)
```

1. **客户端**发送携带JWT的请求到网关
2. **网关**验证JWT并从载荷中提取userId
3. **网关**将userId添加到请求头`X-User-Id`中
4. **下游服务**从请求头中获取userId

## 实现方案

### 1. 网关端实现

#### 1.1 更新JwtAuthFilter

文件：`gateway/web/src/main/java/com/liajay/flightbooking/gateway/filter/JwtAuthFilter.java`

主要改动：
- 注入`JwtUtil`工具类
- 验证JWT后提取userId
- 使用`HttpServletRequestWrapper`将userId添加到请求头`X-User-Id`

```java
// 从JWT中提取userId
Long userId = jwtUtil.getUserIdFromToken(token);

// 创建包装的请求，添加userId到请求头
HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
    @Override
    public String getHeader(String name) {
        if ("X-User-Id".equals(name)) {
            return userId.toString();
        }
        return super.getHeader(name);
    }
};
```

### 2. 下游服务端实现

#### 2.1 创建UserContextUtil工具类

每个服务都需要创建对应的工具类：
- `inventory-service/util/src/main/java/com/liajay/flightbooking/inventory/util/UserContextUtil.java`
- `user-service/util/src/main/java/com/liajay/flightbooking/user/util/UserContextUtil.java`
- `order-service/util/src/main/java/com/liajay/flightbooking/order/util/UserContextUtil.java`

主要方法：
```java
// 设置当前线程的用户ID
public static void setCurrentUserId(Long userId);

// 获取当前线程的用户ID
public static Long getCurrentUserId();

// 检查是否设置了用户ID
public static boolean hasCurrentUserId();

// 获取用户ID，如果未设置则抛出异常
public static Long requireCurrentUserId();

// 清除当前线程的用户ID
public static void clearCurrentUserId();
```

#### 2.2 创建UserContextInterceptor拦截器

文件：`inventory-service/web/src/main/java/com/liajay/flightbooking/inventory/web/interceptor/UserContextInterceptor.java`

功能：
- 在请求处理前从请求头中提取userId并设置到ThreadLocal
- 在请求完成后清理ThreadLocal防止内存泄漏

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String userIdStr = request.getHeader("X-User-Id");
    if (userIdStr != null && !userIdStr.trim().isEmpty()) {
        Long userId = Long.valueOf(userIdStr);
        UserContextUtil.setCurrentUserId(userId);
    }
    return true;
}

@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    UserContextUtil.clearCurrentUserId();
}
```

#### 2.3 配置拦截器

在各服务的WebConfig中注册拦截器：

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userContextInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/health");
}
```

### 3. 控制器中使用

在任何需要获取当前用户ID的地方，都可以使用：

```java
@GetMapping("/my-bookings")
public HttpResponse<SeatQueryResultDTO> getMyBookedSeats() {
    // 获取当前用户ID
    Long currentUserId = UserContextUtil.getCurrentUserId();
    
    if (currentUserId == null) {
        return HttpResponse.error("用户未登录或用户ID不存在");
    }
    
    // 业务逻辑处理
    // ...
}
```

## 方案优势

1. **简单易实现**：只需要在网关处添加请求头，下游服务读取即可
2. **性能好**：避免了在每个服务中重复解析JWT
3. **安全性**：JWT只在网关处验证一次，下游服务通过内网通信获取用户信息
4. **解耦性好**：下游服务不需要了解JWT的具体实现
5. **易于调试**：可以直接在请求头中看到传递的用户ID

## 注意事项

1. **线程安全**：使用ThreadLocal确保多线程环境下的安全性
2. **内存泄漏防护**：在请求完成后必须清理ThreadLocal
3. **错误处理**：当userId不存在或格式错误时，要有适当的错误处理
4. **公共接口**：对于不需要用户身份的公共接口，可以在拦截器中排除

## 替代方案

如果不想使用ThreadLocal，也可以考虑以下方案：

### 方案2：直接从请求头读取

在控制器方法中直接从HttpServletRequest读取：

```java
@GetMapping("/my-bookings")
public HttpResponse<SeatQueryResultDTO> getMyBookedSeats(HttpServletRequest request) {
    String userIdStr = request.getHeader("X-User-Id");
    Long userId = userIdStr != null ? Long.valueOf(userIdStr) : null;
    // ...
}
```

### 方案3：通过方法参数注解

可以创建自定义注解和参数解析器：

```java
@GetMapping("/my-bookings")
public HttpResponse<SeatQueryResultDTO> getMyBookedSeats(@CurrentUserId Long userId) {
    // ...
}
```

## 测试建议

1. **单元测试**：测试UserContextUtil的各个方法
2. **集成测试**：测试网关到下游服务的完整流程
3. **负载测试**：确保在高并发情况下ThreadLocal不会出现问题

## 总结

这个方案在保证安全性的前提下，实现了用户身份信息在微服务之间的高效传递，是目前微服务架构中比较常用且成熟的解决方案。
