CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
  `nick_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
  `password` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
  `type` char(1) DEFAULT '0' COMMENT '用户类型：0代表普通用户，1代表管理员',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(11) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

insert  into `user`(`id`,`user_name`,`nick_name`,`password`,`type`,`status`,`email`,`sex`,`avatar`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`)
values (1,'denglinsha','denglinsha','$2a$10$VcIamfDZIvkRP1JJZKYAHOZpsb4Z3LZptJACS9wur9mZoOpTMpsAO','1','0','624919@qq.com',,'1','http://s5nolp90r.hn-bkt.clouddn.com/2023/12/19/9a88baf07c8145e88b464f4201b9b429.jpg',NULL,'2023-12-05 09:01:56',1,'2023-12-05 15:37:03',0),
       (3,'test','测试','$2a$10$v9QNSgh6rMgjwuvmgwWJG.wxPWfARyBk/c8oDDQU4JbXI2ufIOyXu','1','0','123456@qq.com',NULL,'http://s5nolp90r.hn-bkt.clouddn.com/2023/12/19/875a5e20e7ee4a0ea389c4f93c156d2c.png',NULL,'2023-12-05 13:28:43',NULL,'2023-12-05 13:28:43',0),
       (4,'小明','小明','$2a$10$Cjxu8UwfmUYvgzy7VJexke3suuKNM9bwy8ENHj4UEzBmMZX5p.OBm','1','0','23412332@qq.com','0',NULL,NULL,NULL,NULL,NULL,0),
       (5,'小红','小红','$2a$10$Cjxu8UwfmUYvgzy7VJexke3suuKNM9bwy8ENHj4UEzBmMZX5p.OBm','1','0','123@qq.com','1',NULL,NULL,'2023-12-05 09:51:13',NULL,'2023-12-05 10:00:50',0),
       (6,'小刚','小刚','$2a$10$Cjxu8UwfmUYvgzy7VJexke3suuKNM9bwy8ENHj4UEzBmMZX5p.OBm','1','0','123@qq.com','0',NULL,NULL,'2023-12-05 09:54:26',NULL,'2023-12-05 10:06:34',0),
       (14787164048662,'weixin','weixin','$2a$10$y3k3fnMZsBNihsVLXWfI8uMNueVXBI08k.LzWYaKsW8CW7xXy18wC','0','0','weixin@qq.com',NULL,NULL,-1,'2023-12-05 17:18:44',-1,'2023-12-05 17:18:44',0);
