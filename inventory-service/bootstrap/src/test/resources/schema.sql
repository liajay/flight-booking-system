USE flight_booking_test;

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
     INDEX idx_flight_class (flight_number, seat_class)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表';

