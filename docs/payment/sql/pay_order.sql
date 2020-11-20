SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_order_0
-- ----------------------------
DROP TABLE IF EXISTS `pay_order_0`;
CREATE TABLE `pay_order_0`
(
    `key`                BIGINT(20)   NOT NULL COMMENT '内部主键，自增型',
    `id`                 BIGINT(20)   NOT NULL COMMENT '交易流水号，用于分表分库',
    `version`            INT(11)      NOT NULL DEFAULT '0',
    `app_id`             VARCHAR(64)  NOT NULL COMMENT '发起交易的app id',
    `pay_scenarios`      INT(4)       NOT NULL COMMENT '发起交易的场景',
    `notify_url`         VARCHAR(255) NOT NULL COMMENT '异步通知url',
    `return_url`         VARCHAR(255) NOT NULL COMMENT '同步通知url',
    `status`             INT(5)       NOT NULL COMMENT '订单状态',
    `create_time`        DATETIME     NOT NULL,
    `update_time`        DATETIME     NOT NULL,
    `expire_time`        DATETIME     NOT NULL COMMENT '过期时间',
    `pay_time`           DATETIME     NOT NULL COMMENT '支付时间',
    `error_code`         VARCHAR(32)  NOT NULL COMMENT '如果支付失败，这里需记录错误码',
    `error_detail`       VARCHAR(128) NOT NULL COMMENT '详细错误信息，如果支付失败，需要在这里记录错误信息',
    `partner_order_no`   VARCHAR(64)  NOT NULL COMMENT '接入方订单号',
    `pay_mode`           TINYINT(2)   NOT NULL COMMENT '0.正常订单，1.代表测试订单',
    `order_id`           VARCHAR(32)  NOT NULL COMMENT '订单ID，记录用户购买的产品信息（需使用产品查询接口）',
    `order_title`        VARCHAR(62)  NOT NULL COMMENT '订单名称',
    `order_detail`       VARCHAR(128) NOT NULL COMMENT '订单描述',
    `order_show_url`     VARCHAR(256) NOT NULL COMMENT '订单显示的url地址',
    `fee`                INT(9)       NOT NULL COMMENT '预期金额',
    `fee_real`           INT(9)       NOT NULL COMMENT '实际支付总金额',
    `fee_unit`           TINYINT(2)   NOT NULL DEFAULT '1' COMMENT '货币类型 ',
    `sub_id`             VARCHAR(32)  NOT NULL COMMENT '交易主体不仅仅是用户，也可以是商户',
    `sub_type`           INT(9)       NOT NULL COMMENT '交易主体类型',
    `sub_name`           VARCHAR(32)  NOT NULL COMMENT '交易主体名称',
    `sub_account_id`     VARCHAR(32)  NOT NULL COMMENT '交易主体账号',
    `sub_account_type`   INT(10)      NOT NULL COMMENT '交易主体使用的账号类型；',
    `sub_ip`             VARCHAR(32)  NOT NULL COMMENT '交易主体ip地址',
    `sub_mobile`         VARCHAR(32)  NOT NULL COMMENT '交易主体使用的手机号',
    `sub_client_code`    VARCHAR(16)  NOT NULL COMMENT '交易主体的客户端代码，发起交易的平台代码，参考公司标准代码规范',
    `sub_device_id`      VARCHAR(32)  NOT NULL COMMENT '交易主体使用的设备',
    `sub_email`          VARCHAR(128) NOT NULL COMMENT '交易主体的邮箱【预留】',
    `sub_location`       VARCHAR(128) NOT NULL COMMENT '交易主体在交易发生时所在的位置【预留】',
    `sub_country_code`   VARCHAR(128) NOT NULL COMMENT '交易主体所在的国家代码，默认为中国【预留】',
    `partner_id`         INT(9)       NOT NULL COMMENT '交易对手的Id,接入方ID',
    `partner_type`       INT(9)       NOT NULL COMMENT '交易对手的类型',
    `partner_account_id` INT(9)       NOT NULL COMMENT '交易对手的账号,商户号pay_service_account的ID',
    `partner_name`       VARCHAR(50)  NOT NULL COMMENT '交易对手的名称',
    `source_pay_type`    INT(9)       NOT NULL COMMENT '用户选择的交易渠道实体编码',
    `dest_pay_type`      INT(9)       NOT NULL COMMENT '实际执行的交易渠道虚拟账户编码',
    `third_create_time`  DATETIME     NOT NULL COMMENT '第三方创建时间',
    `third_pay_time`     DATETIME     NOT NULL COMMENT '第三方创建时间',
    `third_trade_code`   VARCHAR(50)  NOT NULL COMMENT '第三方交易订单号',
    `current_key`        INT(9)       NOT NULL,
    PRIMARY KEY (`key`),
    UNIQUE KEY `idx_pay_order_id` (`id`) USING BTREE,
    KEY `idx_pay_order_sub_id` (`sub_id`),
    KEY `idx_pay_order_partner_id` (`partner_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `pay_order_1`;
CREATE TABLE `pay_order_1`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_2`;
CREATE TABLE `pay_order_2`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_3`;
CREATE TABLE `pay_order_3`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_4`;
CREATE TABLE `pay_order_4`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_5`;
CREATE TABLE `pay_order_5`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_6`;
CREATE TABLE `pay_order_6`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_7`;
CREATE TABLE `pay_order_7`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_8`;
CREATE TABLE `pay_order_8`
    LIKE `pay_order_0`;

DROP TABLE IF EXISTS `pay_order_9`;
CREATE TABLE `pay_order_9`
    LIKE `pay_order_0`;
