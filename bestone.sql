-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.13 - MySQL Community Server - GPL
-- 服务器OS:                        Win64
-- HeidiSQL 版本:                  10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for bestone
CREATE DATABASE IF NOT EXISTS `bestone` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `bestone`;

-- Dumping structure for table bestone.tb_blog
CREATE TABLE IF NOT EXISTS `tb_blog` (
  `bid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `author` varchar(150) DEFAULT NULL COMMENT '作者',
  `title` varchar(500) DEFAULT NULL COMMENT '标题',
  `content` varchar(2000) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Blog';

-- Dumping data for table bestone.tb_blog: ~2 rows (大约)
/*!40000 ALTER TABLE `tb_blog` DISABLE KEYS */;
INSERT INTO `tb_blog` (`bid`, `author`, `title`, `content`) VALUES
	(1, '朱之文', '晴天雨天', '这是一个晴空万里与瓢泼大雨的奇怪日子。'),
	(2, '马克.李文', '偷影子的人', '你偷走了我的影子，不论你在哪里，我都会一直想着你。');
/*!40000 ALTER TABLE `tb_blog` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
