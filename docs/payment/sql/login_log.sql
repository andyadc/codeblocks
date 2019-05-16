SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for login_log
-- 2018-3-23
-- ----------------------------

DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `key`             BIGINT(20) NOT NULL
  COMMENT '内部主键，自增型',
  `login_cert_type` TINYINT(4)          DEFAULT NULL
  COMMENT '登录标识类型',
  `login_cert_code` VARCHAR(50)         DEFAULT NULL
  COMMENT '登录时使用的凭证（手机号、邮箱、昵称）',
  `user_id`         BIGINT(20) NOT NULL
  COMMENT '用户id',
  `version`         INT(11)    NOT NULL DEFAULT '1'
  COMMENT '版本号',
  `created_time`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `updated_time`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`key`),
  UNIQUE KEY `idx_user_id` (`user_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='用户登陆标识';