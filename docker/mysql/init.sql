-- 创建业务数据库
CREATE DATABASE IF NOT EXISTS flight_booking;
CREATE DATABASE IF NOT EXISTS flight_booking_test;
-- 创建Nacos数据库
CREATE DATABASE IF NOT EXISTS nacos DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

USE flight_booking;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS flights (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `flight_number` VARCHAR(20) NOT NULL UNIQUE COMMENT '航班号',
    `airline` VARCHAR(100) NOT NULL COMMENT '航空公司',
    `departure_city` VARCHAR(100) NOT NULL COMMENT '出发城市',
    `arrival_city` VARCHAR(100) NOT NULL COMMENT '到达城市',
    `departure_time` DATETIME NOT NULL COMMENT '出发时间',
    `arrival_time` DATETIME NOT NULL COMMENT '到达时间',
    `base_price` DECIMAL(10,2) NOT NULL COMMENT '基础价格',
    `status` VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' COMMENT '航班状态',
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_flight_number (flight_number),
    INDEX idx_departure_city (departure_city),
    INDEX idx_arrival_city (arrival_city),
    INDEX idx_departure_time (departure_time),
    INDEX idx_status (status),
    INDEX idx_route (departure_city, arrival_city),
    INDEX idx_airline (airline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='航班表';

-- 创建座位表
CREATE TABLE IF NOT EXISTS seats (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `flight_number` VARCHAR(20) NOT NULL COMMENT '航班号',
    `seat_number` VARCHAR(10) NOT NULL COMMENT '座位号',
    `seat_class` VARCHAR(20) NOT NULL COMMENT '舱位等级',
    `is_available` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否可用',
    `price` DECIMAL(10,2) NOT NULL COMMENT '座位价格',
    `created_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_flight_seat (flight_number, seat_number),
    INDEX idx_flight_id (flight_number),
    INDEX idx_seat_class (seat_class),
    INDEX idx_is_available (is_available),
    INDEX idx_flight_available (flight_number, is_available),
    INDEX idx_flight_class (flight_number, seat_class),
    FOREIGN KEY (flight_number) REFERENCES flights(flight_number) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `order_number` VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `flight_number` VARCHAR(20) NOT NULL COMMENT '航班号',
    `seat_number` VARCHAR(10) NOT NULL COMMENT '座位号',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    `gmt_create` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_number (order_number),
    INDEX idx_user_id (user_id),
    INDEX idx_flight_number (flight_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

INSERT INTO flights (flight_number, airline, departure_city, arrival_city, departure_time, arrival_time, base_price, status) VALUES
('CA1234', '中国国际航空', '北京', '上海', '2025-08-21 08:00:00', '2025-08-21 10:30:00', 800.00, 'SCHEDULED'),
('MU5678', '中国东方航空', '上海', '广州', '2025-08-21 14:30:00', '2025-08-21 17:00:00', 900.00, 'SCHEDULED'),
('CZ9012', '中国南方航空', '广州', '深圳', '2025-08-21 19:00:00', '2025-08-21 20:00:00', 300.00, 'SCHEDULED'),
('3U3456', '四川航空', '成都', '北京', '2025-08-22 09:30:00', '2025-08-22 12:30:00', 1200.00, 'SCHEDULED'),
('HU7890', '海南航空', '海口', '上海', '2025-08-22 16:00:00', '2025-08-22 19:30:00', 1500.00, 'SCHEDULED');

-- 为每个航班插入座位数据
-- CA1234航班座位（波音737，经济舱120座，商务舱20座）
INSERT INTO seats (flight_number, seat_number, seat_class, is_available, price)
SELECT 'CA1234', seat_number, seat_class, TRUE, price FROM (
    -- 商务舱 (1-4排，每排4座：A,C,D,F)
    SELECT CONCAT(row_num, seat_letter) as seat_number, 'BUSINESS' as seat_class, 1200.00 as price
    FROM (SELECT 1 as row_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) r
    CROSS JOIN (SELECT 'A' as seat_letter UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'F') s

    UNION ALL

    -- 经济舱 (7-26排，每排6座：A,B,C,D,E,F)
    SELECT CONCAT(row_num, seat_letter) as seat_number, 'ECONOMY' as seat_class, 800.00 as price
    FROM (SELECT 7 as row_num UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
          UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18
          UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24
          UNION SELECT 25 UNION SELECT 26) r
    CROSS JOIN (SELECT 'A' as seat_letter UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F') s
) seats_data;

-- MU5678航班座位（空客A320，经济舱150座，商务舱30座）
INSERT INTO seats (flight_number, seat_number, seat_class, is_available, price)
SELECT 'MU5678', seat_number, seat_class, TRUE, price FROM (
    -- 商务舱 (1-5排，每排4座：A,C,D,F) + (6排2座：A,F)
    SELECT CONCAT(row_num, seat_letter) as seat_number, 'BUSINESS' as seat_class, 1350.00 as price
    FROM (SELECT 1 as row_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) r
    CROSS JOIN (SELECT 'A' as seat_letter UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'F') s

    UNION ALL

    SELECT CONCAT('6', seat_letter) as seat_number, 'BUSINESS' as seat_class, 1350.00 as price
    FROM (SELECT 'A' as seat_letter UNION SELECT 'F') s

    UNION ALL

    -- 经济舱 (8-32排，每排6座：A,B,C,D,E,F)
    SELECT CONCAT(row_num, seat_letter) as seat_number, 'ECONOMY' as seat_class, 900.00 as price
    FROM (SELECT 8 as row_num UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13
          UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19
          UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
          UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30 UNION SELECT 31
          UNION SELECT 32) r
    CROSS JOIN (SELECT 'A' as seat_letter UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F') s
) seats_data;

-- CZ9012航班座位（小型客机，经济舱60座）
INSERT INTO seats (flight_number, seat_number, seat_class, is_available, price)
SELECT 'CZ9012', seat_number, seat_class, TRUE, price FROM (
    -- 经济舱 (1-20排，每排3座：A,B,C)
    SELECT CONCAT(row_num, seat_letter) as seat_number, 'ECONOMY' as seat_class, 300.00 as price
    FROM (SELECT 1 as row_num UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6
          UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
          UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18
          UNION SELECT 19 UNION SELECT 20) r
    CROSS JOIN (SELECT 'A' as seat_letter UNION SELECT 'B' UNION SELECT 'C') s
) seats_data;

UPDATE seats SET is_available = FALSE WHERE flight_number = 'CA1234' AND seat_number IN ('1A', '1C', '7A', '7B', '12F');
UPDATE seats SET is_available = FALSE WHERE flight_number = 'MU5678' AND seat_number IN ('2A', '2C', '8A', '8B', '15D', '15E');
UPDATE seats SET is_available = FALSE WHERE flight_number = 'CZ9012' AND seat_number IN ('5A', '5B', '10A', '15C');

INSERT INTO orders (order_number, user_id, flight_number, seat_number, amount)
VALUES ('O001', 479887545757470720, 'CZ9012', '5A', 800);