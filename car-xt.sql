-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: car-xt
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `evaluations`
--

DROP TABLE IF EXISTS `evaluations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluations` (
  `evaluation_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `parking_lot_id` int DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `COMMENT` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`evaluation_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluations`
--

LOCK TABLES `evaluations` WRITE;
/*!40000 ALTER TABLE `evaluations` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_lots`
--

DROP TABLE IF EXISTS `parking_lots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_lots` (
  `parking_lot_id` int NOT NULL AUTO_INCREMENT,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '位置',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `status` tinyint DEFAULT '1' COMMENT '状态',
  `owner_id` int DEFAULT NULL COMMENT '所有者id',
  `available_from` datetime DEFAULT NULL COMMENT '可用起始时间',
  `available_to` datetime DEFAULT NULL COMMENT '可用结束时间',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `image` varbinary(255) DEFAULT NULL COMMENT '图片',
  `sum` int DEFAULT '0' COMMENT '租用次数',
  `user_id` int DEFAULT NULL COMMENT '租用人id',
  `owner_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所有者姓名',
  `owner_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '所有者手机号',
  PRIMARY KEY (`parking_lot_id`) USING BTREE,
  KEY `owner_id` (`owner_id`) USING BTREE,
  KEY `image` (`image`) USING BTREE,
  CONSTRAINT `PK_parking_lots_owner_id` FOREIGN KEY (`owner_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_lots`
--

LOCK TABLES `parking_lots` WRITE;
/*!40000 ALTER TABLE `parking_lots` DISABLE KEYS */;
INSERT INTO `parking_lots` VALUES (8,'南望山',6.00,1,4,'2023-06-01 00:00:00','2023-07-31 23:59:59','测试2',NULL,0,NULL,'管理员','17814562133'),(9,'中国地质大学（未来城校区）',1.00,1,4,'2023-06-01 00:00:00','2024-07-01 23:59:59','测试1',NULL,0,NULL,'管理员','17814562133'),(10,'华中科技大学',2.00,1,4,'2023-06-01 00:00:00','2023-08-01 23:59:59','测试3',_binary 'http://localhost:9090/file/b28066887a97401d876e8bff961bebb1.jpg',0,NULL,'管理员','17814562133');
/*!40000 ALTER TABLE `parking_lots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `reservation_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `parking_lot_id` int DEFAULT NULL COMMENT '停车场id',
  `reservation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '预订时间',
  `start_time` datetime DEFAULT NULL COMMENT '预订开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '预订结束时间',
  `status` tinyint DEFAULT '1' COMMENT '状态',
  `license_plate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '车牌号',
  PRIMARY KEY (`reservation_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `parking_lot_id` (`parking_lot_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `value` (`value`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1,'user','el-icon-user','icon'),(2,'house','el-icon-house','icon'),(3,'menu','el-icon-menu','icon'),(4,'s-custom','el-icon-s-custom','icon'),(5,'s-grid','el-icon-s-grid','icon'),(6,'document','el-icon-document','icon'),(7,'coffee','el-icon-coffee\r\n','icon'),(8,'s-marketing','el-icon-s-marketing','icon'),(9,'phone-outline','el-icon-phone-outline','icon'),(10,'platform-eleme','el-icon-platform-eleme','icon'),(11,'eleme','el-icon-eleme','icon'),(12,'delete-solid','el-icon-delete-solid','icon'),(13,'delete','el-icon-delete','icon'),(14,'s-tools','el-icon-s-tools','icon'),(15,'setting','el-icon-setting','icon'),(16,'user-solid','el-icon-user-solid','icon'),(17,'phone','el-icon-phone','icon'),(18,'more','el-icon-more','icon'),(19,'more-outline','el-icon-more-outline','icon'),(20,'star-on','el-icon-star-on','icon'),(21,'star-off','el-icon-star-off','icon'),(22,'s-goods','el-icon-s-goods','icon'),(23,'goods','el-icon-goods','icon'),(24,'warning','el-icon-warning','icon'),(25,'warning-outline','el-icon-warning-outline','icon'),(26,'question','el-icon-question','icon'),(27,'info','el-icon-info','icon'),(28,'remove','el-icon-remove','icon'),(29,'circle-plus','el-icon-circle-plus','icon'),(30,'success','el-icon-success','icon'),(31,'error','el-icon-error','icon'),(32,'zoom-in','el-icon-zoom-in','icon'),(33,'zoom-out','el-icon-zoom-out','icon'),(34,'remove-outline','el-icon-remove-outline','icon'),(35,'circle-plus-outline','el-icon-circle-plus-outline','icon'),(36,'circle-check','el-icon-circle-check','icon'),(37,'circle-close','el-icon-circle-close','icon'),(38,'s-help','el-icon-s-help','icon'),(39,'help','el-icon-help','icon'),(40,'minus','el-icon-minus','icon'),(41,'plus','el-icon-plus','icon'),(42,'check','el-icon-check','icon'),(43,'close','el-icon-close','icon'),(44,'picture','el-icon-picture','icon'),(45,'picture-outline','el-icon-picture-outline','icon'),(46,'picture-outline-round','el-icon-picture-outline-round','icon'),(47,'upload','el-icon-upload','icon'),(48,'upload2','el-icon-upload2','icon'),(49,'download','el-icon-download','icon'),(50,'camera-solid','el-icon-camera-solid','icon'),(51,'camera','el-icon-camera','icon'),(52,'video-camera-solid','el-icon-video-camera-solid','icon'),(53,'video-camera','el-icon-video-camera','icon'),(54,'message-solid','el-icon-message-solid','icon'),(55,'bell','el-icon-bell','icon'),(56,'s-cooperation','el-icon-s-cooperation','icon'),(57,'s-order','el-icon-s-order','icon'),(58,'s-platform','el-icon-s-platform','icon'),(59,'s-fold','el-icon-s-fold','icon'),(60,'s-unfold','el-icon-s-unfold','icon'),(61,'s-operation','el-icon-s-operation','icon'),(62,'s-promotion','el-icon-s-promotion','icon'),(63,'s-home','el-icon-s-home','icon'),(64,'s-release','el-icon-s-release','icon'),(65,'s-ticket','el-icon-s-ticket','icon'),(66,'s-management','el-icon-s-management','icon'),(67,'s-open','el-icon-s-open','icon'),(68,'s-shop','el-icon-s-shop','icon'),(69,'s-flag','el-icon-s-flag','icon'),(70,'s-comment','el-icon-s-comment','icon'),(71,'s-finance','el-icon-s-finance','icon'),(72,'s-claim','el-icon-s-claim','icon'),(73,'s-opportunity','el-icon-s-opportunity','icon'),(74,'s-data','el-icon-s-data','icon'),(75,'s-check','el-icon-s-check','icon'),(76,'share','el-icon-share','icon'),(77,'d-caret','el-icon-d-caret','icon'),(78,'caret-left','el-icon-caret-left','icon'),(79,'caret-right','el-icon-caret-right','icon'),(80,'caret-bottom','el-icon-caret-bottom','icon'),(81,'caret-top','el-icon-caret-top','icon'),(82,'bottom-left','el-icon-bottom-left','icon'),(83,'bottom-right','el-icon-bottom-right','icon'),(84,'back','el-icon-back','icon'),(85,'right','el-icon-right','icon'),(86,'bottom','el-icon-bottom','icon'),(87,'top','el-icon-top','icon'),(88,'top-left','el-icon-top-left','icon'),(89,'top-right','el-icon-top-right','icon'),(90,'arrow-left','el-icon-arrow-left','icon'),(91,'arrow-right','el-icon-arrow-right','icon'),(92,'arrow-down','el-icon-arrow-down','icon'),(93,'arrow-up','el-icon-arrow-up','icon'),(94,'d-arrow-left','el-icon-d-arrow-left','icon'),(95,'d-arrow-right','el-icon-d-arrow-right','icon'),(96,'video-pause','el-icon-video-pause','icon'),(97,'video-play','el-icon-video-play','icon'),(98,'refresh','el-icon-refresh','icon'),(99,'refresh-right','el-icon-refresh-right','icon'),(100,'refresh-left','el-icon-refresh-left','icon'),(101,'finished','el-icon-finished','icon'),(102,'sort','el-icon-sort','icon'),(103,'sort-up','el-icon-sort-up','icon'),(104,'sort-down','el-icon-sort-down','icon'),(105,'rank','el-icon-rank','icon'),(106,'loading','el-icon-loading','icon'),(107,'view','el-icon-view','icon'),(108,'c-scale-to-original','el-icon-c-scale-to-original','icon'),(109,'date','el-icon-date','icon'),(110,'edit','el-icon-edit','icon'),(111,'edit-outline','el-icon-edit-outline','icon'),(112,'folder','el-icon-folder','icon'),(113,'folder-opened','el-icon-folder-opened','icon'),(114,'folder-add','el-icon-folder-add','icon'),(115,'folder-remove','el-icon-folder-remove','icon'),(116,'folder-delete','el-icon-folder-delete','icon'),(117,'folder-checked','el-icon-folder-checked','icon'),(118,'tickets','el-icon-tickets','icon'),(119,'document-remove','el-icon-document-remove','icon'),(120,'document-delete','el-icon-document-delete','icon'),(121,'document-copy','el-icon-document-copy','icon'),(122,'document-checked','el-icon-document-checked','icon'),(123,'document-add','el-icon-document-add','icon'),(124,'printer','el-icon-printer','icon'),(125,'paperclip','el-icon-paperclip','icon'),(126,'takeaway-box','el-icon-takeaway-box','icon'),(127,'search','el-icon-search','icon'),(128,'monitor','el-icon-monitor','icon'),(129,'attract','el-icon-attract','icon'),(130,'mobile','el-icon-mobile','icon'),(131,'scissors','el-icon-scissors','icon'),(132,'umbrella','el-icon-umbrella','icon'),(133,'headset','el-icon-headset','icon'),(134,'brush','el-icon-brush','icon'),(135,'mouse','el-icon-mouse','icon'),(136,'coordinate','el-icon-coordinate','icon'),(137,'magic-stick','el-icon-magic-stick','icon'),(138,'reading','el-icon-reading','icon'),(139,'data-line','el-icon-data-line','icon'),(140,'data-board','el-icon-data-board','icon'),(141,'pie-chart','el-icon-pie-chart','icon'),(142,'data-analysis','el-icon-data-analysis','icon'),(143,'collection-tag','el-icon-collection-tag','icon'),(144,'film','el-icon-film','icon'),(145,'suitcase','el-icon-suitcase','icon'),(146,'suitcase-1','el-icon-suitcase-1','icon'),(147,'receiving','el-icon-receiving','icon'),(148,'collection','el-icon-collection','icon'),(149,'files','el-icon-files','icon'),(150,'notebook-1','el-icon-notebook-1','icon'),(151,'notebook-2','el-icon-notebook-2','icon'),(152,'toilet-paper','el-icon-toilet-paper','icon'),(153,'office-building','el-icon-office-building','icon'),(154,'school','el-icon-school','icon'),(155,'table-lamp','el-icon-table-lamp','icon'),(156,'no-smoking','el-icon-no-smoking','icon'),(157,'smoking','el-icon-smoking','icon'),(158,'shopping-cart-full','el-icon-shopping-cart-full','icon'),(159,'shopping-cart-1','el-icon-shopping-cart-1','icon'),(160,'shopping-cart-2','el-icon-shopping-cart-2','icon'),(161,'shopping-bag-1','el-icon-shopping-bag-1','icon'),(162,'shopping-bag-2','el-icon-shopping-bag-2','icon'),(163,'sold-out','el-icon-sold-out','icon'),(164,'sell','el-icon-sell','icon'),(165,'present','el-icon-present','icon'),(166,'box','el-icon-box','icon'),(167,'bank-card','el-icon-bank-card','icon'),(168,'money','el-icon-money','icon'),(169,'coin','el-icon-coin','icon'),(170,'wallet','el-icon-wallet','icon'),(171,'discount','el-icon-discount','icon'),(172,'price-tag','el-icon-price-tag','icon'),(173,'news','el-icon-news','icon'),(174,'guide','el-icon-guide','icon'),(175,'male','el-icon-male','icon'),(176,'female','el-icon-female','icon'),(177,'thumb','el-icon-thumb','icon'),(178,'cpu','el-icon-cpu','icon'),(179,'link','el-icon-link','icon'),(180,'connection','el-icon-connection','icon'),(181,'open','el-icon-open','icon'),(182,'turn-off','el-icon-turn-off','icon'),(183,'set-up','el-icon-set-up','icon'),(184,'chat-round','el-icon-chat-round','icon'),(185,'chat-line-round','el-icon-chat-line-round','icon'),(186,'chat-square','el-icon-chat-square','icon'),(187,'chat-dot-round','el-icon-chat-dot-round','icon'),(188,'chat-dot-square','el-icon-chat-dot-square','icon'),(189,'chat-line-square','el-icon-chat-line-square','icon'),(190,'message','el-icon-message','icon'),(191,'postcard','el-icon-postcard','icon'),(192,'position','el-icon-position','icon'),(193,'turn-off-microphone','el-icon-turn-off-microphone','icon'),(194,'microphone','el-icon-microphone','icon'),(195,'close-notification','el-icon-close-notification','icon'),(196,'bangzhu','el-icon-bangzhu','icon'),(197,'time','el-icon-time','icon'),(198,'odometer','el-icon-odometer','icon'),(199,'crop','el-icon-crop','icon'),(200,'aim','el-icon-aim','icon'),(201,'switch-button','el-icon-switch-button','icon'),(202,'full-screen','el-icon-full-screen','icon'),(203,'copy-document','el-icon-copy-document','icon'),(204,'mic','el-icon-mic','icon'),(205,'stopwatch','el-icon-stopwatch','icon'),(206,'medal-1','el-icon-medal-1','icon'),(207,'medal','el-icon-medal','icon'),(208,'trophy','el-icon-trophy','icon'),(209,'trophy-1','el-icon-trophy-1','icon'),(210,'first-aid-kit','el-icon-first-aid-kit','icon'),(211,'discover','el-icon-discover','icon'),(212,'place','el-icon-place','icon'),(213,'location','el-icon-location','icon'),(214,'location-outline','el-icon-location-outline','icon'),(215,'location-information','el-icon-location-information','icon'),(216,'add-location','el-icon-add-location','icon'),(217,'delete-location','el-icon-delete-location','icon'),(218,'map-location','el-icon-map-location','icon'),(219,'alarm-clock','el-icon-alarm-clock','icon'),(220,'timer','el-icon-timer','icon'),(221,'watch-1','el-icon-watch-1','icon'),(222,'watch','el-icon-watch','icon'),(223,'lock','el-icon-lock','icon'),(224,'unlock','el-icon-unlock','icon'),(225,'key','el-icon-key','icon'),(226,'service','el-icon-service','icon'),(227,'mobile-phone','el-icon-mobile-phone','icon'),(228,'bicycle','el-icon-bicycle','icon'),(229,'truck','el-icon-truck','icon'),(230,'ship','el-icon-ship','icon'),(231,'basketball','el-icon-basketball','icon'),(232,'football','el-icon-football','icon'),(233,'soccer','el-icon-soccer','icon'),(234,'baseball','el-icon-baseball','icon'),(235,'wind-power','el-icon-wind-power','icon'),(236,'light-rain','el-icon-light-rain','icon'),(237,'lightning','el-icon-lightning','icon'),(238,'heavy-rain','el-icon-heavy-rain','icon'),(239,'sunrise','el-icon-sunrise','icon'),(240,'sunrise-1','el-icon-sunrise-1','icon'),(241,'sunset','el-icon-sunset','icon'),(242,'sunny','el-icon-sunny','icon'),(243,'cloudy','el-icon-cloudy','icon'),(244,'partly-cloudy','el-icon-partly-cloudy','icon'),(245,'cloudy-and-sunny','el-icon-cloudy-and-sunny','icon'),(246,'moon','el-icon-moon','icon'),(247,'moon-night','el-icon-moon-night','icon'),(248,'dish','el-icon-dish','icon'),(249,'dish-1','el-icon-dish-1','icon'),(250,'food','el-icon-food','icon'),(251,'chicken','el-icon-chicken','icon'),(252,'fork-spoon','el-icon-fork-spoon','icon'),(253,'knife-fork','el-icon-knife-fork','icon'),(254,'burger','el-icon-burger','icon'),(255,'tableware','el-icon-tableware','icon'),(256,'sugar','el-icon-sugar','icon'),(257,'dessert','el-icon-dessert','icon'),(258,'ice-cream','el-icon-ice-cream','icon'),(259,'hot-water','el-icon-hot-water','icon'),(260,'water-cup','el-icon-water-cup','icon'),(261,'coffee-cup','el-icon-coffee-cup','icon'),(262,'cold-drink','el-icon-cold-drink','icon'),(263,'goblet','el-icon-goblet','icon'),(264,'goblet-full','el-icon-goblet-full','icon'),(265,'goblet-square','el-icon-goblet-square','icon'),(266,'goblet-square-full','el-icon-goblet-square-full','icon'),(267,'refrigerator','el-icon-refrigerator','icon'),(268,'grape','el-icon-grape','icon'),(269,'watermelon','el-icon-watermelon','icon'),(270,'cherry','el-icon-cherry','icon'),(271,'apple','el-icon-apple','icon'),(272,'pear','el-icon-pear','icon'),(273,'orange','el-icon-orange','icon'),(274,'ice-tea','el-icon-ice-tea','icon'),(275,'ice-drink','el-icon-ice-drink','icon'),(276,'milk-tea','el-icon-milk-tea','icon'),(277,'potato-strips','el-icon-potato-strips','icon'),(278,'lollipop','el-icon-lollipop','icon'),(279,'ice-cream-square','el-icon-ice-cream-square','icon'),(280,'ice-cream-round','el-icon-ice-cream-round','icon');
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_file`
--

DROP TABLE IF EXISTS `sys_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件类型',
  `size` bigint DEFAULT NULL COMMENT '文件大小(kb)',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下载链接',
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件md5',
  `enable` tinyint(1) DEFAULT '1' COMMENT '是否禁用链接',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `url` (`url`) USING BTREE,
  KEY `url_2` (`url`,`md5`) USING BTREE,
  KEY `md5` (`md5`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_file`
--

LOCK TABLES `sys_file` WRITE;
/*!40000 ALTER TABLE `sys_file` DISABLE KEYS */;
INSERT INTO `sys_file` VALUES (30,'OIP-C (1).jpg','jpg',34,'http://localhost:9090/file/4f6e995a02f04bf7a5f2eab69091d13f.jpg','b0e07d77232b1b31b897f82fd879acf4',1),(31,'下载.jpg','jpg',22,'http://localhost:9090/file/f4ffc3b661c74ef681b9efb2dff96075.jpg','cddeaf1f8de05a9d9ff4f6a0edf19727',1),(33,'下载 (1).jpg','jpg',48,'http://localhost:9090/file/b28066887a97401d876e8bff961bebb1.jpg','3089d9c272bdd0337af4d1449777818b',1),(34,'OIP-C.jpg','jpg',32,'http://localhost:9090/file/b086c277531f47f1a052bedadec96bca.jpg','35b4bfd02ff3d004046390760faea44c',1),(40,'下载 (1).jpg','jpg',48,'http://localhost:9090/file/b28066887a97401d876e8bff961bebb1.jpg','3089d9c272bdd0337af4d1449777818b',1);
/*!40000 ALTER TABLE `sys_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路径',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图标',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `pid` int DEFAULT NULL COMMENT '父级id',
  `page_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '页面路径',
  `sort_num` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `PK_icon` (`icon`) USING BTREE,
  CONSTRAINT `PK_icon` FOREIGN KEY (`icon`) REFERENCES `sys_dict` (`value`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (4,'系统管理',NULL,'el-icon-s-grid',NULL,NULL,NULL,300),(5,'用户管理','/user','el-icon-user',NULL,4,'User',301),(6,'角色管理','/role','el-icon-s-custom',NULL,4,'Role',302),(7,'菜单管理','/menu','el-icon-menu',NULL,4,'Menu',303),(8,'文件管理','/file','el-icon-document',NULL,4,'File',304),(10,'主页','/home','el-icon-house',NULL,NULL,'Home',0),(13,'车位共享信息','/parkingLots','el-icon-menu',NULL,NULL,'ParkingLots',1),(14,'车位信息维护','/reservations','el-icon-menu',NULL,NULL,'Reservations',2);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `flag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'管理员','管理员','ROLE_ADMIN'),(2,'普通用户','普通用户','ROLE_USER');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` int NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE,
  KEY `PK_menu_id` (`menu_id`) USING BTREE,
  CONSTRAINT `PK_menu_id` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `PK_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,4),(1,5),(1,6),(1,7),(1,8),(1,10),(2,10),(1,13),(2,13),(1,14);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `CHK_sys_user_email` CHECK ((`email` like _utf8mb4'%@%'))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (4,'admin','eeab9c459ae99aec1294e755906497c6','管理员','747439409@qq.com','17814562133',NULL,'2023-04-29 00:11:40','','ROLE_ADMIN'),(5,'test','eeab9c459ae99aec1294e755906497c6','测试用户',NULL,'15527691952',NULL,'2023-06-10 08:17:38','http://localhost:9090/file/683aef130594471dbe74de32e391acb0.png','ROLE_USER');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-30  0:12:25
