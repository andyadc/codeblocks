SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sms_verify
-- 2018-3-23
-- ----------------------------
DROP TABLE IF EXISTS `sms_verify`;
CREATE TABLE `sms_verify`
(
    `key`             BIGINT(20)  NOT NULL COMMENT '内部主键，自增型',
    `id`              BIGINT(20)  NOT NULL COMMENT '短息验证码信息ID ',
    `receiver_no`     VARCHAR(11) NOT NULL COMMENT '目标手机号码',
    `receipt_code`    VARCHAR(6)  NOT NULL COMMENT '验证码回执编号',
    `verify_code`     VARCHAR(6)  NOT NULL COMMENT '短信验证码',
    `auth_status`     TINYINT(4)  NOT NULL DEFAULT '0' COMMENT '鉴权状态',
    `pre_auth_status` TINYINT(4)  NOT NULL DEFAULT '0' COMMENT '预鉴权状态',
    `expiration`      DATETIME    NOT NULL COMMENT '验证码的有效截止时间',
    `msg_type`        TINYINT(4)  NOT NULL COMMENT '短信类型',
    `sys_id`          TINYINT(4)  NOT NULL COMMENT '请求系统编码',
    `version`         INT(11)              DEFAULT '1' COMMENT '版本号',
    `create_time`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`key`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='短信验证码信息表';
