CREATE TABLE `link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `logo` varchar(256) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL COMMENT '网站地址',
  `status` char(1) DEFAULT '2' COMMENT '审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='友链';

insert  into `link`(`id`,`name`,`logo`,`description`,`address`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`)
values (1,'百度','https://p5.itc.cn/images01/20211125/c2ea35c74de1465082d02bc2f37bc2b9.jpeg','百度一下，生活更好','https://www.baidu.com','1',NULL,'2023-12-12 17:18:37',NULL,'2023-12-25 14:16:45',0),
       (2,'淘宝','https://img0.baidu.com/it/u=2607889200,3343962549&fm=253&fmt=auto&app=138&f=PNG?w=500&h=500','太好逛了吧','https://www.taobao.com/','1',NULL,'2023-12-10 17:14:23',NULL,'2023-12-25 14:16:59',0),
       (3,'小红书','https://x0.ifengimg.com/res/2022/73A5DE510304E1EB66FC5FE978D5407D07469465_size45_w729_h455.png','标记我的生活','https://www.xiaohongshu.com/','1','1','2023-12-18 14:48:42','1','2023-12-25 14:17:27',0);
