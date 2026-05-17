-- Create syntax for TABLE 'content_blogpost'
CREATE TABLE `content_blogpost` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `introduction` varchar(255) DEFAULT NULL COMMENT '文章简介',
  `cover` varchar(255) DEFAULT NULL COMMENT '文章封面地址',
  `classify_id` int NOT NULL COMMENT '分类id',
  `column_id` int DEFAULT NULL COMMENT '专栏id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `read_type` int DEFAULT '1' COMMENT '阅读方式:1 免费阅读 2 付费阅读 3 会员阅读 4 私有 5 登录阅读',
  `is_stick` int DEFAULT '2' COMMENT '是否置顶:1 置顶 2 不置顶',
  `state` int DEFAULT '4' COMMENT '状态: 1 上架 2 下架 3 草稿 4 预审 5 已退回 6 已下线 7 已归档 8 删除',
  `is_original` int DEFAULT '2' COMMENT '是否原创:  1 转载 2 原创',
  `reprint_address` varchar(255) DEFAULT NULL COMMENT '转载地址url',
  `is_recommend` int DEFAULT '2' COMMENT '是否推荐: 1 推荐 2 不推荐',
  `keywords` varchar(150) DEFAULT NULL COMMENT '关键词',
  `readership` int DEFAULT '0' COMMENT '阅读量',
  `user_source` int DEFAULT '1' COMMENT '创建者来源 1: 前端 2：后台管理者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000005 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章';

