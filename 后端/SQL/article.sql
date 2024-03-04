create database blog;
use blog;
CREATE TABLE `article` (
  `id` bigint(200) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) DEFAULT NULL COMMENT '标题',
  `content` longtext COMMENT '文章内容',
  `summary` varchar(1024) DEFAULT NULL COMMENT '文章摘要',
  `category_id` bigint(20) DEFAULT NULL COMMENT '所属分类id',
  `thumbnail` varchar(256) DEFAULT NULL COMMENT '缩略图',
  `is_top` char(1) DEFAULT '0' COMMENT '是否置顶（0否，1是）',
  `status` char(1) DEFAULT '1' COMMENT '状态（0已发布，1草稿）',
  `view_count` bigint(200) DEFAULT '0' COMMENT '访问量',
  `is_comment` char(1) DEFAULT '1' COMMENT '是否允许评论 1是，0否',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

insert  into `article`(`id`,`title`,`content`,`summary`,`category_id`,`thumbnail`,`is_top`,`status`,`view_count`,`is_comment`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`)
values (1,'不必借光而行，你我亦是星辰','# 时光如白驹过隙，草木眨眼已过半生。
 行走在人生路上，看过多少人世沧桑，繁华落尽。有人曾说，每个日子都是有意义的，所以值得铭记于心。身处纷杂世界，很多时容易迷失自己，但请记住，你我是独一无二的自己，是世间万物不可复制的生命。
 ### 不必借光而行，你我亦是星辰。
 ![不必借光而行.jpg](http://s5nolp90r.hn-bkt.clouddn.com/2023/12/19/faa820ef66124ffab81282a297e90fd2.jpg)
 人生海海，山山而川，虽说你我只是沧海一粟，但你我都有各自人生轨迹要运转。在追梦上，你我怀着最纯真美好的念想，去逐风挽浪，披荆斩棘，圆心中的梦。或许孤独而漫长，但是追梦的人儿，永远有颗不服输的心，哪怕惊涛骇浪，也要迎风浪而去。','时光如白驹过隙，草木眨眼已过半生。行走在人生路上，看过多少人世沧桑，繁华落尽。有人曾说，每个日子都是有意义的，所以值得铭记于心。身处纷杂的世界，很多时候容易迷失自己，但请记住，你我是独一无二的自己，是世间万物不可复制的生命。不必借光而行，你我亦是星辰。',1,'http://s5nolp90r.hn-bkt.clouddn.com/2023/12/19/78fdf6dd6c4948e5b94559b92a774ab5.jpg','1','0',221,'0',NULL,'2023-12-08 23:20:11',NULL,NULL,0),
       (2,'weq','adadaeqe','adad',2,'https://sg-blog-oss.oss-cn-beijing.aliyuncs.com/2022/01/15/fd2e9460c58a4af3bbeae5d9ed581688.png','1','0',22,'0',NULL,'2022-01-21 14:58:30',NULL,NULL,1),
       (3,'dad','asdasda','sadad',1,'https://sg-blog-oss.oss-cn-beijing.aliyuncs.com/2022/01/15/737a0ed0b8ea430d8700a12e76aa1cd1.png','1','0',33,'0',NULL,'2022-01-18 14:58:34',NULL,NULL,1),
       (5,'今日心情日记','![心情日记正文图片.jpg](http://s5nolp90r.hn-bkt.clouddn.com/2023/12/19/e5a3fde3971e45fda63f5a9318516b9a.jpg)','我说不清楚',2,'http://s5nolp90r.hn-bkt.clouddn.com/2023/12/19/27876b8cb0d94880be152fc54c50a798.jpg','1','0',44,'0',NULL,'2023-12-17 14:58:37',NULL,NULL,0);
