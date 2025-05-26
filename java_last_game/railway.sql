CREATE TABLE `change_records`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '改签ID，主键',
  `ticket_id` bigint(20) NOT NULL COMMENT '原车票ID',
  `new_ticket_id` bigint(20) NULL DEFAULT NULL COMMENT '新车票ID(改签成功后生成)',
  `order_id` bigint(20) NOT NULL COMMENT '关联订单ID',
  `change_fee` decimal(10, 2) NOT NULL COMMENT '改签手续费',
  `price_difference` decimal(10, 2) NOT NULL COMMENT '票价差额',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0-处理中，1-改签成功，2-改签取消',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_new_ticket_id`(`new_ticket_id`) USING BTREE,
  INDEX `idx_ticket_id`(`ticket_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车票改签记录表' ROW_FORMAT = Dynamic;

CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_order_no`(`order_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

CREATE TABLE `passengers`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '乘车人ID，主键',
  `user_id` bigint(20) NOT NULL COMMENT '关联用户ID',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '乘车人姓名',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `passenger_type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '乘客类型：1-成人，2-儿童，3-学生，4-残疾军人',
  `is_default` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否默认乘车人：0-否，1-是',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_id_card`(`id_card`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '乘车人信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `seat_locks`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '锁定ID，主键',
  `seat_id` bigint(20) NOT NULL COMMENT '座位ID',
  `finish` tinyint(4) NOT NULL DEFAULT 0 COMMENT '1完成,0未完成,2取消',
  `expire_time` datetime(0) NOT NULL COMMENT '过期时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `lock_start` datetime(0) NULL DEFAULT NULL COMMENT '锁定开始',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '原因',
  `type` bigint(20) NULL DEFAULT NULL COMMENT '0-管理员锁定,1-用户购买锁定',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_train_date_seat`(`seat_id`) USING BTREE,
  INDEX `fk_seat_lock_seat`(`seat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '座位锁定表' ROW_FORMAT = Dynamic;

CREATE TABLE `seats`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '座位ID，主键',
  `carriage_id` bigint(20) NOT NULL COMMENT '车厢ID',
  `seat_number` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '座位号，如\"01A\"',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：1-可用，0-锁定',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `seats_pk`(`seat_number`, `id`) USING BTREE,
  INDEX `idx_carriage_id`(`carriage_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 285 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '座位信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `stations`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '车站ID，主键',
  `station_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车站名称',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所在城市',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所在省份',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-启用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_city`(`city`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车站信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `system_logs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID，主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块名称',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '操作状态：0-失败，1-成功',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误消息',
  `operation_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_module`(`module`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

CREATE TABLE `tickets`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '车票ID，主键',
  `ticket_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车票编号',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `passenger_id` bigint(20) NOT NULL COMMENT '乘车人ID',
  `train_id` bigint(20) NULL DEFAULT NULL COMMENT '车次ID',
  `date` date NOT NULL COMMENT '乘车日期',
  `departure_station_id` int(11) NOT NULL COMMENT '出发站ID',
  `arrival_station_id` int(11) NOT NULL COMMENT '到达站ID',
  `seat_type` tinyint(4) NOT NULL COMMENT '座位类型：1-商务座，2-一等座，3-二等座，4-无座',
  `carriage_id` bigint(20) NULL DEFAULT NULL COMMENT '车厢ID',
  `seat_id` bigint(20) NULL DEFAULT NULL COMMENT '座位ID',
  `price` decimal(10, 2) NOT NULL COMMENT '票价',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0-待支付，1-已出票，2-已退票，3-改签处理中，4-改签待支付，5-已改签',
  `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `refund_time` datetime(0) NULL DEFAULT NULL COMMENT '退款时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `seat_lock_id` bigint(20) NULL DEFAULT NULL COMMENT '座位锁定记录对应id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_ticket_no`(`ticket_no`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_passenger_id`(`passenger_id`) USING BTREE,
  INDEX `idx_train_date`(`train_id`, `date`) USING BTREE,
  INDEX `idx_seat_id`(`seat_id`) USING BTREE,
  INDEX `idx_carriage_id`(`carriage_id`) USING BTREE,
  INDEX `fk_ticket_departure_station`(`departure_station_id`) USING BTREE,
  INDEX `fk_ticket_arrival_station`(`arrival_station_id`) USING BTREE,
  INDEX `tickets_seat_lock_id__fk`(`seat_lock_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车票信息表' ROW_FORMAT = Dynamic;
CREATE DEFINER = `root`@`localhost` TRIGGER `seat_check_insert` BEFORE INSERT ON `tickets` FOR EACH ROW BEGIN
    IF NEW.seat_type = 4 THEN
        IF NEW.carriage_id IS NOT NULL OR NEW.seat_id IS NOT NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '当座位类型为无座时，车厢ID和座位ID必须为NULL';
        END IF;
    ELSE
        IF NEW.carriage_id IS NULL OR NEW.seat_id IS NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '当座位类型不为无座时，车厢ID和座位ID不能为NULL';
        END IF;
    END IF;
END;
CREATE DEFINER = `root`@`localhost` TRIGGER `seat_check_update` BEFORE UPDATE ON `tickets` FOR EACH ROW BEGIN
    IF NEW.seat_type = 4 THEN
        IF NEW.carriage_id IS NOT NULL OR NEW.seat_id IS NOT NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '当座位类型为无座时，车厢ID和座位ID必须为NULL';
        END IF;
    ELSE
        IF NEW.carriage_id IS NULL OR NEW.seat_id IS NULL THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '当座位类型不为无座时，车厢ID和座位ID不能为NULL';
        END IF;
    END IF;
END;

CREATE TABLE `train_carriages`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '车厢ID，主键',
  `model_id` int(11) NOT NULL COMMENT '车型ID',
  `carriage_number` int(10) NOT NULL COMMENT '车厢编号，如1',
  `carriage_type` tinyint(4) NOT NULL COMMENT '车厢类型：0-商务座，1-一等座，2-二等座',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_model_carriage`(`model_id`, `carriage_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车厢信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `train_models`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '车型ID，主键',
  `model_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车型名称，如\"复兴号CR400AF\"',
  `model_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车型代码',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：0-使用中，1停用',
  `max_capacity` int(11) NOT NULL COMMENT '最大载客量',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '车型描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_model_code`(`model_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆型号表' ROW_FORMAT = Dynamic;

CREATE TABLE `train_seats`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '座位ID，主键',
  `no_seat_tickets` int(11) NOT NULL DEFAULT 0 COMMENT '无座票数量',
  `business_price` decimal(10, 2) NOT NULL COMMENT '商务座价格',
  `first_class_price` decimal(10, 2) NOT NULL COMMENT '一等座价格',
  `second_class_price` decimal(10, 2) NOT NULL COMMENT '二等座价格',
  `no_seat_price` decimal(10, 2) NOT NULL COMMENT '无座票价格',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车次座位信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `train_stops`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '站点ID，主键',
  `train_id` bigint(20) NOT NULL COMMENT '车次ID',
  `station_id` int(11) NOT NULL COMMENT '车站ID',
  `sequence` int(11) NOT NULL COMMENT '站点顺序，从1开始',
  `arrival_time` time(0) NULL DEFAULT NULL COMMENT '到达时间',
  `stop_duration` int(11) NULL DEFAULT 0 COMMENT '停靠时长(分钟)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_train_station`(`train_id`, `station_id`) USING BTREE,
  INDEX `idx_train_sequence`(`train_id`, `sequence`) USING BTREE,
  INDEX `fk_stop_station`(`station_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车次站点经停表' ROW_FORMAT = Dynamic;
CREATE DEFINER = `root`@`localhost` TRIGGER `prevent_train_stop_update` BEFORE UPDATE ON `train_stops` FOR EACH ROW BEGIN
    IF OLD.station_id != NEW.station_id OR OLD.train_id != NEW.train_id THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'station_id and train_id cannot be modified after creation';
    END IF;
END;

CREATE TABLE `trains`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '车次ID',
  `train_seats_id` bigint(20) NOT NULL COMMENT '车次座位表',
  `train_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '车次编号，如G1234',
  `model_id` int(11) NOT NULL COMMENT '车型ID',
  `start_station_id` int(11) NOT NULL COMMENT '始发站ID',
  `end_station_id` int(11) NOT NULL COMMENT '终点站ID',
  `departure_time` time(0) NOT NULL COMMENT '发车时间',
  `arrival_time` time(0) NOT NULL COMMENT '到达时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `date` date NOT NULL COMMENT '日期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_start_end`(`start_station_id`, `end_station_id`) USING BTREE,
  INDEX `idx_model_id`(`model_id`) USING BTREE,
  INDEX `fk_train_end_station`(`end_station_id`) USING BTREE,
  INDEX `fk_trains_train_seats_1`(`train_seats_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车次信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，用于登录',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码，存储加密后的值',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱，可为空',
  `user_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户类型：0-普通用户，1-管理员',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '账号状态：0-禁用，1-正常',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE,
  UNIQUE INDEX `idx_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `verification_codes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '验证码ID，主键',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NOT NULL COMMENT '过期时间',
  `is_used` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否已使用：0-未使用，1-已使用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_email_phone`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '验证码表' ROW_FORMAT = Dynamic;

CREATE TABLE `waiting_orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '候补ID，主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `train_id` bigint(20) NOT NULL COMMENT '车次ID',
  `date` date NOT NULL COMMENT '乘车日期',
  `departure_station_id` int(11) NOT NULL COMMENT '出发站ID',
  `arrival_station_id` int(11) NOT NULL COMMENT '到达站ID',
  `seat_type` tinyint(4) NOT NULL COMMENT '座位类型：0-商务座，1-一等座，2-二等座',
  `passenger_count` int(11) NOT NULL DEFAULT 1 COMMENT '乘客数量',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0-等待中，1-候补成功，2-已取消，3-已过期',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_train_date`(`train_id`, `date`) USING BTREE,
  INDEX `fk_waiting_departure_station`(`departure_station_id`) USING BTREE,
  INDEX `fk_waiting_arrival_station`(`arrival_station_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '候补订单表' ROW_FORMAT = Dynamic;

ALTER TABLE `change_records` ADD CONSTRAINT `fk_change_new_ticket` FOREIGN KEY (`new_ticket_id`) REFERENCES `tickets` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `change_records` ADD CONSTRAINT `fk_change_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `change_records` ADD CONSTRAINT `fk_change_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `orders` ADD CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `passengers` ADD CONSTRAINT `fk_passenger_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `seat_locks` ADD CONSTRAINT `fk_seat_lock_seat` FOREIGN KEY (`seat_id`) REFERENCES `seats` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `seats` ADD CONSTRAINT `fk_seat_carriage` FOREIGN KEY (`carriage_id`) REFERENCES `train_carriages` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT;
ALTER TABLE `system_logs` ADD CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `fk_ticket_arrival_station` FOREIGN KEY (`arrival_station_id`) REFERENCES `stations` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `fk_ticket_carriage` FOREIGN KEY (`carriage_id`) REFERENCES `train_carriages` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `fk_ticket_departure_station` FOREIGN KEY (`departure_station_id`) REFERENCES `stations` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `fk_ticket_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `fk_ticket_passenger` FOREIGN KEY (`passenger_id`) REFERENCES `passengers` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `fk_ticket_seat` FOREIGN KEY (`seat_id`) REFERENCES `seats` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `tickets_trains_id_fk` FOREIGN KEY (`train_id`) REFERENCES `trains` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `tickets` ADD CONSTRAINT `tickets_seat_lock_id__fk` FOREIGN KEY (`seat_lock_id`) REFERENCES `seat_locks` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `train_carriages` ADD CONSTRAINT `fk_carriage_model` FOREIGN KEY (`model_id`) REFERENCES `train_models` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `train_stops` ADD CONSTRAINT `fk_stop_station` FOREIGN KEY (`station_id`) REFERENCES `stations` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `train_stops` ADD CONSTRAINT `train_stops_trains_id_fk` FOREIGN KEY (`train_id`) REFERENCES `trains` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `trains` ADD CONSTRAINT `fk_train_end_station_status` FOREIGN KEY (`end_station_id`) REFERENCES `stations` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `trains` ADD CONSTRAINT `fk_train_model` FOREIGN KEY (`model_id`) REFERENCES `train_models` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `trains` ADD CONSTRAINT `fk_train_start_station_status` FOREIGN KEY (`start_station_id`) REFERENCES `stations` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;
ALTER TABLE `trains` ADD CONSTRAINT `fk_trains_train_seats_1` FOREIGN KEY (`train_seats_id`) REFERENCES `train_seats` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `waiting_orders` ADD CONSTRAINT `fk_waiting_arrival_station` FOREIGN KEY (`arrival_station_id`) REFERENCES `stations` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `waiting_orders` ADD CONSTRAINT `fk_waiting_departure_station` FOREIGN KEY (`departure_station_id`) REFERENCES `stations` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `waiting_orders` ADD CONSTRAINT `fk_waiting_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `waiting_orders` ADD CONSTRAINT `waiting_orders_trains_id_fk` FOREIGN KEY (`train_id`) REFERENCES `trains` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `stations_view` AS select `stations`.`id` AS `id`,`stations`.`station_name` AS `station_name`,`stations`.`city` AS `city`,`stations`.`province` AS `province`,`stations`.`address` AS `address`,`stations`.`status` AS `status`,`stations`.`create_time` AS `create_time`,`stations`.`update_time` AS `update_time` from `stations` where (`stations`.`status` = 1);

