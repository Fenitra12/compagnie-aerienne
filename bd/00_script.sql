DROP DATABASE IF EXISTS "compagnie-aerienne";
CREATE DATABASE "compagnie-aerienne";
\c "compagnie-aerienne";

-- Table Utilisateur pour login admin
CREATE TABLE Utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'admin'
);


-- Table Compagnie
CREATE TABLE Compagnie (
    id_compagnie SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    pays VARCHAR(50),
    code_iata CHAR(2) UNIQUE,
    code_icao CHAR(3) UNIQUE
);

-- Table Avion
CREATE TABLE Avion (
    id_avion SERIAL PRIMARY KEY,
    id_compagnie INT NOT NULL,
    modele VARCHAR(50) NOT NULL,
    capacite INT NOT NULL,
    numero_immatriculation VARCHAR(20) UNIQUE NOT NULL,
    FOREIGN KEY (id_compagnie) REFERENCES Compagnie(id_compagnie)
);

-- Table Aeroport
CREATE TABLE Aeroport (
    id_aeroport SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    ville VARCHAR(50),
    pays VARCHAR(50),
    code_iata CHAR(3) UNIQUE,
    code_icao CHAR(4) UNIQUE
);

-- Table Vol
CREATE TABLE Vol (
    id_vol SERIAL PRIMARY KEY,
    id_avion INT NOT NULL,
    id_aeroport_depart INT NOT NULL,
    id_aeroport_arrivee INT NOT NULL,
    date_depart TIMESTAMP NOT NULL,
    date_arrivee TIMESTAMP NOT NULL,
    statut VARCHAR(20) DEFAULT 'prévu',
    FOREIGN KEY (id_avion) REFERENCES Avion(id_avion),
    FOREIGN KEY (id_aeroport_depart) REFERENCES Aeroport(id_aeroport),
    FOREIGN KEY (id_aeroport_arrivee) REFERENCES Aeroport(id_aeroport)
);

-- Nouvelle table PrixVol (avant Reservation pour éviter l'erreur)
CREATE TABLE PrixVol (
    id_prix SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,
    id_compagnie INT NOT NULL,
    classe VARCHAR(20) NOT NULL,
    prix NUMERIC(10,2) NOT NULL,
    date_maj TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (id_vol) REFERENCES Vol(id_vol),
    FOREIGN KEY (id_compagnie) REFERENCES Compagnie(id_compagnie),
    UNIQUE (id_vol, id_compagnie, classe)
);

-- Table Pilote
CREATE TABLE Pilote (
    id_pilote SERIAL PRIMARY KEY,
    id_compagnie INT NOT NULL,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    licence_num VARCHAR(20) UNIQUE NOT NULL,
    experience INT DEFAULT 0,
    FOREIGN KEY (id_compagnie) REFERENCES Compagnie(id_compagnie)
);

-- Table Passager
CREATE TABLE Passager (
    id_passager SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE,
    email VARCHAR(100) UNIQUE
);

-- Table Reservation (N-M Passager <-> Vol avec prix lié)
CREATE TABLE Reservation (
    id_reservation SERIAL PRIMARY KEY,
    id_passager INT NOT NULL,
    id_prix_vol INT NOT NULL,
    date_reservation TIMESTAMP DEFAULT NOW(),
    siege VARCHAR(5),
    statut VARCHAR(20) DEFAULT 'confirmée',
    FOREIGN KEY (id_passager) REFERENCES Passager(id_passager),
    FOREIGN KEY (id_prix_vol) REFERENCES PrixVol(id_prix),
    UNIQUE (id_passager, id_prix_vol)
);

-- Table Equipage (N-M Vol <-> Pilote)
CREATE TABLE Equipage (
    id_equipage SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,
    id_pilote INT NOT NULL,
    role VARCHAR(50) DEFAULT 'Pilote',
    FOREIGN KEY (id_vol) REFERENCES Vol(id_vol),
    FOREIGN KEY (id_pilote) REFERENCES Pilote(id_pilote),
    UNIQUE (id_vol, id_pilote)
);



-- Insérer le compte admin par défaut
INSERT INTO Utilisateur (username, mot_de_passe, role)
VALUES ('admin', 'admin', 'admin');

INSERT INTO Compagnie (nom, pays, code_iata, code_icao) VALUES
('Air France', 'France', 'AF', 'AFR'),
('Lufthansa', 'Germany', 'LH', 'DLH'),
('Emirates', 'United Arab Emirates', 'EK', 'UAE');

INSERT INTO Avion (id_compagnie, modele, capacite, numero_immatriculation) VALUES
-- Air France
(1, 'Airbus A320', 180, 'F-A32001'),
(1, 'Airbus A330', 250, 'F-A33001'),
(1, 'Boeing 777', 300, 'F-B77701'),
(1, 'Boeing 787', 280, 'F-B78701'),
(1, 'Embraer E190', 100, 'F-E19001'),

-- Lufthansa
(2, 'Airbus A320', 180, 'D-A32001'),
(2, 'Airbus A350', 300, 'D-A35001'),
(2, 'Boeing 747', 400, 'D-B74701'),
(2, 'Boeing 777', 300, 'D-B77702'),
(2, 'Embraer E195', 120, 'D-E19501'),

-- Emirates
(3, 'Airbus A380', 500, 'A6-A38001'),
(3, 'Boeing 777', 350, 'A6-B77701'),
(3, 'Boeing 787', 280, 'A6-B78701'),
(3, 'Airbus A350', 300, 'A6-A35001'),
(3, 'Boeing 737', 180, 'A6-B73701');
