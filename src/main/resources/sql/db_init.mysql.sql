CREATE SCHEMA IF NOT EXISTS `SAMPLE`;

USE `SAMPLE`;

CREATE TABLE IF NOT EXISTS `Country` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(40),
    PRIMARY KEY (`id`))
ENGINE = InnoDB;

TRUNCATE TABLE `Country`;

INSERT INTO `Country` (id, name) VALUES(1,'Brazil');
INSERT INTO `Country` (id, name) VALUES(2,'United States');
INSERT INTO `Country` (id, name) VALUES(3,'France');


CREATE TABLE IF NOT EXISTS `City` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(40),
    `country_id` INT,
    PRIMARY KEY (`id`))
ENGINE = InnoDB;

TRUNCATE TABLE `City`;

INSERT INTO `City` (name, country_id) VALUES('Curitiba',1);
INSERT INTO `City` (name, country_id) VALUES('Rio de Janeiro',1);
INSERT INTO `City` (name, country_id) VALUES('Manaus',1);
INSERT INTO `City` (name, country_id) VALUES('Fortaleza',1);
INSERT INTO `City` (name, country_id) VALUES('New York',2);
INSERT INTO `City` (name, country_id) VALUES('Los Angeles',2);
INSERT INTO `City` (name, country_id) VALUES('Atlanta',2);
INSERT INTO `City` (name, country_id) VALUES('Paris',3);
INSERT INTO `City` (name, country_id) VALUES('Lyon',3);

CREATE USER 'dbuser'@'localhost' IDENTIFIED BY 'dbuser';
CREATE USER 'dbuser'@'%' IDENTIFIED BY 'dbuser';

GRANT CREATE, DROP, GRANT OPTION, REFERENCES, ALTER, DELETE, INDEX, INSERT, SELECT, UPDATE ON TABLE `Country` TO 'dbuser'@'localhost';
GRANT CREATE, DROP, GRANT OPTION, REFERENCES, ALTER, DELETE, INDEX, INSERT, SELECT, UPDATE ON TABLE `Country` TO 'dbuser'@'%';

GRANT CREATE, DROP, GRANT OPTION, REFERENCES, ALTER, DELETE, INDEX, INSERT, SELECT, UPDATE ON TABLE `City` TO 'dbuser'@'localhost';
GRANT CREATE, DROP, GRANT OPTION, REFERENCES, ALTER, DELETE, INDEX, INSERT, SELECT, UPDATE ON TABLE `City` TO 'dbuser'@'%';