-- Create syntax for TABLE 'content_blogpost_label'
CREATE TABLE `content_blogpost_label` (
  `blogpost_id` bigint NOT NULL COMMENT '博客文章id',
  `label_id` bigint NOT NULL COMMENT '内容标签id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`blogpost_id`,`label_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章标签关联表';

-- Create syntax for TABLE 'content_blogpost_like'
CREATE TABLE `content_blogpost_like` (
  `blogpost_id` bigint NOT NULL COMMENT '博客文章id',
  `user_id` bigint NOT NULL COMMENT '点赞用户id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`blogpost_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章用户点赞表';

-- Create syntax for TABLE 'content_classify'
CREATE TABLE `content_classify` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  `state` int DEFAULT '1' COMMENT '状态：1 正常 2 禁用 3 删除',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='内容类型表';

-- Create syntax for TABLE 'content_column'
CREATE TABLE `content_column` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专栏名称',
  `state` int DEFAULT '2' COMMENT '状态: 1 上架 2 预审 3 已下线 4 删除 5 已退回',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '专栏封面',
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专栏简介',
  `user_id` int NOT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='内容专栏表';

-- Create syntax for TABLE 'content_comments'
CREATE TABLE `content_comments` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `blogpost_id` int DEFAULT NULL COMMENT '关联文章id',
  `moments_id` int DEFAULT NULL COMMENT '关联说说id',
  `parent_id` int DEFAULT '0' COMMENT '父评论id(0表示顶级评论)',
  `reply_to_id` int DEFAULT '0' COMMENT '被回复评论id(0表示直接评论文章)',
  `user_id` int NOT NULL COMMENT '评论人id',
  `content` text NOT NULL COMMENT '评论内容',
  `status` int DEFAULT '1' COMMENT '评论状态 1:正常 2:待审核 3:已屏蔽',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_article_status_created` (`blogpost_id`,`moments_id`,`status`,`create_time`),
  KEY `idx_parent_reply` (`parent_id`,`reply_to_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';

-- Create syntax for TABLE 'content_comments_like'
CREATE TABLE `content_comments_like` (
  `comment_id` bigint NOT NULL COMMENT '评论id',
  `user_id` bigint NOT NULL COMMENT '点赞用户id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`comment_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create syntax for TABLE 'content_label'
CREATE TABLE `content_label` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `state` int DEFAULT '1' COMMENT '状态: 1 正常 2 禁用',
  `type` int NOT NULL COMMENT '所属类型：1 博客 2 说说',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100013 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='内容标签表';

-- Create syntax for TABLE 'content_moments'
CREATE TABLE `content_moments` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int NOT NULL COMMENT '用户id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '说说内容',
  `image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '图片URL列表，用逗号分隔',
  `video_url` varchar(500) DEFAULT NULL COMMENT '视频URL',
  `privacy` int DEFAULT '1' COMMENT '隐私: 1 公开 2 好友 3 私密',
  `state` int DEFAULT '4' COMMENT '状态: 1 发布 2 删除 3 草稿 4 违规',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地理位置',
  `user_source` int DEFAULT '1' COMMENT '创建者来源 1: 前端 2：后台管理者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000014 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='说说-记录下的瞬间';

-- Create syntax for TABLE 'content_moments_label'
CREATE TABLE `content_moments_label` (
  `moments_id` bigint NOT NULL COMMENT '说说id',
  `label_id` bigint NOT NULL COMMENT '内容标签id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`moments_id`,`label_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='说说标签关联表';

-- Create syntax for TABLE 'files_record'
CREATE TABLE `files_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件访问url',
  `stored_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件存储名称',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件原始名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件在服务器或云存储中的完整路径',
  `file_ext` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '扩展名',
  `file_size` int NOT NULL COMMENT '文件大小（单位：字节），用于验证和统计',
  `user_id` int NOT NULL COMMENT '关联上传用户的ID',
  `upload_id` varchar(100) DEFAULT NULL COMMENT '上传ID，仅在分片上传使用',
  `user_platform` int DEFAULT NULL COMMENT '1 后台管理 2 web端 3 微信小程序 5 支付宝小程序 6 app ',
  `file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000067 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件详细记录表';

-- Create syntax for TABLE 'stat_access_log'
CREATE TABLE `stat_access_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `request_ip` varchar(45) NOT NULL COMMENT '用户IP',
  `device_id` varchar(64) DEFAULT NULL COMMENT '前端生成的设备UUID',
  `page_path` varchar(255) NOT NULL COMMENT '访问路径',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '标识',
  `terminal_type` varchar(20) DEFAULT 'PC' COMMENT '终端类型: PC, H5, APP, MINI_PROGRAM',
  `access_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  PRIMARY KEY (`id`),
  KEY `idx_access_time` (`access_time`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000073 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='访问日志明细';

-- Create syntax for TABLE 'stat_daily_traffic'
CREATE TABLE `stat_daily_traffic` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `stat_date` date NOT NULL COMMENT '统计日期',
  `page_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '__global__' COMMENT '页面路径，__global__代表全站',
  `pv` bigint DEFAULT '0' COMMENT '页面访问量',
  `uv` bigint DEFAULT '0' COMMENT '独立访客数',
  `ip_count` int DEFAULT '0' COMMENT '独立IP数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date_path` (`stat_date`,`page_path`)
) ENGINE=InnoDB AUTO_INCREMENT=1000007 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='每日流量统计';

-- Create syntax for TABLE 'sys_cloud_mail'
CREATE TABLE `sys_cloud_mail` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `access_key_id` varchar(128) NOT NULL COMMENT 'AccessKeyId',
  `access_key_secret` varchar(128) NOT NULL COMMENT 'AccessKeySecret',
  `account_name` varchar(128) NOT NULL COMMENT '发信地址',
  `from_alias` varchar(64) DEFAULT NULL COMMENT '发信人昵称',
  `tag_name` varchar(64) DEFAULT NULL COMMENT '邮件标签',
  `region_id` varchar(32) DEFAULT 'cn-hangzhou' COMMENT '地域ID',
  `template` text COMMENT '邮件HTML模板，支持 {code} {time} 等占位符',
  `template_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '云模板code',
  `status` tinyint DEFAULT '1' COMMENT '状态：1启用 2禁用',
  `valid_time` int DEFAULT NULL COMMENT '有效时间，单位秒',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='云邮箱配置';

-- Create syntax for TABLE 'sys_config'
CREATE TABLE `sys_config` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `config_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
  `config_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置类型',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置的键',
  `config_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置的值',
  `config_sort` int DEFAULT '1' COMMENT '排序',
  `config_state` int DEFAULT '1' COMMENT '状态 1 正常  2禁用',
  `config_restrict` varchar(150) DEFAULT NULL COMMENT '约束',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10022 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配置表';

-- Create syntax for TABLE 'sys_config_mail'
CREATE TABLE `sys_config_mail` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SMTP服务器地址',
  `port` int NOT NULL COMMENT 'SMTP端口',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱密码或授权码',
  `protocol` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '协议类型',
  `smtp_auth` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份验证',
  `smtp_ssl_enable` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SSL加密',
  `smtp_starttls_enable` varchar(10) DEFAULT NULL COMMENT 'TLS加密',
  `debug` varchar(10) DEFAULT NULL COMMENT '调试模式，生产环境可设为false',
  `connection_timeout` varchar(10) DEFAULT NULL COMMENT '连接超时时间（毫秒）',
  `timeout` varchar(10) DEFAULT NULL COMMENT '取超时时间（毫秒）',
  `write_timeout` varchar(10) DEFAULT NULL COMMENT '写入超时时间（毫秒）',
  `state` int NOT NULL COMMENT '邮箱状态： 1 启用，2 关闭，3删除',
  `valid_time` int DEFAULT '3600' COMMENT '有效时间，单位秒',
  `template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '邮件HTML模板，支持 {code} {time} 等占位符',
  `subject` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主题',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='邮箱配置';

-- Create syntax for TABLE 'sys_log'
CREATE TABLE `sys_log` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户ID（游客为0）',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（游客为随机生成名称）',
  `api_path` varchar(200) NOT NULL COMMENT 'API路径',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `address` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
  `device` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作内容',
  `user_source` int DEFAULT '1' COMMENT '创建者来源 1: 前端 2：后台管理者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_api_path` (`api_path`),
  KEY `idx_ip` (`ip`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1000000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志';

-- Create syntax for TABLE 'sys_menu'
CREATE TABLE `sys_menu` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(200) DEFAULT NULL COMMENT 'icon',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识符',
  `path` varchar(255) DEFAULT NULL COMMENT 'url',
  `router` varchar(200) DEFAULT NULL COMMENT '路由',
  `component` varchar(200) DEFAULT NULL COMMENT '组件',
  `sort` int DEFAULT '0' COMMENT '排序',
  `is_external` int DEFAULT '2' COMMENT '外链： 1 是 2 否',
  `parent_id` bigint DEFAULT '0' COMMENT '父级id',
  `type` int NOT NULL COMMENT '类型 1 directory 2 menu 3 permission',
  `state` int DEFAULT '1' COMMENT '状态 1 正常 2 禁用',
  `description` varchar(50) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10046 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统菜单';

-- Create syntax for TABLE 'sys_menu_icon'
CREATE TABLE `sys_menu_icon` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'icon 名称',
  `icon_key` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'icon key 值',
  `sort` int DEFAULT '999' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10010 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create syntax for TABLE 'sys_user'
CREATE TABLE `sys_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名：为5-16个大小写字母，唯一',
  `real_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 组成',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱：唯一',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `sex` int DEFAULT NULL COMMENT '性别：1 男，2 女',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `state` int NOT NULL COMMENT '状态：1 正常，2 禁用，3  邮箱或手机号未验证，4 演示账号 5 账户删除 6 账号未激活 7 账号冻结',
  `signature` varchar(200) DEFAULT NULL COMMENT '签名',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `email_verified_time` datetime DEFAULT NULL COMMENT '邮箱验证时间',
  `phone_verified_time` datetime DEFAULT NULL COMMENT '手机号码验证时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=10030 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台管理用户表';

-- Create syntax for TABLE 'sys_user_menu'
CREATE TABLE `sys_user_menu` (
  `user_id` bigint NOT NULL COMMENT '后台用户id',
  `menu_id` bigint NOT NULL COMMENT '后台菜单id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户菜单关联表';

-- Create syntax for TABLE 'user'
CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名：为5-16个大小写字母，唯一',
  `nickname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称：由1-16个字符以内',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱：唯一',
  `password` varchar(255) NOT NULL COMMENT '密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 组成',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像URL',
  `sex` int DEFAULT '3' COMMENT '性别：1 男，2 女，3 未知',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `signature` varchar(200) DEFAULT NULL COMMENT '签名',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `wechat` varchar(16) DEFAULT NULL COMMENT '微信号',
  `qq` varchar(16) DEFAULT NULL COMMENT 'QQ号',
  `state` int DEFAULT NULL COMMENT '状态：1 正常，2 冻结，3 封号，4 注销，5 邮箱或手机号码未验证',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `email_verified_time` datetime DEFAULT NULL COMMENT '邮箱验证时间',
  `phone_verified_time` datetime DEFAULT NULL COMMENT '手机号码验证时间',
  `post_count` bigint DEFAULT NULL COMMENT '文章数量',
  `comment_count` bigint DEFAULT NULL COMMENT '评论数量',
  `like_count` bigint DEFAULT NULL COMMENT '获赞数量',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';