SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_account_0
-- ----------------------------
DROP TABLE IF EXISTS `pay_account_0`;
CREATE TABLE `pay_account_0`
(
    `key`           BIGINT(20) UNSIGNED NOT NULL COMMENT '账户ID，内部主键',
    `id`            BIGINT(20) UNSIGNED NOT NULL COMMENT '账户号，实际主键',
    `owner_id`      BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '用户ID',
    `order_id`      BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '签约并支付时关联的订单号',
    `type`          TINYINT(2)          NOT NULL DEFAULT '2' COMMENT '账户类型',
    `account_title` INT(12)             NOT NULL DEFAULT '0' COMMENT '会计科目代码',
    `fee_unit`      TINYINT(4)          NOT NULL DEFAULT '1' COMMENT '货币类型 1、人民币 2、积分 3、代金券 4、美元 5、台币',
    `third_type`    INT(8)              NOT NULL DEFAULT '0' COMMENT '第三方渠道ID，pay_partner表维护',
    `third_account` VARCHAR(32)         NOT NULL DEFAULT '' COMMENT '第三方的用户账户',
    `third_param`   VARCHAR(1024)       NOT NULL DEFAULT '' COMMENT '第三方凭证信息，通用字段，每个渠道区别维护',
    `balance`       BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '余额',
    `frozen`        BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '冻结金额',
    `income`        BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '入账总额',
    `outcome`       BIGINT(20)          NOT NULL DEFAULT '0' COMMENT '出账总额',
    `status`        TINYINT(2)          NOT NULL DEFAULT '0' COMMENT '状态：0：创建；1、激活；2：冻结；3：销毁',
    `rank`          TINYINT(2)          NOT NULL DEFAULT '0' COMMENT '账户等级',
    `notification`  TINYINT(2)          NOT NULL DEFAULT '0' COMMENT '通知方式',
    `permissions`   BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '账户权限，可以bit位扩展',
    `risk_level`    TINYINT(2)          NOT NULL DEFAULT '0' COMMENT '安全等级',
    `sandbox`       TINYINT(2)          NOT NULL DEFAULT '0' COMMENT '沙盒账户：0：否；1：是',
    `version`       BIGINT(12)          NOT NULL DEFAULT '1' COMMENT '版本号',
    `create_time`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`key`),
    UNIQUE KEY `idx_account_id` (`id`) USING BTREE,
    KEY `idx_account_owner_id` (`owner_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `pay_account_1`;
CREATE TABLE `pay_account_1`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_2`;
CREATE TABLE `pay_account_2`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_3`;
CREATE TABLE `pay_account_3`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_4`;
CREATE TABLE `pay_account_4`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_5`;
CREATE TABLE `pay_account_5`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_6`;
CREATE TABLE `pay_account_6`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_7`;
CREATE TABLE `pay_account_7`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_8`;
CREATE TABLE `pay_account_8`
    LIKE `pay_account_0`;

DROP TABLE IF EXISTS `pay_account_9`;
CREATE TABLE `pay_account_9`
    LIKE `pay_account_0`;
