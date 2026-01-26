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

-- Table StatutVol
CREATE TABLE StatutVol (
    id_statut SERIAL PRIMARY KEY,
    nom VARCHAR(20) UNIQUE NOT NULL
);

-- Table Vol
CREATE TABLE Vol (
    id_vol SERIAL PRIMARY KEY,
    id_avion INT NOT NULL,
    id_aeroport_depart INT NOT NULL,
    id_aeroport_arrivee INT NOT NULL,
    date_depart TIMESTAMP NOT NULL,
    date_arrivee TIMESTAMP NOT NULL,
    -- Seats per class are stored in table Vol_Place_Classe (see below)
    seats_total INT NOT NULL DEFAULT 0,
    seats_available INT NOT NULL DEFAULT 0,
    id_statut INT DEFAULT 1,
    FOREIGN KEY (id_avion) REFERENCES Avion(id_avion),
    FOREIGN KEY (id_aeroport_depart) REFERENCES Aeroport(id_aeroport),
    FOREIGN KEY (id_aeroport_arrivee) REFERENCES Aeroport(id_aeroport),
    FOREIGN KEY (id_statut) REFERENCES StatutVol(id_statut)
);

-- Table Vol_Place_Classe : nombre de places par classe pour chaque vol
CREATE TABLE Vol_Place_Classe (
    id_vol_place SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,
    classe VARCHAR(20) NOT NULL,
    seats_total INT NOT NULL DEFAULT 0,
    seats_available INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_vol) REFERENCES Vol(id_vol),
    UNIQUE (id_vol, classe)
);

-- Nouvelle table PrixVol (avant Reservation pour éviter l'erreur)
CREATE TABLE prix_vol (
    id_prix SERIAL PRIMARY KEY,
    id_vol INT NOT NULL,
    classe VARCHAR(20) NOT NULL,
    prix NUMERIC(10,2) NOT NULL,
    prix_reduction NUMERIC(10,2) DEFAULT 0,
    prix_bebe NUMERIC(10,2) DEFAULT 0,
    date_maj TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (id_vol) REFERENCES Vol(id_vol),
    -- id_compagnie removed to avoid redundancy (compagnie can be inferred via Vol->Avion->Compagnie)
    UNIQUE (id_vol, classe)
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
    adult_count INT NOT NULL DEFAULT 0,
    child_count INT NOT NULL DEFAULT 0,
    baby_count INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_passager) REFERENCES Passager(id_passager),
    FOREIGN KEY (id_prix_vol) REFERENCES prix_vol(id_prix)
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

CREATE TABLE Societe (
    id_societe SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    contact VARCHAR(200)
);

CREATE TABLE Publicite (
    id_publicite SERIAL PRIMARY KEY,
    id_societe INT NOT NULL REFERENCES Societe(id_societe),
    titre VARCHAR(200),
    description TEXT
);

CREATE TABLE diffusion_pub (
    id_diffusion SERIAL PRIMARY KEY,
    id_publicite INT NOT NULL REFERENCES Publicite(id_publicite),
    id_vol INT REFERENCES Vol(id_vol),
    annee INT NOT NULL,
    mois INT NOT NULL, -- 1..12
    nombre_diffusions INT NOT NULL DEFAULT 0,
    prix_par_diffusion NUMERIC(15,2) NOT NULL
);

CREATE TABLE paiement_pub (
    id_paiement SERIAL PRIMARY KEY,
    id_diffusion INT NOT NULL REFERENCES diffusion_pub(id_diffusion) ON DELETE CASCADE,
    montant NUMERIC(15,2) NOT NULL,
    date_paiement TIMESTAMP DEFAULT NOW()
);

CREATE TABLE facture_pub (
    id_facture SERIAL PRIMARY KEY,
    id_societe INT NOT NULL REFERENCES Societe(id_societe),
    montant_total NUMERIC(15,2) NOT NULL DEFAULT 0,
    montant_paye NUMERIC(15,2) NOT NULL DEFAULT 0,
    statut VARCHAR(20) DEFAULT 'EN_COURS',
    date_creation TIMESTAMP DEFAULT NOW()
);

CREATE TABLE paiement_facture_pub (
    id_paiement SERIAL PRIMARY KEY,
    id_facture INT NOT NULL REFERENCES facture_pub(id_facture) ON DELETE CASCADE,
    montant NUMERIC(15,2) NOT NULL,
    date_paiement TIMESTAMP DEFAULT NOW()
);

INSERT INTO StatutVol (nom) VALUES
('PREVU'),
('CONFIRME'),
('ANNULE'),
('RETARDE'),
('EN_COURS'),
('TERMINE');

-- Données minimales : admin uniquement. Les jeux d'essai sont dans test.sql
INSERT INTO Utilisateur (username, mot_de_passe, role)
VALUES ('admin', 'admin', 'admin');