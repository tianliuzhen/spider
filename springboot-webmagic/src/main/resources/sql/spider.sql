/*
 Navicat Premium Data Transfer

 Source Server         : 本地localhost
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : spider

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 16/04/2020 18:58:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for art_type_info
-- ----------------------------
DROP TABLE IF EXISTS `sx_art_type_info`;
CREATE TABLE `sx_art_type_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `detail_html` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sx_type_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `art_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sx_index
-- ----------------------------
DROP TABLE IF EXISTS `sx_index`;
CREATE TABLE `sx_index`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `src_roll` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '{\r\n  \"mainUrl\": \"\",\r\n  \"srcUrl1\": \"\",\r\n  \"srcTitle1\": \"\",\r\n  \"srcUrl2\": \"\",\r\n  \"srcTitle2\": \"\",\r\n  \"srcUrl3\": \"\",\r\n  \"srcTitle3\": \"\"',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sx_type
-- ----------------------------
DROP TABLE IF EXISTS `sx_type`;
CREATE TABLE `sx_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NULL DEFAULT NULL COMMENT '类型编码 （1,2,3,4）',
  `sx_type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `sx_type_href` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超链接',
  `img_src1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型图片1',
  `img_src2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型图片2',
  `list` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型右侧，广告导航标题{\r\n  \"sxArtTitle\": \"\",\r\n  \"sxArtHref\": \"\",\r\n  \"sxArtUtil\": \"\"\r\n}',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sx_type_list
-- ----------------------------
DROP TABLE IF EXISTS `sx_type_list`;
CREATE TABLE `sx_type_list`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `href` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sx_type_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `art_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 553 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
