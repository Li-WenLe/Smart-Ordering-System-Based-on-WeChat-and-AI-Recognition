# 基于微信小程序与 AI 视觉识别的智能点餐系统是一套集微信小程序点餐、Vue 管理后台、Spring Boot 业务服务和 Django 菜品图像识别于一体的智能餐饮平台，支持菜品管理、购物车、订单、优惠券、AI 对话及视觉识别等功能

项目包含用户端微信小程序、后台管理系统、核心业务服务，以及基于计算机视觉的菜品识别服务。

## 项目功能

### 用户端小程序

- 用户注册与登录
- 菜品浏览与分类查询
- 菜品搜索
- 购物车管理
- 地址管理
- 提交订单与订单查询
- 优惠券管理
- 优惠券秒杀
- 在线客服聊天
- AI 菜品识别

### 管理后台

- 用户管理
- 菜品分类管理
- 菜品管理
- 套餐管理
- 轮播图管理
- 优惠券管理
- 订单管理
- 数据统计

### 后端服务

- 用户认证与权限控制
- 菜品和订单业务接口
- MySQL 数据持久化
- Redis 缓存
- RabbitMQ 消息处理
- WebSocket 实时通信
- 阿里云 OSS 图片上传
- AI 对话和菜品识别

## 技术栈

| 模块 | 技术 |
| --- | --- |
| 用户端 | 微信小程序 |
| 管理端 | Vue 3、Vue Router、Vuex、Element Plus、ECharts |
| 核心后端 | Spring Boot 3、MyBatis、MySQL、Redis、WebScoket |
| 缓存与消息 | Redis、RabbitMQ |
| AI 服务 | Spring AI、智谱 AI、DeepSeek |
| 图像识别 | Django、PyTorch、OpenCV |
| 文件存储 | 阿里云 OSS |

## 项目目录

点餐项目升级/
├── 点餐小程序源码/       # 微信小程序
├── vue/                  # Vue 管理后台
├── wxprogrem/            # Spring Boot 后端
└── Django项目源码/       # 菜品图像识别服务

本项目仅用于学习和技术交流。
项目中涉及到的 API Key、云服务密钥均已失效，需要自行配置
数据库密码或用户名需要自行配置本地或云端自行连接

