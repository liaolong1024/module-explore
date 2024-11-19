CREATE DATABASE learn_self;

USE learn_self;

DROP TABLE IF EXISTS t_task;
CREATE TABLE t_task (
  `taskId` VARCHAR(64) CHAR SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL PRIMARY KEY,
  `taskName` VARCHAR(128) CHAR SET utf8mb4 COLLATE utf8mb4_general_ci
);