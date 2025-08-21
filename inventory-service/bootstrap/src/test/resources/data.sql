USE flight_booking_test;

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