附上项目的操作手册,包含了项目的功能以及操作方法
[基于卷积神经网络的AI菜品识别与外卖管理系统-操作手册.pdf](https://github.com/user-attachments/files/31437059/AI.-.pdf)

附上Mysq1 的sql文件
[shop.sql](https://github.com/user-attachments/files/31679915/shop.sql)
/*
 Navicat Premium Data Transfer

 Source Server         : connect
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : shop

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 14/01/2026 22:31:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_book
-- ----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `detail_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_default_address` int NULL DEFAULT NULL,
  `tag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address_book
-- ----------------------------
INSERT INTO `address_book` VALUES (1, 1, '17638382838', 'lwl', '河南省许昌市花园小区', '101', 1, 'company');
INSERT INTO `address_book` VALUES (2, 7, '17563736473', 'yxc', '河南省许昌市花园小区', '203', 1, 'home');
INSERT INTO `address_book` VALUES (3, 1, '17684973864', 'lwl', '河南省新乡市红旗区', '花园小区108号', 0, 'school');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ishow` int NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (2, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/2a9ab1e3-43b6-44bc-8198-db09c993383a_1fe4d3c406837a1a59a85fa14fd3ff1.png', '智慧餐馆开业了，优惠多多，实惠美味，欢迎各位前来体验', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/43a6c3cb-de89-4ab7-add0-8691f30cbbc1_942497808815c4f31abdfc7c79c923b.png', '旧城以西智慧参观今天正式开始营业了！！');
INSERT INTO `banner` VALUES (3, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/db9ce817-dd24-4153-aa09-446022eb72c0_f899e82facbe0aa0d109a103ff5d1e8.png', '新用户注册即送10元无门槛优惠券，可在规定日期购买任意商品使用', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/db9ce817-dd24-4153-aa09-446022eb72c0_f899e82facbe0aa0d109a103ff5d1e8.png', '新用户注册即送10元无门槛优惠券');
INSERT INTO `banner` VALUES (5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3f7801c4-9982-4195-9117-41f57a062720_984ae715d7685a38afbc726f623e135.jpg', '为了迎合大众需求，本店新增了套餐若干，具体信息可以在分类页面查看。', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0938bdf6-4e2c-4bbb-971c-27460032a460_f5963f12dff0bef9062739caff9b7b4.png', '新增套餐提醒');
INSERT INTO `banner` VALUES (6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/c1bbb878-eee6-4796-b185-a9d812764b16_05ee6765dd12ab28ddc67d5b04f5165.png', '这是标题四对应的内容', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/c7a11275-ebdd-438c-82eb-2a68c7ad1e27_05ee6765dd12ab28ddc67d5b04f5165.png', '这是标题四');
INSERT INTO `banner` VALUES (8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4e5b19cf-735e-460f-817c-0aee839588ca_af38219529d2aa41d2a73eb8738ae73.png', '疯狂星期六优惠券秒杀', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a77c1052-8967-4095-ad0d-31bd53dd995b_af38219529d2aa41d2a73eb8738ae73.png', '疯狂星期六优惠券秒杀');

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `price` double NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `dish_type_id` int NULL DEFAULT NULL,
  `recommend` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish
-- ----------------------------
INSERT INTO `dish` VALUES (4, '草莓蛋糕', 8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '草莓蛋糕，由新鲜草莓和奶油制作而成，口感丝滑', 1, '2025-03-26 14:38:32', '2025-04-20 18:19:24', 13, 1);
INSERT INTO `dish` VALUES (5, '铁锅牛肉面', 18, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '铁锅牛肉面是以牛腱肉、切面等为主要材料制作的食物，口味鲜香，为中华传统面食。', 0, NULL, '2025-04-10 14:14:26', 3, 0);
INSERT INTO `dish` VALUES (13, '卤制红烧肉', 15.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/10c778a0-5df8-485b-9fd5-8b2c89a985fc_d431fc33222b68d7608a2235b613357.png', '五花肉为制作主料 ， 肥而不腻，香甜松软，营养丰富，入口即化。', 0, NULL, '2025-04-10 11:26:57', 3, 0);
INSERT INTO `dish` VALUES (14, '水果沙拉', 16.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '精制水果沙拉盘，包含芒果、蓝莓、草莓等新鲜水果。', 0, NULL, '2025-04-10 11:40:07', 13, 0);
INSERT INTO `dish` VALUES (15, '青椒炒肉', 15.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/05ddabfa-c0da-4466-8455-46545aca41b3_1884a94868aaa10cb59abe1dd6956a7.png', '青椒炒肉是一道以青椒、猪肉为主要食材烹制而成的京菜，口味辣。 味道可口，营养丰富', 1, NULL, '2025-04-10 15:35:25', 3, 1);
INSERT INTO `dish` VALUES (17, '红烧鲤鱼', 18.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '红烧鲤鱼的烹饪技巧以红烧为主，口味属于咸鲜，呈黄色，鱼嫩蘑香，汁浓味美。', 0, NULL, '2025-04-10 14:22:37', 3, 0);
INSERT INTO `dish` VALUES (18, '香辣鸡腿堡', 10.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '香辣鸡腿堡，由精品鸡腿肉、生菜叶、芝士片自作而成，香辣可口。', 0, NULL, '2025-04-10 14:28:26', 14, 0);
INSERT INTO `dish` VALUES (19, '可乐', 3, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4ef04083-192a-4d10-9b8c-fcbcddbf5865_69e2b7c5ec55d45d8f70877bb62afb6.png', '小瓶瓶装可口可乐', 0, NULL, '2025-04-10 14:56:13', 5, 0);
INSERT INTO `dish` VALUES (20, '铁板龙虾', 18.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '铁板龙虾，由新鲜龙虾烹饪而成，香辣可口。', 0, NULL, '2025-04-14 10:50:08', 3, 0);
INSERT INTO `dish` VALUES (21, '红烧牛肉片', 28.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/d4af5014-aab2-4368-b03c-4c102e05cc30_178612332f372b122728a816ce60979.png', '红烧牛肉片，由新鲜牛肉蒸煮而成，香辣可口，色香味俱全', 0, NULL, '2025-04-14 10:53:08', 3, 0);
INSERT INTO `dish` VALUES (22, '龙虾火锅', 38.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '龙虾火锅，物美价廉', 0, NULL, '2025-04-14 10:55:21', 3, 0);
INSERT INTO `dish` VALUES (23, '坚果500g', 18.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ca391cb8-3dc8-4132-9b6e-485f7722f4c2_f9c9c4e660d621839a95b5a41ac1c02.png', '由杏仁、巴西坚果、 腰果、榛子、澳洲坚果、山核桃、松子、开心果、核桃、葵花子和花生等混装', 0, NULL, '2025-04-17 16:50:42', 4, 0);
INSERT INTO `dish` VALUES (24, '腰果500g', 28.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/00beb8a5-34a9-49a3-bc58-23a1b719d48e_6553ff7577449f9c3d0ae6c65fec017.png', '营养价值非常高，含有丰富的蛋白质、脂肪、矿物质和维生素。', 0, NULL, '2025-04-18 10:32:15', 4, 0);
INSERT INTO `dish` VALUES (25, '开心果500g', 19.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/39d4eb46-52e8-431c-b2e4-86a674727a52_ec23e87dd04be86cd9e81a0fd689395.png', '开心果果仁是高营养的食品，每100克果仁含维生素A20微克，叶酸59微克，同时含有多种矿物质等', 0, NULL, '2025-04-18 10:37:24', 4, 0);
INSERT INTO `dish` VALUES (26, '杏仁500g', 18.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/55cba14c-5506-4f63-9303-44ee6677f4ba_586b25c1a39fb022104c6133b411abf.png', '杏仁含有丰富的营养成分，特别富含蛋白质、脂肪、矿物质和维生素', 0, NULL, '2025-04-18 10:41:08', 4, 0);
INSERT INTO `dish` VALUES (27, '核桃仁500g', 18.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/93b0eed3-5794-46e8-8439-6e8f90ca74f5_18ac5ed27b1dd8e195ceabbb496b1d1.png', '核桃仁含有丰富的营养素，是很好的滋补品和食品加工原料，含脂肪65%左右，蛋白质15%左右，碳水化合物10%以上', 0, NULL, '2025-04-18 10:45:18', 4, 0);
INSERT INTO `dish` VALUES (28, '夏威夷果', 28.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e833b94f-f201-4c7b-9930-00a787885347_cb42a346d3cef1738be1c9593ac0fc6.png', '夏威夷果 果仁营养丰富，其外果皮青绿色，内果皮坚硬，呈褐色，单果重15—16克，含油量70％左右， 蛋白质 9％', 0, NULL, '2025-04-18 10:55:04', 4, 0);
INSERT INTO `dish` VALUES (29, '炒板栗200g', 10, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a4868da5-82a9-4049-a715-511cf2bd4a50_e77c8818c491dbd87026a713f8f79ba.png', '口感丰富多样，既有坚实的质感，又有糯甜的细腻。咬上一口，板栗的香气在口腔中弥漫开来，让人感受到秋天的浓郁气 …', 0, NULL, '2025-04-18 10:53:43', 4, 0);
INSERT INTO `dish` VALUES (30, '柠檬水500ml', 3.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3a1ba2b6-ffc5-41af-a48a-be2a802e7b7d_d3438a42f5726c7e020d6438f208122.png', '冰镇柠檬水，炎日必备，清凉解渴', 0, NULL, '2025-04-18 11:58:10', 5, 0);
INSERT INTO `dish` VALUES (31, '茉莉花茶', 15, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/c153f434-a625-40b1-b70c-bda1772bdcc1_419fdbc5b54db4f0d3b5e2c5ba2c9ad.jpg', '甄选横县茉莉、高山春茶制成茉莉花茶、搭配醇香牛乳、花香入茶骨，口感清爽轻负担', 0, NULL, '2025-04-18 12:03:23', 5, 0);
INSERT INTO `dish` VALUES (32, '芝士绿茶', 18.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e2bec630-405b-47a5-ae7d-2c7a917e0ffc_8f0589578ffd95b736a777c65f13e07.png', '芝士绿茶，灵感来自喜茶十年经典芝士茶。 特别添加胶原蛋白肽。 低脂低糖，一瓶的能量小于一个苹果', 0, NULL, '2025-04-18 16:37:46', 5, 0);
INSERT INTO `dish` VALUES (33, '百香双重奏', 10.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e46abccc-dd9f-4d05-b9db-cf18507e9f67_2534ab41d846bd3ba665ad5095c943f.jpg', '满满百香果，超多维生素C搭配珍珠和椰果，口感兼具Q弹和软糯', 0, NULL, '2025-04-19 20:06:13', 5, 0);
INSERT INTO `dish` VALUES (34, '超A葡萄冰', 17.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/350d2c55-d7ab-4022-9703-f426a74b7183_17c29f766536474ce88d5748386fea3.jpg', '手剥时令葡萄，融合Q弹茉莉冻，不含奶盖口感更轻盈', 0, NULL, '2025-04-18 16:51:37', 5, 0);
INSERT INTO `dish` VALUES (36, '布蕾脆脆奶芙', 18, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7ae82bbb-e132-4754-b878-a56a704f34f2_113803f558813143f909825bab4dcb9.jpg', '入口即化的奶芙搭配Q弹有嚼劲的珍珠，碰撞出不一样的味蕾体验。', 0, NULL, '2025-04-18 17:15:01', 5, 0);
INSERT INTO `dish` VALUES (37, '伊利盒装纯牛奶', 3.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4b4bfff8-f175-4a54-8638-1f9bcca56f8b_c4cdfc3f44f8121005c34f5a3114284.png', '伊利盒装生牛乳，经典品牌，值得信赖', 0, NULL, '2025-04-18 17:22:45', 1, 0);
INSERT INTO `dish` VALUES (38, '罐装旺仔牛奶', 5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/df76fcfd-b18e-4d27-acac-a74d35f022aa_c4bd4d5c5affb912b57e8510e3019fd.png', '儿童含乳饮料乳制品学生早餐', 0, NULL, '2025-04-18 17:26:58', 1, 0);
INSERT INTO `dish` VALUES (39, '旺仔牛奶礼包', 15.9, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/23f3180b-e5b5-4171-bf43-1255740bfbb3_6ce42ba1d72103be9b21697da8c21bc.jpg', '儿童乳制品饮料罐装', 0, NULL, '2025-04-18 17:48:13', 1, 0);
INSERT INTO `dish` VALUES (40, '美汁源果粒奶优', 5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/75226675-4787-4c87-9293-689ae7ee3e2e_8e9660f30050fbd892bebc1f53bbc7a.png', '果粒奶优乳制品饮料含糖', 0, NULL, '2025-04-18 18:36:20', 1, 0);
INSERT INTO `dish` VALUES (41, '优酸乳盒装', 3.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/b1d5c68d-c031-4512-9050-1311ec71da48_69e37465f8539cbd81256f435d1735e.png', '乳饮料饮品牛奶批特价果粒酸酸乳', 0, NULL, '2025-04-18 19:11:29', 1, 0);
INSERT INTO `dish` VALUES (42, '伊利 优酸乳原味250ml*24盒整箱', 58, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7cd1e125-6db4-4c73-92f1-7b5c5114aa6c_abbc959f88082508fb8139f8dbf6c3c.png', '伊利 优酸乳原味250ml*24盒整箱乳饮料饮品牛奶批特价果粒酸酸乳', 0, NULL, '2025-04-18 19:12:56', 1, 0);
INSERT INTO `dish` VALUES (43, '蛋炒饭小份', 10.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/afff221f-2ec6-493b-9935-2d51cf215a4a_dc8f3393836b572d2525c33d6185cea.png', '以鸡蛋 、米饭 为主要食材的常见菜肴', 0, NULL, '2025-04-18 19:20:37', 10, 0);
INSERT INTO `dish` VALUES (44, '蛋炒饭大份', 15.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/aec5ff1f-e161-480e-8446-4efdfb92d35e_d57fa654d851b84198a9fe64acd9ec0.png', '以鸡蛋 、米饭、火腿肠 为主要食材的常见菜肴', 0, NULL, '2025-04-18 19:22:48', 10, 0);
INSERT INTO `dish` VALUES (45, '红烧肉拌饭', 18.8, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/1cec431e-7f10-44c2-8825-61cb8221587f_4affe5af5a712cb33a947946ac774b4.png', '红烧肉拌饭是一道以五花肉、油菜为主料的菜品', 0, NULL, '2025-04-18 19:26:10', 10, 0);
INSERT INTO `dish` VALUES (46, '西红柿鸡蛋盖饭', 11, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/285b4e04-d496-476b-9550-a1086f9b6d40_93440c96086b348f07a01861ac6218c.jpg', '原料：番茄鸡蛋大米、1人份', 0, NULL, '2025-04-18 19:30:53', 10, 0);
INSERT INTO `dish` VALUES (47, '可乐鸡块盖饭', 15, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/1291c079-4d78-4ffa-8740-75c103a72c81_691e71229a689cb3b6e041806ac2da2.jpg', '原料：鸡肉大米、1人份', 0, NULL, '2025-04-18 19:34:14', 10, 0);
INSERT INTO `dish` VALUES (48, '烤肉拌饭', 18.6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/6783ba60-76e6-455f-92f5-56c0a66f547d_21a24cff2d70f8786a8326cc9d03640.jpg', '大米，猪肉一人份', 0, NULL, '2025-04-18 21:25:54', 10, 0);
INSERT INTO `dish` VALUES (49, '双层香辣鸡腿堡', 13, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/37cc8e43-3a34-45a1-b67b-8305a69550d4_dcf2778d82eaaf9b797a829a9cb0a7b.png', '双层鸡腿堡是一道由高筋面粉、鸡腿、白砂糖、盐等食材制成的食品。', 0, NULL, '2025-04-18 21:29:34', 14, 0);
INSERT INTO `dish` VALUES (50, '香酥鸡排', 6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/8b19a30f-3993-4ee2-aae4-31655997ff52_96c251f536dc0c82ec52a9fe99d0c72.jpg', '扒皮抽骨鸡腿肉，裹上面粉，丢进油锅炸制两面金黄', 0, NULL, '2025-04-18 21:40:57', 15, 1);
INSERT INTO `dish` VALUES (51, '鸡米花', 6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/48168431-b67f-4663-92ca-cb111590b8a7_938e9805f7f6d8a1672cfe969a79e28.jpg', '去骨鸡腿肉油炸制成', 0, NULL, '2025-04-18 21:42:02', 15, 0);
INSERT INTO `dish` VALUES (52, '黑椒鸡块', 5.5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ba0745d8-2cd2-4a27-a7b5-38c7478930bd_755c41da7e80ddb6c7ac80b8e8db5ae.jpg', '黑椒鸡块，由面粉鸡肉制作而成', 0, NULL, '2025-04-18 21:49:37', 15, 1);
INSERT INTO `dish` VALUES (53, '油炸小腿', 6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/9ef0fd56-1d88-41f1-b376-acfd0edf1b09_e980ae5569f3af162c9c41737a5411c.jpg', '油炸鸡小腿', 0, NULL, '2025-04-18 21:52:50', 15, 0);

-- ----------------------------
-- Table structure for dish_type
-- ----------------------------
DROP TABLE IF EXISTS `dish_type`;
CREATE TABLE `dish_type`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dish_id` int NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ishow` int NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish_type
-- ----------------------------
INSERT INTO `dish_type` VALUES (1, 1, '乳制饮料', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ded3a95d-6536-44c4-9074-0476f472eb49_006 - Fries.png');
INSERT INTO `dish_type` VALUES (3, NULL, '肉类', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a66f0170-89ca-4ebc-96aa-d026d483a917_生鲜-肉类3.png');
INSERT INTO `dish_type` VALUES (4, NULL, '坚果', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/14457579-0065-4f41-b362-01e2eccf2511_坚果 (1).png');
INSERT INTO `dish_type` VALUES (5, NULL, '饮料', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/fa68b2f2-81da-42f8-b4b6-7cdfc9c66825_饮料.png');
INSERT INTO `dish_type` VALUES (10, NULL, '米饭', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/18c46f77-9ef5-4dfb-a954-4b728ff9b7d7_米饭.png');
INSERT INTO `dish_type` VALUES (13, NULL, '甜点', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/6b39ea77-b980-4015-8bc1-6cae232ad9f4_甜甜圈,甜点,糕点,零食.png');
INSERT INTO `dish_type` VALUES (14, NULL, '汉堡', 0, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a620710a-fd8b-4cd6-b0c1-eab221e26978_汉堡.png');
INSERT INTO `dish_type` VALUES (15, NULL, '油炸食品', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a99b198a-958e-44dc-9f83-7676e570ac0a_006 - Fries.png');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 'admin', 'admin', 'lwl');
INSERT INTO `employee` VALUES (2, 'admin1', 'admin1', 'admin');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total` double NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `order_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payed_total` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (11, '木子李', '18736473847', 22, 1, '河南省许昌市花园小区101', 0, '2025-04-07 11:54:30', NULL, '202504071954303020', 22);
INSERT INTO `order` VALUES (12, 'yxc', '17837483784', 22, 7, '河南省许昌市花园小区203', 0, '2025-04-08 09:51:44', NULL, '202504081751456356', 8);
INSERT INTO `order` VALUES (13, 'yxc', '17837483784', 22, 7, '河南省许昌市花园小区203', 3, '2025-04-08 10:39:42', '2025-04-09 11:05:00', '202504081839427519', 22);
INSERT INTO `order` VALUES (14, 'yxc', '17837483784', 4, 7, '河南省许昌市花园小区203', 3, '2025-04-08 10:39:47', '2025-04-09 11:05:00', '202504081839475261', 22);
INSERT INTO `order` VALUES (15, 'yxc', '17837483784', 4, 7, '河南省许昌市花园小区203', 3, '2025-04-08 10:40:29', '2025-04-09 11:05:00', '202504081840293821', 4);
INSERT INTO `order` VALUES (16, '木子李', '18736473847', 22, 1, '河南省许昌市花园小区101', 0, '2025-04-09 03:33:42', NULL, '202504091133428841', 22);
INSERT INTO `order` VALUES (17, '木子李', '18736473847', 12, 1, '河南省许昌市花园小区101', 0, '2025-04-09 03:56:16', NULL, '202504091156166319', 12);
INSERT INTO `order` VALUES (18, '木子李', '18736473847', 12, 1, '河南省许昌市花园小区101', 0, '2025-04-09 03:58:34', NULL, '202504091158344221', 12);
INSERT INTO `order` VALUES (19, '木子李', '18736473847', 22, 1, '河南省许昌市花园小区101', 0, '2025-04-09 04:01:18', NULL, '202504091201183851', 22);
INSERT INTO `order` VALUES (20, '木子李', '18736473847', 12, 1, '河南省许昌市花园小区101', 0, '2025-04-09 04:03:42', '2025-05-02 19:42:09', '202504091203426919', 12);
INSERT INTO `order` VALUES (21, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 0, '2025-04-09 07:30:34', '2025-04-19 22:42:25', '202504091530348624', 30);
INSERT INTO `order` VALUES (22, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 0, '2025-04-09 07:32:50', '2025-04-19 22:42:32', '202504091532508034', 30);
INSERT INTO `order` VALUES (23, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:40:10', NULL, '20250409154010244', 30);
INSERT INTO `order` VALUES (24, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:44:51', NULL, '202504091544516107', 30);
INSERT INTO `order` VALUES (25, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:46:25', NULL, '202504091546254156', 30);
INSERT INTO `order` VALUES (26, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:51:02', NULL, '202504091551023365', 30);
INSERT INTO `order` VALUES (27, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:52:57', NULL, '202504091552576888', 30);
INSERT INTO `order` VALUES (28, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:54:08', NULL, '202504091554083178', 30);
INSERT INTO `order` VALUES (29, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 07:54:57', NULL, '202504091554578941', 30);
INSERT INTO `order` VALUES (30, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 08:00:35', NULL, '202504091600354503', 30);
INSERT INTO `order` VALUES (31, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 08:02:54', NULL, '202504091602541388', 30);
INSERT INTO `order` VALUES (32, '木子李', '18736473847', 30, 1, '河南省许昌市花园小区101', 2, '2025-04-09 08:21:03', NULL, '202504091621039543', 30);
INSERT INTO `order` VALUES (33, '木子李', '18736473847', 22, 1, '河南省许昌市花园小区101', 3, '2025-04-09 08:27:16', '2025-04-10 19:46:00', '202504091627163099', 22);
INSERT INTO `order` VALUES (34, '木子李', '18736473847', 22, 1, '河南省许昌市花园小区101', 2, '2025-04-09 08:34:19', NULL, '202504091634193313', 12);
INSERT INTO `order` VALUES (35, '木子李', '18736473847', 69.3, 1, '河南省许昌市花园小区101', 0, '2025-04-10 07:40:07', NULL, '202504101540079521', 59);
INSERT INTO `order` VALUES (36, '木子李', '18736473847', 7, 1, '河南省许昌市花园小区101', 2, '2025-04-10 08:08:00', NULL, '202504101608009936', 7);
INSERT INTO `order` VALUES (37, '木子李', '18736473847', 30.3, 1, '河南省许昌市花园小区101', 4, '2025-04-10 08:29:16', NULL, '202504101629167191', 30);
INSERT INTO `order` VALUES (38, '木子李', '18736473847', 20.8, 1, '河南省许昌市花园小区101', 0, '2025-04-10 08:31:42', NULL, '202504101631426306', 20);
INSERT INTO `order` VALUES (42, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 3, '2025-04-14 11:10:12', '2025-04-14 19:11:00', '202504141910129027', 22);
INSERT INTO `order` VALUES (43, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 3, '2025-04-14 11:21:36', '2025-04-14 19:22:00', '202504141921366156', 22);
INSERT INTO `order` VALUES (44, 'lwl', '17484638372', 61.3, 1, '河南省许昌市花园小区101', 0, '2025-04-15 02:29:28', NULL, '202504151029282913', 61);
INSERT INTO `order` VALUES (45, 'lwl', '17484638372', 61, 1, '河南省许昌市花园小区101', 3, '2025-04-15 02:30:04', '2025-04-22 16:25:00', '202504151030048259', 61);
INSERT INTO `order` VALUES (46, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 2, '2025-04-15 02:30:25', NULL, '202504151030254424', 22);
INSERT INTO `order` VALUES (47, 'lwl', '17484638372', 14.5, 1, '河南省许昌市花园小区101', 2, '2025-04-15 03:04:15', NULL, '202504151104153884', 14);
INSERT INTO `order` VALUES (48, 'lwl', '17484638372', 49.8, 1, '河南省许昌市花园小区101', 2, '2025-04-15 10:08:56', NULL, '202504151808564167', 49);
INSERT INTO `order` VALUES (49, 'lwl', '17484638372', 42.8, 1, '河南省许昌市花园小区101', 2, '2025-04-15 10:09:30', NULL, '20250415180930219', 42);
INSERT INTO `order` VALUES (50, 'lwl', '17484638372', 42.8, 1, '河南省许昌市花园小区101', 0, '2025-04-16 02:16:33', NULL, '202504161016339590', 32);
INSERT INTO `order` VALUES (60, 'lwl', '17484638372', 4, 1, '河南省许昌市花园小区101', 2, '2025-04-17 06:22:10', NULL, '20250417142210514', 4);
INSERT INTO `order` VALUES (61, 'lwl', '17484638372', 42.8, 1, '河南省许昌市花园小区101', 0, '2025-04-17 06:25:04', NULL, '202504171425044013', 42);
INSERT INTO `order` VALUES (62, 'lwl', '17484638372', 14.5, 1, '河南省许昌市花园小区101', 0, '2025-04-17 06:29:02', NULL, '202504171429023392', 14);
INSERT INTO `order` VALUES (63, 'lwl', '17484638372', 7, 1, '河南省许昌市花园小区101', 4, '2025-04-17 06:31:02', NULL, '202504171431024314', 7);
INSERT INTO `order` VALUES (64, 'lwl', '17484638372', 30, 1, '河南省许昌市花园小区101', 0, '2025-04-17 06:43:06', NULL, '202504171443066238', 30);
INSERT INTO `order` VALUES (65, 'lwl', '17484638372', 14.5, 1, '河南省许昌市花园小区101', 0, '2025-04-17 06:44:10', NULL, '202504171444101156', 14);
INSERT INTO `order` VALUES (66, 'lwl', '17484638372', 12, 1, '河南省许昌市花园小区101', 3, '2025-04-17 06:44:50', '2025-04-22 16:25:00', '202504171444502430', 12);
INSERT INTO `order` VALUES (67, 'lwl', '17484638372', 12, 1, '河南省许昌市花园小区101', 3, '2025-04-17 06:47:13', '2025-04-22 16:25:00', '202504171447133928', 12);
INSERT INTO `order` VALUES (68, 'lwl', '17484638372', 12, 1, '河南省许昌市花园小区101', 0, '2025-04-17 06:51:57', NULL, '202504171451576660', 12);
INSERT INTO `order` VALUES (69, 'lwl', '17484638372', 19.8, 1, '河南省许昌市花园小区101', 3, '2025-04-18 02:59:22', '2025-04-22 16:25:00', '202504181059222755', 19);
INSERT INTO `order` VALUES (70, 'lwl', '17484638372', 33.3, 1, '河南省许昌市花园小区101', 3, '2025-04-18 08:06:58', '2025-04-22 16:25:00', '202504181606585547', 23);
INSERT INTO `order` VALUES (71, 'lwl', '17484638372', 61.3, 1, '河南省许昌市花园小区101', 4, '2025-04-18 08:08:10', NULL, '202504181608108024', 61);
INSERT INTO `order` VALUES (72, 'lwl', '17484638372', 7, 1, '河南省许昌市花园小区101', 0, '2025-04-18 08:09:06', '2025-04-19 22:42:55', '202504181609066890', 7);
INSERT INTO `order` VALUES (73, 'lwl', '17484638372', 36.5, 1, '河南省许昌市花园小区101', 2, '2025-04-18 14:46:07', NULL, '202504182246077310', 26);
INSERT INTO `order` VALUES (74, 'lwl', '17484638372', 36.5, 1, '河南省许昌市花园小区101', 4, '2025-04-18 14:50:41', NULL, '202504182250411554', 26);
INSERT INTO `order` VALUES (75, 'lwl', '17484638372', 14.5, 1, '河南省许昌市花园小区101', 3, '2025-04-18 14:56:57', '2025-04-22 16:25:00', '202504182256574374', 14);
INSERT INTO `order` VALUES (76, 'lwl', '17484638372', 36.5, 1, '河南省许昌市花园小区101', 3, '2025-04-19 03:40:58', '2025-04-22 16:25:00', '202504191140587804', 36);
INSERT INTO `order` VALUES (77, 'lwl', '17484638372', 55.3, 1, '河南省许昌市花园小区101', 3, '2025-04-19 05:37:38', '2025-04-22 16:25:00', '202504191337386533', 55);
INSERT INTO `order` VALUES (78, 'lwl', '17484638372', 22, 1, '河南省许昌市花园小区101', 3, '2025-04-19 05:45:26', '2025-04-22 16:25:00', '202504191345268648', 22);
INSERT INTO `order` VALUES (79, 'lwl', '17484638372', 12, 1, '河南省许昌市花园小区101', 0, '2025-04-19 05:45:47', '2025-04-20 18:34:45', '202504191345479768', 12);
INSERT INTO `order` VALUES (80, 'lwl', '17484638372', 7.5, 1, '河南省许昌市花园小区101', 3, '2025-04-19 05:50:23', '2025-04-22 16:25:00', '202504191350233411', 7);
INSERT INTO `order` VALUES (81, 'yxc', '16738637467', 22.5, 7, '河南省许昌市花园小区203', 3, '2025-04-19 05:53:47', '2025-04-22 16:25:00', '202504191353471931', 22);
INSERT INTO `order` VALUES (82, 'yxc', '16738637467', 32.5, 7, '河南省许昌市花园小区203', 3, '2025-04-19 05:54:07', '2025-04-22 16:25:00', '202504191354077299', 32);
INSERT INTO `order` VALUES (83, 'yxc', '16738637467', 22.8, 7, '河南省许昌市花园小区203', 0, '2025-04-19 05:54:25', NULL, '202504191354258948', 22);
INSERT INTO `order` VALUES (84, 'yxc', '16738637467', 22.8, 7, '河南省许昌市花园小区203', 3, '2025-04-19 05:58:03', '2025-04-22 16:25:00', '202504191358034002', 22);
INSERT INTO `order` VALUES (85, 'lwl', '17484638372', 55.3, 1, '河南省许昌市花园小区101', 0, '2025-04-19 06:36:51', NULL, '202504191436514414', 55);
INSERT INTO `order` VALUES (86, 'lwl', '17484638372', 21.5, 1, '河南省许昌市花园小区101', 0, '2025-04-20 00:23:07', '2025-04-20 17:59:05', '202504200823077433', 11);
INSERT INTO `order` VALUES (87, 'lwl', '17484638372', 85.6, 1, '河南省许昌市花园小区101', 3, '2025-04-20 09:39:23', '2025-04-22 16:25:00', '202504201739235745', 75);
INSERT INTO `order` VALUES (88, 'lwl', '17484638372', 85.6, 1, '河南省许昌市花园小区101', 3, '2025-04-20 09:40:52', '2025-04-22 16:25:00', '202504201740522121', 75);
INSERT INTO `order` VALUES (89, 'lwl', '17484638372', 85.6, 1, '河南省许昌市花园小区101', 4, '2025-04-20 09:42:27', NULL, '202504201742271156', 85);
INSERT INTO `order` VALUES (90, 'lwl', '17484638372', 85.6, 1, '河南省许昌市花园小区101', 2, '2025-04-20 10:36:22', NULL, '202504201836228867', 85);
INSERT INTO `order` VALUES (91, 'lwl', '17484638372', 40, 1, '河南省许昌市花园小区101', 4, '2025-04-22 14:02:06', NULL, '202504222202063488', 40);
INSERT INTO `order` VALUES (92, 'lwl', '17484638372', 43.5, 1, '河南省许昌市花园小区101', 3, '2025-04-23 04:39:20', '2025-04-23 12:40:00', '202504231239204237', 0);
INSERT INTO `order` VALUES (93, 'lwl', '17484638372', 7.5, 1, '河南省许昌市花园小区101', 2, '2025-04-23 04:39:58', NULL, '202504231239583896', 7);
INSERT INTO `order` VALUES (94, 'lwl', '17484638372', 14.5, 1, '河南省许昌市花园小区101', 2, '2025-04-23 04:45:11', NULL, '202504231245113616', 14);
INSERT INTO `order` VALUES (95, 'lwl', '17484638372', 14.5, 1, '河南省许昌市花园小区101', 2, '2025-04-23 04:46:27', NULL, '202504231246276682', 14);
INSERT INTO `order` VALUES (96, 'lwl', '17484638372', 7.5, 1, '河南省许昌市花园小区101', 3, '2025-04-23 04:46:46', '2025-04-23 12:47:00', '202504231246464061', 7);
INSERT INTO `order` VALUES (97, 'lwl', '17484638372', 7.5, 1, '河南省许昌市花园小区101', 3, '2025-04-23 04:55:17', '2025-04-23 12:56:00', '202504231255174737', 7);
INSERT INTO `order` VALUES (98, 'lwl', '17484638372', 26, 1, '河南省许昌市花园小区101', 3, '2025-04-23 05:04:21', '2025-04-23 13:05:00', '202504231304217064', 26);
INSERT INTO `order` VALUES (99, 'lwl', '17484638372', 26, 1, '河南省许昌市花园小区101', 3, '2025-04-23 05:15:25', '2025-04-23 13:16:00', '202504231315259333', 26);
INSERT INTO `order` VALUES (100, 'lwl', '17484638372', 26, 1, '河南省许昌市花园小区101', 3, '2025-04-23 05:19:28', '2025-04-23 13:20:00', '202504231319281593', 26);
INSERT INTO `order` VALUES (101, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 3, '2025-04-23 05:22:15', '2025-04-23 13:23:00', '202504231322155637', 22);
INSERT INTO `order` VALUES (102, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 3, '2025-04-23 05:25:25', '2025-04-23 13:26:00', '202504231325252117', 22);
INSERT INTO `order` VALUES (103, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 3, '2025-04-23 05:29:17', '2025-04-23 13:30:00', '202504231329179686', 22);
INSERT INTO `order` VALUES (104, 'lwl', '17484638372', 41.6, 1, '河南省许昌市花园小区101', 3, '2025-04-23 13:37:46', '2025-04-23 13:53:00', '202504231337461466', 41);
INSERT INTO `order` VALUES (105, 'lwl', '17484638372', 19, 1, '河南省许昌市花园小区101', 4, '2025-04-25 08:50:39', NULL, '202504250850392793', 19);
INSERT INTO `order` VALUES (106, 'lwl', '17484638372', 19, 1, '河南省许昌市花园小区101', 4, '2025-04-25 08:59:25', NULL, '202504250859254321', 19);
INSERT INTO `order` VALUES (107, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 0, '2025-04-25 09:23:12', '2025-05-04 15:54:25', '202504250923128256', 22);
INSERT INTO `order` VALUES (108, 'lwl', '17484638372', 22.5, 1, '河南省许昌市花园小区101', 4, '2025-04-25 09:24:10', NULL, '202504250924105058', 22);
INSERT INTO `order` VALUES (109, 'lwl', '17484638372', 22, 1, '河南省许昌市花园小区101', 4, '2025-04-25 09:26:40', NULL, '202504250926405508', 22);
INSERT INTO `order` VALUES (110, 'yxc', '16738637467', 7.5, 7, '河南省许昌市花园小区203', 0, '2025-05-04 08:03:00', '2025-05-04 16:03:32', '202505041603004498', 7);
INSERT INTO `order` VALUES (111, 'lwl', '17484638372', 41, 1, '河南省许昌市花园小区101', 2, '2025-05-04 08:05:27', NULL, '202505041605271430', 41);
INSERT INTO `order` VALUES (112, 'yxc', '16738637467', 30, 7, '河南省许昌市花园小区203', 2, '2025-05-04 09:06:59', NULL, '202505041706599584', 30);
INSERT INTO `order` VALUES (113, 'yxc', '16738637467', 14.5, 7, '河南省许昌市花园小区203', 2, '2025-05-04 09:24:03', NULL, '20250504172403507', 14);
INSERT INTO `order` VALUES (114, 'yxc', '16738637467', 7.5, 7, '河南省许昌市花园小区203', 2, '2025-05-04 09:45:17', NULL, '202505041745177911', 7);
INSERT INTO `order` VALUES (115, 'yxc', '16738637467', 7.5, 7, '河南省许昌市花园小区203', 2, '2025-05-04 09:59:46', NULL, '202505041759467997', 7);
INSERT INTO `order` VALUES (116, 'lwl', '17484638372', 20.8, 1, '河南省许昌市花园小区101', 2, '2025-05-14 12:36:27', NULL, '202505142036272913', 20);
INSERT INTO `order` VALUES (117, 'lwl', '17484638372', 38.8, 1, '河南省许昌市花园小区101', 1, '2025-05-14 14:06:51', NULL, '2025051422065132', 38);

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dishname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dish_id` int NULL DEFAULT NULL,
  `number` int NULL DEFAULT NULL,
  `acount` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 167 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES (1, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (2, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (3, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (4, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (5, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (6, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (7, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (8, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (9, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (10, NULL, NULL, NULL, 0, 0, 0);
INSERT INTO `order_detail` VALUES (11, '酸菜鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '', 5, 1, 18);
INSERT INTO `order_detail` VALUES (12, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '', 4, 1, 8);
INSERT INTO `order_detail` VALUES (13, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '', 4, 1, 8);
INSERT INTO `order_detail` VALUES (14, '酸菜鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '', 5, 1, 18);
INSERT INTO `order_detail` VALUES (15, '酸菜鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504091621039543', 5, 1, 18);
INSERT INTO `order_detail` VALUES (16, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504091621039543', 4, 1, 8);
INSERT INTO `order_detail` VALUES (17, '酸菜鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504091634193313', 5, 1, 18);
INSERT INTO `order_detail` VALUES (18, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504091634193313', 4, 1, 8);
INSERT INTO `order_detail` VALUES (19, '酸菜鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504101540079521', 5, 1, 18);
INSERT INTO `order_detail` VALUES (20, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504101540079521', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (21, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5e2e7cd3-698e-4066-852e-786b32f6599a_05ee6765dd12ab28ddc67d5b04f5165.png', '202504101540079521', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (22, '人气汉堡套餐一', NULL, '202504101540079521', 5, 2, 13.5);
INSERT INTO `order_detail` VALUES (23, '小杯可乐', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4ef04083-192a-4d10-9b8c-fcbcddbf5865_69e2b7c5ec55d45d8f70877bb62afb6.png', '202504101608009936', 19, 1, 3);
INSERT INTO `order_detail` VALUES (24, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504101629167191', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (25, '卤制红烧肉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/10c778a0-5df8-485b-9fd5-8b2c89a985fc_d431fc33222b68d7608a2235b613357.png', '202504101629167191', 13, 1, 15.8);
INSERT INTO `order_detail` VALUES (26, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504101631426306', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (27, '卤制红烧肉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/10c778a0-5df8-485b-9fd5-8b2c89a985fc_d431fc33222b68d7608a2235b613357.png', '202504101631426306', 13, 1, 15.8);
INSERT INTO `order_detail` VALUES (28, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504101912391839', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (29, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504101912391839', 4, 1, 8);
INSERT INTO `order_detail` VALUES (30, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504141836476403', 4, 3, 8);
INSERT INTO `order_detail` VALUES (31, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504141836476403', 14, 3, 16.8);
INSERT INTO `order_detail` VALUES (32, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504141836476403', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (33, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504141909107375', 4, 3, 8);
INSERT INTO `order_detail` VALUES (34, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504141909107375', 14, 3, 16.8);
INSERT INTO `order_detail` VALUES (35, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504141909107375', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (36, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504151029282913', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (37, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504151029282913', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (38, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504151030254424', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (39, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504151030254424', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (40, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504151104153884', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (41, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504151104153884', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (42, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504151808564167', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (43, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504151808564167', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (44, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '202504151808564167', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (45, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '20250415180930219', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (46, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '20250415180930219', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (47, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '20250415180930219', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (48, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504161016339590', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (49, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504161028452579', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (50, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504161029535981', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (51, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504161738354616', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (52, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504161738354616', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (53, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504161742277500', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (54, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504161742277500', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (55, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504162213172513', 5, 2, 18);
INSERT INTO `order_detail` VALUES (56, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504171415339674', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (57, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504171415339674', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (58, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504171418565476', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (59, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504171418565476', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (60, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '20250417141954835', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (61, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '20250417141954835', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (62, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504171421293570', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (63, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504171421293570', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (64, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '20250417142210514', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (65, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '20250417142210514', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (66, '龙虾火锅', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/3e0218ae-d3a3-4b1f-a9ad-430b4b39b561_bfb889dc2ca08ca77430c3c249b3d2f.png', '202504171425044013', 22, 1, 38.8);
INSERT INTO `order_detail` VALUES (67, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504171429023392', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (68, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504171431024314', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (69, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504171443066238', 4, 1, 8);
INSERT INTO `order_detail` VALUES (70, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504171443066238', 5, 1, 18);
INSERT INTO `order_detail` VALUES (71, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504171444101156', 4, 1, 8);
INSERT INTO `order_detail` VALUES (72, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504171444101156', 5, 1, 18);
INSERT INTO `order_detail` VALUES (73, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202504171451576660', 4, 1, 8);
INSERT INTO `order_detail` VALUES (74, '卤制红烧肉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/10c778a0-5df8-485b-9fd5-8b2c89a985fc_d431fc33222b68d7608a2235b613357.png', '202504181608108024', 13, 1, 15.8);
INSERT INTO `order_detail` VALUES (75, '人气汉堡套餐一', NULL, '202504181608108024', 5, 1, 13.5);
INSERT INTO `order_detail` VALUES (76, '小杯可乐', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4ef04083-192a-4d10-9b8c-fcbcddbf5865_69e2b7c5ec55d45d8f70877bb62afb6.png', '202504181609066890', 19, 1, 3);
INSERT INTO `order_detail` VALUES (77, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504182246077310', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (78, '人气汉堡套餐二', NULL, '202504182246077310', 6, 1, 22);
INSERT INTO `order_detail` VALUES (79, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504182250411554', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (80, '人气汉堡套餐二', NULL, '202504182250411554', 6, 1, 22);
INSERT INTO `order_detail` VALUES (81, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504191345479768', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (82, '人气汉堡套餐二', NULL, '202504191345479768', 6, 1, 22);
INSERT INTO `order_detail` VALUES (83, '红烧肉拌饭', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/1cec431e-7f10-44c2-8825-61cb8221587f_4affe5af5a712cb33a947946ac774b4.png', '202504191345479768', 45, 1, 18.8);
INSERT INTO `order_detail` VALUES (84, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '202504191354258948', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (85, '坚果500g', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ca391cb8-3dc8-4132-9b6e-485f7722f4c2_f9c9c4e660d621839a95b5a41ac1c02.png', '202504191358034002', 23, 1, 18.8);
INSERT INTO `order_detail` VALUES (86, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504191436514414', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (87, '人气汉堡套餐二', NULL, '202504191436514414', 6, 1, 22);
INSERT INTO `order_detail` VALUES (88, '红烧肉拌饭', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/1cec431e-7f10-44c2-8825-61cb8221587f_4affe5af5a712cb33a947946ac774b4.png', '202504191436514414', 45, 1, 18.8);
INSERT INTO `order_detail` VALUES (89, '超A葡萄冰', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/350d2c55-d7ab-4022-9703-f426a74b7183_17c29f766536474ce88d5748386fea3.jpg', '202504200823077433', 34, 1, 17.5);
INSERT INTO `order_detail` VALUES (90, '超A葡萄冰', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/350d2c55-d7ab-4022-9703-f426a74b7183_17c29f766536474ce88d5748386fea3.jpg', '202504201739235745', 34, 1, 17.5);
INSERT INTO `order_detail` VALUES (91, '蛋炒饭大份', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/aec5ff1f-e161-480e-8446-4efdfb92d35e_d57fa654d851b84198a9fe64acd9ec0.png', '202504201739235745', 44, 1, 15.8);
INSERT INTO `order_detail` VALUES (92, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504201739235745', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (93, '百香双重奏', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e46abccc-dd9f-4d05-b9db-cf18507e9f67_2534ab41d846bd3ba665ad5095c943f.jpg', '202504201739235745', 33, 3, 10.5);
INSERT INTO `order_detail` VALUES (94, '超A葡萄冰', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/350d2c55-d7ab-4022-9703-f426a74b7183_17c29f766536474ce88d5748386fea3.jpg', '202504201740522121', 34, 1, 17.5);
INSERT INTO `order_detail` VALUES (95, '蛋炒饭大份', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/aec5ff1f-e161-480e-8446-4efdfb92d35e_d57fa654d851b84198a9fe64acd9ec0.png', '202504201740522121', 44, 1, 15.8);
INSERT INTO `order_detail` VALUES (96, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504201740522121', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (97, '百香双重奏', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e46abccc-dd9f-4d05-b9db-cf18507e9f67_2534ab41d846bd3ba665ad5095c943f.jpg', '202504201740522121', 33, 3, 10.5);
INSERT INTO `order_detail` VALUES (98, '超A葡萄冰', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/350d2c55-d7ab-4022-9703-f426a74b7183_17c29f766536474ce88d5748386fea3.jpg', '202504201742271156', 34, 1, 17.5);
INSERT INTO `order_detail` VALUES (99, '蛋炒饭大份', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/aec5ff1f-e161-480e-8446-4efdfb92d35e_d57fa654d851b84198a9fe64acd9ec0.png', '202504201742271156', 44, 1, 15.8);
INSERT INTO `order_detail` VALUES (100, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504201742271156', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (101, '百香双重奏', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e46abccc-dd9f-4d05-b9db-cf18507e9f67_2534ab41d846bd3ba665ad5095c943f.jpg', '202504201742271156', 33, 3, 10.5);
INSERT INTO `order_detail` VALUES (102, '超A葡萄冰', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/350d2c55-d7ab-4022-9703-f426a74b7183_17c29f766536474ce88d5748386fea3.jpg', '202504201836228867', 34, 1, 17.5);
INSERT INTO `order_detail` VALUES (103, '蛋炒饭大份', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/aec5ff1f-e161-480e-8446-4efdfb92d35e_d57fa654d851b84198a9fe64acd9ec0.png', '202504201836228867', 44, 1, 15.8);
INSERT INTO `order_detail` VALUES (104, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202504201836228867', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (105, '百香双重奏', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e46abccc-dd9f-4d05-b9db-cf18507e9f67_2534ab41d846bd3ba665ad5095c943f.jpg', '202504201836228867', 33, 3, 10.5);
INSERT INTO `order_detail` VALUES (106, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504222202063488', 5, 2, 18);
INSERT INTO `order_detail` VALUES (107, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504231239204237', 5, 2, 18);
INSERT INTO `order_detail` VALUES (108, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231239204237', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (109, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504231239583896', 5, 2, 18);
INSERT INTO `order_detail` VALUES (110, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231239583896', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (111, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504231245113616', 5, 2, 18);
INSERT INTO `order_detail` VALUES (112, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231245113616', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (113, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504231246276682', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (114, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '202504231246464061', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (115, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231255174737', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (116, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231304217064', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (117, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '202504231304217064', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (118, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231315259333', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (119, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '202504231315259333', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (120, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504231319281593', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (121, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '202504231319281593', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (122, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504231322155637', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (123, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504231325252117', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (124, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504231329179686', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (125, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504231329179686', 20, 2, 18.5);
INSERT INTO `order_detail` VALUES (126, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504231329179686', 5, 1, 18);
INSERT INTO `order_detail` VALUES (127, '坚果500g', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ca391cb8-3dc8-4132-9b6e-485f7722f4c2_f9c9c4e660d621839a95b5a41ac1c02.png', '202504231337461466', 23, 2, 18.8);
INSERT INTO `order_detail` VALUES (128, '茉莉花茶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/c153f434-a625-40b1-b70c-bda1772bdcc1_419fdbc5b54db4f0d3b5e2c5ba2c9ad.jpg', '202504250850392793', 31, 1, 15);
INSERT INTO `order_detail` VALUES (129, '茉莉花茶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/c153f434-a625-40b1-b70c-bda1772bdcc1_419fdbc5b54db4f0d3b5e2c5ba2c9ad.jpg', '202504250859254321', 31, 1, 15);
INSERT INTO `order_detail` VALUES (130, '核桃仁500g', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/93b0eed3-5794-46e8-8439-6e8f90ca74f5_18ac5ed27b1dd8e195ceabbb496b1d1.png', '202504250923128256', 27, 1, 18.5);
INSERT INTO `order_detail` VALUES (131, '核桃仁500g', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/93b0eed3-5794-46e8-8439-6e8f90ca74f5_18ac5ed27b1dd8e195ceabbb496b1d1.png', '202504250924105058', 27, 1, 18.5);
INSERT INTO `order_detail` VALUES (132, '芝士绿茶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e2bec630-405b-47a5-ae7d-2c7a917e0ffc_8f0589578ffd95b736a777c65f13e07.png', '202504250924105058', 32, 1, 18.5);
INSERT INTO `order_detail` VALUES (133, '布蕾脆脆奶芙', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7ae82bbb-e132-4754-b878-a56a704f34f2_113803f558813143f909825bab4dcb9.jpg', '202504250926405508', 36, 1, 18);
INSERT INTO `order_detail` VALUES (134, '旺仔牛奶礼包', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/23f3180b-e5b5-4171-bf43-1255740bfbb3_6ce42ba1d72103be9b21697da8c21bc.jpg', '202504250926405508', 39, 1, 15.9);
INSERT INTO `order_detail` VALUES (135, '美汁源果粒奶优', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/75226675-4787-4c87-9293-689ae7ee3e2e_8e9660f30050fbd892bebc1f53bbc7a.png', '202504250926405508', 40, 1, 5);
INSERT INTO `order_detail` VALUES (136, '伊利 优酸乳原味250ml*24盒整箱', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7cd1e125-6db4-4c73-92f1-7b5c5114aa6c_abbc959f88082508fb8139f8dbf6c3c.png', '202504250926405508', 42, 1, 58);
INSERT INTO `order_detail` VALUES (137, '布蕾脆脆奶芙', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7ae82bbb-e132-4754-b878-a56a704f34f2_113803f558813143f909825bab4dcb9.jpg', '202504250926405508', 36, 1, 18);
INSERT INTO `order_detail` VALUES (138, '旺仔牛奶礼包', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/23f3180b-e5b5-4171-bf43-1255740bfbb3_6ce42ba1d72103be9b21697da8c21bc.jpg', '202504250926405508', 39, 1, 15.9);
INSERT INTO `order_detail` VALUES (139, '美汁源果粒奶优', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/75226675-4787-4c87-9293-689ae7ee3e2e_8e9660f30050fbd892bebc1f53bbc7a.png', '202504250926405508', 40, 1, 5);
INSERT INTO `order_detail` VALUES (140, '伊利 优酸乳原味250ml*24盒整箱', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7cd1e125-6db4-4c73-92f1-7b5c5114aa6c_abbc959f88082508fb8139f8dbf6c3c.png', '202504250926405508', 42, 1, 58);
INSERT INTO `order_detail` VALUES (141, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504250926405508', 20, 1, 18.5);
INSERT INTO `order_detail` VALUES (142, '铁板龙虾', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/0f9962c0-09b1-448a-9f4b-7b42dd098122_149bb4443b6936b22b8f1bf20312ca2.png', '202504250926405508', 20, 2, 18.5);
INSERT INTO `order_detail` VALUES (143, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202504250926405508', 5, 1, 18);
INSERT INTO `order_detail` VALUES (144, '核桃仁500g', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/93b0eed3-5794-46e8-8439-6e8f90ca74f5_18ac5ed27b1dd8e195ceabbb496b1d1.png', '202504250926405508', 27, 1, 18.5);
INSERT INTO `order_detail` VALUES (145, '芝士绿茶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e2bec630-405b-47a5-ae7d-2c7a917e0ffc_8f0589578ffd95b736a777c65f13e07.png', '202504250926405508', 32, 1, 18.5);
INSERT INTO `order_detail` VALUES (146, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504191358034002', 9, 2, 3.5);
INSERT INTO `order_detail` VALUES (147, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202504191358034002', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (148, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041603004498', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (149, '核桃仁500g', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/93b0eed3-5794-46e8-8439-6e8f90ca74f5_18ac5ed27b1dd8e195ceabbb496b1d1.png', '202505041605271430', 27, 1, 18.5);
INSERT INTO `order_detail` VALUES (150, '芝士绿茶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e2bec630-405b-47a5-ae7d-2c7a917e0ffc_8f0589578ffd95b736a777c65f13e07.png', '202505041605271430', 32, 1, 18.5);
INSERT INTO `order_detail` VALUES (151, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202505041605271430', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (152, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202505041605271430', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (153, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041605271430', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (154, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202505041605271430', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (155, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041605271430', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (156, '草莓蛋糕', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', '202505041706599584', 4, 1, 8);
INSERT INTO `order_detail` VALUES (157, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '202505041706599584', 5, 1, 18);
INSERT INTO `order_detail` VALUES (158, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041706599584', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (159, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041706599584', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (160, '红烧鲤鱼', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/510ab72a-8d19-4a64-9168-68cd682ad417_eb2bf2ffbe2605e417d9b02bb1caeca.png', '202505041706599584', 17, 1, 18.5);
INSERT INTO `order_detail` VALUES (161, '香辣鸡腿堡', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5419a5fd-eef9-48fe-9f7d-b9c0c27fc4e6_92baf6ea0d3fda6e2629bb340f95314.png', '20250504172403507', 18, 1, 10.5);
INSERT INTO `order_detail` VALUES (162, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041745177911', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (163, '瓶装牛奶', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/bc4e6373-41d3-41c8-ba63-8d004d03808c_1218303755a8076cd4c6ee15246aaac.png', '202505041759467997', 9, 1, 3.5);
INSERT INTO `order_detail` VALUES (164, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '202505142036272913', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (165, '水果沙拉', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/a020f83f-91bb-48dc-bda1-3642104fe445_d92afde6dfd76f30e7707df46ef8fcc.png', '2025051422065132', 14, 1, 16.8);
INSERT INTO `order_detail` VALUES (166, '铁锅牛肉面', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/5259fa26-39f1-4e2e-8cad-0ad2ca180cb6_轮播图3.jpg', '2025051422065132', 5, 1, 18);

-- ----------------------------
-- Table structure for setmeal
-- ----------------------------
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `price` double NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setmeal
-- ----------------------------
INSERT INTO `setmeal` VALUES (5, '人气汉堡套餐一', 63.4, 0, '双层香辣鸡腿堡+香酥鸡排+黑椒鸡块+可乐', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7baf7f83-2097-42bf-a901-6b8fd4b2f2ad_1b39a65b5a3f41108b6e36ab46c8870.jpg');
INSERT INTO `setmeal` VALUES (6, '人气汉堡套餐二', 47, 0, '双层香辣鸡腿堡+香酥鸡排+可乐', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/7baf7f83-2097-42bf-a901-6b8fd4b2f2ad_1b39a65b5a3f41108b6e36ab46c8870.jpg');

-- ----------------------------
-- Table structure for setmeal_dish
-- ----------------------------
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `setmeal_id` int NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `price` double NULL DEFAULT NULL,
  `acount` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setmeal_dish
-- ----------------------------
INSERT INTO `setmeal_dish` VALUES (53, '盒装牛奶', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/288975bb-af0b-47b8-9893-fc2855e81042_11288715356_991353753.jpg', 4.5, 3);
INSERT INTO `setmeal_dish` VALUES (54, '草莓蛋糕', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/ef64d76f-3307-4dec-8f6a-a74bda601f51_OIP-C (3).jpg', 8.5, 2);
INSERT INTO `setmeal_dish` VALUES (55, '瓶装牛奶', 1, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e9cd98a8-ef18-4af0-8a37-4321884638c2_05ee6765dd12ab28ddc67d5b04f5165.png', 3.5, 2);
INSERT INTO `setmeal_dish` VALUES (56, '瓶装牛奶', 3, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/e9cd98a8-ef18-4af0-8a37-4321884638c2_05ee6765dd12ab28ddc67d5b04f5165.png', 3.5, 1);
INSERT INTO `setmeal_dish` VALUES (57, '盒装牛奶', 3, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/288975bb-af0b-47b8-9893-fc2855e81042_11288715356_991353753.jpg', 4, 2);
INSERT INTO `setmeal_dish` VALUES (116, '红烧肉拌饭', 5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/1cec431e-7f10-44c2-8825-61cb8221587f_4affe5af5a712cb33a947946ac774b4.png', 18.8, 1);
INSERT INTO `setmeal_dish` VALUES (117, '西红柿鸡蛋盖饭', 5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/285b4e04-d496-476b-9550-a1086f9b6d40_93440c96086b348f07a01861ac6218c.jpg', 11, 1);
INSERT INTO `setmeal_dish` VALUES (118, '可乐鸡块盖饭', 5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/1291c079-4d78-4ffa-8740-75c103a72c81_691e71229a689cb3b6e041806ac2da2.jpg', 15, 1);
INSERT INTO `setmeal_dish` VALUES (119, '烤肉拌饭', 5, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/6783ba60-76e6-455f-92f5-56c0a66f547d_21a24cff2d70f8786a8326cc9d03640.jpg', 18.6, 1);
INSERT INTO `setmeal_dish` VALUES (129, '双层香辣鸡腿堡', 6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/37cc8e43-3a34-45a1-b67b-8305a69550d4_dcf2778d82eaaf9b797a829a9cb0a7b.png', 13, 2);
INSERT INTO `setmeal_dish` VALUES (130, '小杯可乐', 6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4ef04083-192a-4d10-9b8c-fcbcddbf5865_69e2b7c5ec55d45d8f70877bb62afb6.png', 3, 3);
INSERT INTO `setmeal_dish` VALUES (131, '香酥鸡排', 6, 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/8b19a30f-3993-4ee2-aae4-31655997ff52_96c251f536dc0c82ec52a9fe99d0c72.jpg', 6, 2);

-- ----------------------------
-- Table structure for sick_voucher
-- ----------------------------
DROP TABLE IF EXISTS `sick_voucher`;
CREATE TABLE `sick_voucher`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `voucher_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sick_voucher
-- ----------------------------
INSERT INTO `sick_voucher` VALUES (1, 3, 1);
INSERT INTO `sick_voucher` VALUES (2, 1, 1);
INSERT INTO `sick_voucher` VALUES (3, 1, 6);
INSERT INTO `sick_voucher` VALUES (4, 1, 7);
INSERT INTO `sick_voucher` VALUES (5, 3, 7);

-- ----------------------------
-- Table structure for taste
-- ----------------------------
DROP TABLE IF EXISTS `taste`;
CREATE TABLE `taste`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `taste` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taste
-- ----------------------------

-- ----------------------------
-- Table structure for taste_user
-- ----------------------------
DROP TABLE IF EXISTS `taste_user`;
CREATE TABLE `taste_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `taste_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of taste_user
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `photo` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `register_time` datetime NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `identity` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'lwl', '1234', 'lwl', '17484638372', 'https://ts3.tc.mm.bing.net/th/id/OIP-C.y6rdSCGpxbfeb8Rd1CpSuwAAAA?w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2', '2025-04-08 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (2, 'user1', '1234', 'user1', '16737483654', 'https://ts3.tc.mm.bing.net/th/id/OIP-C.y6rdSCGpxbfeb8Rd1CpSuwAAAA?w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2', '2025-04-09 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (3, 'user2', '1234', 'user2', '16738363722', 'https://ts3.tc.mm.bing.net/th/id/OIP-C.y6rdSCGpxbfeb8Rd1CpSuwAAAA?w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2', '2025-04-10 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (4, 'user_3', '1234', 'user_3', '18930738267', 'https://ts3.tc.mm.bing.net/th/id/OIP-C.y6rdSCGpxbfeb8Rd1CpSuwAAAA?w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2', '2025-04-11 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (5, 'lzx', '123456', 'lzx', '19271759490', 'https://ts2.tc.mm.bing.net/th/id/OIP-C.nKetvjjSggVKwC55M-AzUwAAAA?w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2', '2025-04-12 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (6, 'ysc', '1234', 'ysc', '16738748378', 'https://img.ixintu.com/download/jpg/20200910/f9256155491e54bf5e99bf29eece0156_512_512.jpg!ys', '2025-04-13 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (7, 'yxc', '1234', 'yxc', '16738637467', 'https://ts3.tc.mm.bing.net/th/id/OIP-C.LzpO_tvwNEQQ0JqYLQHJNgAAAA?cb=iwc1&rs=1&pid=ImgDetMain', '2025-04-15 17:15:49', NULL, NULL);
INSERT INTO `user` VALUES (8, 'ljh', '1234', 'ljh', '16784736728', 'https://img.ixintu.com/download/jpg/20200910/f9256155491e54bf5e99bf29eece0156_512_512.jpg!ys', '2025-04-17 22:06:48', NULL, NULL);
INSERT INTO `user` VALUES (9, 'lwq', '1234', 'lwq', '16737472872', 'https://img.ixintu.com/download/jpg/20200910/f9256155491e54bf5e99bf29eece0156_512_512.jpg!ys', '2025-04-20 19:26:58', NULL, NULL);

-- ----------------------------
-- Table structure for voucher
-- ----------------------------
DROP TABLE IF EXISTS `voucher`;
CREATE TABLE `voucher`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ruler` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `used_time` datetime NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `number` int NULL DEFAULT NULL,
  `remain` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of voucher
-- ----------------------------
INSERT INTO `voucher` VALUES (1, '50元无门槛优惠券', '全场通用、n无需预约、可无限叠加、不兑现、不找零、仅限堂食', '2025-05-28 11:30:19', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/11f4fec4-08c3-4c70-b50f-673c8b4ece53_f5d1d5e7337ecf756882f1b033e1224.png', 100, 100);
INSERT INTO `voucher` VALUES (3, '10元无门槛优惠券', '全场通用、n无需预约、可无限叠加、不兑现、不找零、仅限堂食', '2025-05-28 11:30:19', 'https://sky-tak-eout.oss-cn-beijing.aliyuncs.com/images/4cc1e09c-ed40-41c9-ac01-6fa5efaf51ba_f899e82facbe0aa0d109a103ff5d1e8.png', 979, 968);

-- ----------------------------
-- Table structure for voucher_user
-- ----------------------------
DROP TABLE IF EXISTS `voucher_user`;
CREATE TABLE `voucher_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `voucher_id` int NULL DEFAULT NULL,
  `acount` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of voucher_user
-- ----------------------------
INSERT INTO `voucher_user` VALUES (3, 3, 1, 4);
INSERT INTO `voucher_user` VALUES (4, 3, 1, 5);
INSERT INTO `voucher_user` VALUES (22, 1, 3, 1);
INSERT INTO `voucher_user` VALUES (25, 1, 1, 6);
INSERT INTO `voucher_user` VALUES (26, 1, 1, 7);
INSERT INTO `voucher_user` VALUES (28, 3, 1, 7);
INSERT INTO `voucher_user` VALUES (29, 3, 1, 9);

-- ----------------------------
-- Table structure for worker
-- ----------------------------
DROP TABLE IF EXISTS `worker`;
CREATE TABLE `worker`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `age` int NULL DEFAULT NULL,
  `position` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of worker
-- ----------------------------
INSERT INTO `worker` VALUES (1, '张三', 25, '软件工程师');
INSERT INTO `worker` VALUES (3, 'lzx', 21, '实习生');
INSERT INTO `worker` VALUES (4, '赵六', 22, '实习生');
INSERT INTO `worker` VALUES (5, '孙七', 35, '技术总监');
INSERT INTO `worker` VALUES (6, '周八', 26, '前端开发工程师');
INSERT INTO `worker` VALUES (7, '吴九', 29, '后端开发工程师');
INSERT INTO `worker` VALUES (8, '郑十', 23, '数据分析师');
INSERT INTO `worker` VALUES (9, '王十一', 32, '产品经理');
INSERT INTO `worker` VALUES (10, '李十二', 27, '运维工程师');
INSERT INTO `worker` VALUES (11, 'lzx', 21, '实习生');
INSERT INTO `worker` VALUES (12, 'lzx', 21, '实习生');

SET FOREIGN_KEY_CHECKS = 1;



个人认为该项目对初级开发人员以及想要入职后端开发或者寻找实习的学弟学妹或社会人士会有一定的帮助，对项目有任何疑问以及建议可以联系我QQ:3396284803，真心希望项目对大家有所帮助，也欢迎各位大佬给出优化意见！
