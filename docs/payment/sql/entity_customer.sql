SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for entity_customer
-- 2018-3-23
-- ----------------------------
DROP TABLE IF EXISTS `entity_customer`;
CREATE TABLE `entity_customer`
(
    `key`              BIGINT(20) UNSIGNED NOT NULL COMMENT '内部主键，自增型',
    `id`               BIGINT(20) UNSIGNED NOT NULL COMMENT '客户ID',
    `full_name`        VARCHAR(50)                  DEFAULT NULL,
    `gender`           TINYINT(4)                   DEFAULT NULL COMMENT '性别代码',
    `auth_state`       TINYINT(4)                   DEFAULT NULL COMMENT '实名状态',
    `auth_time`        DATETIME                     DEFAULT NULL COMMENT '实名认证时间 (最后一次客户认证时间)',
    `canceled_date`    DATETIME                     DEFAULT NULL COMMENT '销户日期',
    `canceled_reason`  VARCHAR(255)                 DEFAULT NULL COMMENT '销户原因',
    `user_count`       INT(11)                      DEFAULT '0' COMMENT '用户数',
    `valid_user_count` INT(11)                      DEFAULT '0' COMMENT '有效用户数',
    `version`          INT(11)             NOT NULL DEFAULT '1' COMMENT '版本号',
    `create_time`      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`key`),
    UNIQUE KEY `idx_cust_id` (`id`) USING BTREE
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='个人客户基本信息(实体)';
