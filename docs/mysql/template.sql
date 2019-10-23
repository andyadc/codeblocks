SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- <comment>
-- <date>
-- ----------------------------

DROP TABLE IF EXISTS `t_template`;
CREATE TABLE `t_template` (
  `id`               BIGINT(20) unsigned  NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name`             VARCHAR(128) NOT NULL COMMENT 'name',
  `status`           TINYINT(4)   NOT NULL DEFAULT '0' COMMENT 'status',
  `birthday`         DATE  DEFAULT NULL COMMENT 'birthday',
  `version`          INT(11)  NOT NULL DEFAULT '1' COMMENT 'version',
  `create_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
  `update_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update_time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci
  COMMENT ='xxoo';
