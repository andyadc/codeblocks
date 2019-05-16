SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS `pay_contract_account_0`;
CREATE TABLE `pay_contract_account_0` (
  `key`                 BIGINT(20) NOT NULL
  COMMENT '主键',
  `id`                  BIGINT(20) NOT NULL
  COMMENT '账户表主键',
  `create_time`         DATETIME            DEFAULT NULL,
  `update_time`         DATETIME            DEFAULT NULL,
  `status`              INT(11)             DEFAULT 0
  COMMENT '签约状态',
  `version`             INT(11)             DEFAULT NULL
  COMMENT '版本号',

  `company_id`          BIGINT(20)          DEFAULT NULL
  COMMENT '签约公司代码，参见Company表',
  `owner_id`            BIGINT(20) NOT NULL
  COMMENT '用户id',
  `type`                TINYINT(2) NOT NULL DEFAULT '2'
  COMMENT '账户类型',
  `order_id`            BIGINT(20) NOT NULL
  COMMENT '签约并支付时关联的订单号',
  `fee_unit`            TINYINT(4) NOT NULL DEFAULT '1'
  COMMENT '货币类型 1、人民币 2、积分 3、代金券',

  `contract_account`    VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT '签约账号，如银行卡号，第三方平台的账户号',
  `mobile_number`       VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT '银行预留手机号',
  `pin`                 VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT '预留的身份证号',
  `real_name`           VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT '真实姓名',
  `expiration_date`     VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT '过期时间',
  `security_code`       VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT '安全码',
  `contract_token`      VARCHAR(256) CHARACTER SET utf8
  COLLATE utf8_bin                          DEFAULT NULL
  COMMENT 'token内容或签约号',
  `contract_service_id` BIGINT(20)          DEFAULT NULL
  COMMENT '签约使用的PaymentService id',
  `contract_channel_id` BIGINT(20)          DEFAULT NULL
  COMMENT '签约时使用的通道',

  PRIMARY KEY (`key`),
  KEY `idx_conacccon_id` (`id`),
  KEY `idx_conacccon_owner_id` (`owner_id`),
  KEY `idx_conacccon_cont_acc` (`contract_account`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `pay_contract_account_1`;
CREATE TABLE `pay_contract_account_1`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_2`;
CREATE TABLE `pay_contract_account_2`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_3`;
CREATE TABLE `pay_contract_account_3`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_4`;
CREATE TABLE `pay_contract_account_4`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_5`;
CREATE TABLE `pay_contract_account_5`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_6`;
CREATE TABLE `pay_contract_account_6`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_7`;
CREATE TABLE `pay_contract_account_7`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_8`;
CREATE TABLE `pay_contract_account_8`
  LIKE `pay_contract_account_0`;

DROP TABLE IF EXISTS `pay_contract_account_9`;
CREATE TABLE `pay_contract_account_9`
  LIKE `pay_contract_account_0`;