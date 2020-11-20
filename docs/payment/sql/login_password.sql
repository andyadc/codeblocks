SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for login_password
-- 2018-3-23
-- ----------------------------

DROP TABLE IF EXISTS `login_password`;
CREATE TABLE `login_password`
(
    `key`               BIGINT(20) NOT NULL COMMENT '内部主键，自增型',
    `user_id`           BIGINT(20) NOT NULL COMMENT '用户id',
    `password_type`     TINYINT(4)          DEFAULT NULL COMMENT '密码类型',
    `password_status`   TINYINT(4)          DEFAULT NULL COMMENT '密码状态',
    `enc_password`      CHAR(32)            DEFAULT NULL COMMENT '密码密文',
    `init_password`     TINYINT(4)          DEFAULT NULL COMMENT '初始密码标志',
    `is_forced_change`  TINYINT(4)          DEFAULT NULL COMMENT '是否强制修改密码',
    `locked_time`       DATETIME            DEFAULT NULL COMMENT '锁定时间',
    `locked_reason`     VARCHAR(255)        DEFAULT NULL COMMENT '锁定原因',
    `last_updated_time` DATETIME            DEFAULT NULL COMMENT '最后修改时间',
    `version`           INT(12)    NOT NULL DEFAULT '1' COMMENT '版本号',
    `create_time`       DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`key`),
    UNIQUE KEY `idx_user_id` (`user_id`) USING BTREE
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='用户登陆密码';
