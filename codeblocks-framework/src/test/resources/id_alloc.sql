DROP TABLE IF EXISTS `id_alloc`;

CREATE TABLE `id_alloc` (
  `biz_tag` varchar(128) NOT NULL DEFAULT '',
  `max_id` bigint(20) unsigned NOT NULL DEFAULT '1',
  `step` int(11) NOT NULL,
  `description` varchar(256)  DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
;