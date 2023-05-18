-- Drop user first if they exist
DROP USER if exists 'resttest'@'localhost' ;

-- Now create user with prop privileges
CREATE USER 'resttest'@'localhost' IDENTIFIED BY 'resttest';

GRANT ALL PRIVILEGES ON * . * TO 'resttest'@'localhost';

DROP SCHEMA if exists `resttest`;

CREATE DATABASE `resttest`;

USE `resttest`;

