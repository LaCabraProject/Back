DROP SCHEMA IF EXISTS lacabradb;
DROP USER IF EXISTS 'lacabra'@'%';

CREATE SCHEMA IF NOT EXISTS lacabradb;
CREATE USER IF NOT EXISTS 'lacabra'@'%' IDENTIFIED BY 'lacabra';
GRANT ALL ON lacabradb.* TO 'lacabra'@'%';