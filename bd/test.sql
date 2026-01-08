psql -U postgres -d test_db

DROP DATABASE IF EXISTS test_db;
CREATE DATABASE test_db;
-- Connectez-vous à la base test_db avant d'exécuter la suite

CREATE TABLE simple_entity (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255)
);

INSERT INTO simple_entity (id, name) VALUES (1, 'Alice');
INSERT INTO simple_entity (id, name) VALUES (2, 'Bob');
INSERT INTO simple_entity (id, name) VALUES (3, 'Charlie');
