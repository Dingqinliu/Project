CREATE DATABASE IF NOT EXISTS ting_fm CHARSET utf8mb4;

CREATE TABLEIF NOT EXISTS `album` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `brief` varchar(200) NOT NULL,
  `cover` varchar(200) NOT NULL,
  `header` varchar(200) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `story` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `audio` longblob NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `nickname` varchar(45) NOT NULL,
  `password` char(128) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
