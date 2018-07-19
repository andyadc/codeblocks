SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for entity_user
-- 2018-3-23
-- ----------------------------

DROP TABLE IF EXISTS `entity_user`;
CREATE TABLE `entity_user` (
  `key`              BIGINT(20)   NOT NULL
  COMMENT '内部主键，自增型',
  `id`               BIGINT(20)   NOT NULL
  COMMENT '用户id',
  `user_name`        VARCHAR(128) NOT NULL
  COMMENT '用户名',
  `nickname`         VARCHAR(128) COMMENT '昵称',
  `status`           TINYINT(4)   NOT NULL DEFAULT '0'
  COMMENT '用户状态',
  `security_phone`   VARCHAR(32)           DEFAULT NULL
  COMMENT '安全手机',
  `security_email`   VARCHAR(128)          DEFAULT NULL
  COMMENT '安全邮箱',
  `identified`       TINYINT(4)            DEFAULT 0
  COMMENT '用户是否已经识别为客户',
  `security_level`   TINYINT(4)            DEFAULT 0
  COMMENT '用户安全等级',
  `is_trusted`       TINYINT(4)            DEFAULT 0
  COMMENT '是否托管账户',
  `customer_id`      BIGINT(20) COMMENT '对应的客户id',
  `register_date`    DATETIME              DEFAULT NULL
  COMMENT '开户日期',
  `activated_date`   DATETIME              DEFAULT NULL
  COMMENT '激活日期',
  `cancelled_date`   DATETIME              DEFAULT NULL
  COMMENT '销户日期',
  `cancelled_reason` VARCHAR(255)          DEFAULT NULL
  COMMENT '销户原因',
  `register_channel` TINYINT(4)            DEFAULT NULL
  COMMENT '注册渠道',
  `register_info`    VARCHAR(255)          DEFAULT NULL
  COMMENT '预留信息',
  `last_login_ip`    VARCHAR(32)           DEFAULT NULL
  COMMENT '最后登录IP',
  `last_login_time`  DATETIME              DEFAULT NULL
  COMMENT '最后登录时间',
  `version`          INT(11)      NOT NULL DEFAULT '1'
  COMMENT '版本号',
  `created_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `updated_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`key`),
  UNIQUE KEY `idx_user_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_username` (`user_name`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户基本信息(实体)';

-- ----------------------------
-- Table structure for user_payment_password
-- ----------------------------
DROP TABLE IF EXISTS `user_payment_password`;
CREATE TABLE `user_payment_password` (
  `key`           BIGINT(20)  NOT NULL
  COMMENT '内部主键，自增型',
  `user_id`       BIGINT(20)  NOT NULL
  COMMENT '用户id',
  `password_type` TINYINT(4)  NOT NULL DEFAULT 1
  COMMENT '密码类型',
  `locked_time`   DATETIME COMMENT '密码被锁定时间',
  `locked_reason` VARCHAR(128) COMMENT '锁定原因',
  `salt`          VARCHAR(8)           DEFAULT NULL
  COMMENT '密码加密用的盐值',
  `password`      VARCHAR(32) NOT NULL
  COMMENT '加密后密码',
  `is_init`       CHAR(1)              DEFAULT 0
  COMMENT '是否初始密码',
  `is_changed`    CHAR(1)              DEFAULT 0
  COMMENT '是否变更了密码',
  `version`       INT(11)     NOT NULL DEFAULT '1'
  COMMENT '版本号',
  `created_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `updated_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`key`),
  UNIQUE KEY `idx_upp_user_id` (`user_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户支付密码';

-- ----------------------------
-- Table structure for user_login_password
-- ----------------------------
DROP TABLE IF EXISTS `user_login_password`;
CREATE TABLE `user_login_password` (
  `key`           BIGINT(20)  NOT NULL
  COMMENT '内部主键，自增型',
  `user_id`       BIGINT(20)  NOT NULL
  COMMENT '用户id',
  `password_type` TINYINT(4)  NOT NULL DEFAULT 1
  COMMENT '密码类型',
  `locked_time`   DATETIME COMMENT '密码被锁定时间',
  `locked_reason` VARCHAR(128) COMMENT '锁定原因',
  `salt`          VARCHAR(8)           DEFAULT NULL
  COMMENT '密码加密用的盐值',
  `password`      VARCHAR(32) NOT NULL
  COMMENT '加密后密码',
  `is_init`       CHAR(1)              DEFAULT 0
  COMMENT '是否初始密码',
  `is_changed`    CHAR(1)              DEFAULT 0
  COMMENT '是否变更了密码',
  `version`       INT(11)     NOT NULL DEFAULT '1'
  COMMENT '版本号',
  `created_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `updated_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`key`),
  UNIQUE KEY `idx_upp_user_id` (`user_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户登录密码';