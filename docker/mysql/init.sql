-- 创建业务数据库
CREATE DATABASE IF NOT EXISTS flight_booking;

-- 创建Nacos数据库
CREATE DATABASE IF NOT EXISTS nacos DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

USE flight_booking;

-- 用户表
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 航班表
CREATE TABLE flights (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  flight_number VARCHAR(20) NOT NULL,
  departure VARCHAR(50) NOT NULL,
  destination VARCHAR(50) NOT NULL,
  departure_time DATETIME NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  total_seats INT NOT NULL,
  available_seats INT NOT NULL
);

-- 订单表 (包含Seata分布式事务需要的字段)
CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  flight_id BIGINT NOT NULL,
  seats INT NOT NULL,
  total_price DECIMAL(10, 2) NOT NULL,
  status ENUM('PENDING', 'PAID', 'CANCELLED') DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  xid VARCHAR(100) COMMENT 'Seata全局事务ID',
  INDEX idx_xid (xid)
);

-- Seata事务日志表 (必须)
CREATE TABLE IF NOT EXISTS undo_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  branch_id BIGINT NOT NULL,
  xid VARCHAR(100) NOT NULL,
  context VARCHAR(128) NOT NULL,
  rollback_info LONGBLOB NOT NULL,
  log_status INT NOT NULL,
  log_created DATETIME NOT NULL,
  log_modified DATETIME NOT NULL,
  UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 初始化航班数据
INSERT INTO flights (flight_number, departure, destination, departure_time, price, total_seats, available_seats) VALUES
('CA123', '北京', '上海', '2023-12-01 08:00:00', 1200.00, 200, 200),
('MU456', '上海', '广州', '2023-12-02 14:30:00', 900.00, 180, 180);

