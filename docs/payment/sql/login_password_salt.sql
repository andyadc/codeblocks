SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for login_password_salt
-- 2018-3-23
-- ----------------------------

DROP TABLE IF EXISTS `login_password_salt`;
CREATE TABLE `login_password_salt`
(
    `key`         BIGINT(20) NOT NULL COMMENT '内部主键，自增型',
    `user_id`     BIGINT(20) NOT NULL COMMENT '用户id',
    `salt`        VARCHAR(50)         DEFAULT NULL COMMENT '加密盐值',
    `version`     INT(12)    NOT NULL DEFAULT '1' COMMENT '版本号',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`key`),
    UNIQUE KEY `idx_user_id` (`user_id`) USING BTREE
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='用户登陆密码盐值表（将密文与盐值分离存储）';
