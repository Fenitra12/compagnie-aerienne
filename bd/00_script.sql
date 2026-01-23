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
(3, 'Boeing 737', 180, 'A6-B73701'),

-- Air France (ajout pour TNR -> NOS)
(1, 'Airbus A321', 200, 'F-A32199');

INSERT INTO Aeroport (nom, ville, pays, code_iata, code_icao) VALUES
('Charles de Gaulle', 'Paris', 'France', 'CDG', 'LFPG'),
('Heathrow', 'London', 'United Kingdom', 'LHR', 'EGLL'),
('Frankfurt', 'Frankfurt', 'Germany', 'FRA', 'EDDF'),
('Dubai International', 'Dubai', 'United Arab Emirates', 'DXB', 'OMDB'),
('John F. Kennedy', 'New York', 'United States', 'JFK', 'KJFK'),
('Ivato', 'Tananarive', 'Madagascar', 'TNR', 'FMMI'),
('Fascene', 'Nosy Be', 'Madagascar', 'NOS', 'FMNN');

INSERT INTO StatutVol (nom) VALUES
('PREVU'),
('CONFIRME'),
('ANNULE'),
('RETARDE'),
('EN_COURS'),
('TERMINE');

INSERT INTO Vol (id_avion, id_aeroport_depart, id_aeroport_arrivee, date_depart, date_arrivee, seats_total, seats_available, id_statut) VALUES
-- Air France vols

(1, 1, 2, '2026-01-10 08:00:00', '2026-01-10 10:30:00', 180, 180, 1),
(2, 1, 4, '2026-01-11 14:00:00', '2026-01-11 22:00:00', 250, 250, 1),
(3, 1, 5, '2026-01-12 20:00:00', '2026-01-13 08:00:00', 300, 300, 1),

-- Lufthansa vols

(6, 3, 1, '2026-01-10 12:00:00', '2026-01-10 14:30:00', 180, 180, 1),
(7, 3, 4, '2026-01-11 16:00:00', '2026-01-12 00:00:00', 300, 300, 1),

-- Emirates vols
(11, 4, 2, '2026-01-10 18:00:00', '2026-01-11 06:00:00', 500, 500, 1),
(12, 4, 5, '2026-01-12 22:00:00', '2026-01-13 10:00:00', 350, 350, 1),

-- Air France vols (nouveau) TNR -> NOS
(16, 6, 7, '2026-02-10 09:00:00', '2026-02-10 11:00:00', 200, 200, 1);

INSERT INTO prix_vol (id_vol, classe, prix, prix_reduction) VALUES
-- Vol 1: Air France CDG -> LHR
(1, 'Economy', 150.00, 100.00),
(1, 'Business', 300.00, 300.00),
(1, 'First', 500.00, 300.00),
-- Vol 2: Air France CDG -> DXB
(2, 'Economy', 400.00, 400.00),
(2, 'Business', 800.00, 800.00),
(2, 'First', 1200.00, 1200.00),

-- Vol 3: Air France CDG -> JFK
(3, 'Economy', 600.00, 600.00),
(3, 'Business', 1200.00, 1200.00),
(3, 'First', 2000.00, 2000.00),

-- Vol 4: Lufthansa FRA -> CDG
(4, 'Economy', 100.00, 100.00),
(4, 'Business', 200.00, 200.00),

-- Vol 5: Lufthansa FRA -> DXB
(5, 'Economy', 350.00, 350.00),
(5, 'Business', 700.00, 700.00),

-- Vol 6: Emirates DXB -> LHR
(6, 'Economy', 250.00, 250.00),
(6, 'Business', 500.00, 500.00),
-- Vol 7: Emirates DXB -> JFK
(7, 'Economy', 550.00, 550.00),
(7, 'Business', 1100.00, 1100.00),

-- Vol 8: Air France TNR -> NOS
(8, 'Economy', 700000.00, 500000.00),
(8, 'First', 1200000.00, 1200000.00);

-- Données de test: répartition des places par classe (seats_total = seats_available initialement)
INSERT INTO Vol_Place_Classe (id_vol, classe, seats_total, seats_available) VALUES
-- Vol 1 (capacité avion 180)
(1, 'Economy', 140, 140),
(1, 'Business', 30, 30),
(1, 'First', 10, 10),

-- Vol 2 (capacité 250)
(2, 'Economy', 200, 200),
(2, 'Business', 40, 40),
(2, 'First', 10, 10),

-- Vol 3 (capacité 300)
(3, 'Economy', 220, 220),
(3, 'Business', 60, 60),
(3, 'First', 20, 20),

-- Vol 4 (capacité 180) - pas de First dans prix_vol
(4, 'Economy', 150, 150),
(4, 'Business', 30, 30),

-- Vol 5 (capacité 300) - pas de First dans prix_vol
(5, 'Economy', 240, 240),
(5, 'Business', 60, 60),

-- Vol 6 (capacité 500) - pas de First dans prix_vol
(6, 'Economy', 400, 400),
(6, 'Business', 100, 100),

-- Vol 7 (capacité 350) - pas de First dans prix_vol
(7, 'Economy', 280, 280),
(7, 'Business', 70, 70),

-- Vol 8 (capacité 200) TNR -> NOS
(8, 'Economy', 170, 170),
(8, 'First', 30, 30);

-- Quelques passagers de test
INSERT INTO Passager (nom, prenom, date_naissance, email) VALUES
('Dupont', 'Jean', '1980-05-12', 'jean.dupont@example.com'),
('Rasoa', 'Mialy', '1992-09-02', 'mialy.rasoa@example.com');

-- Réservations de test (s'assure que la sous-requête récupère l'id_prix correct)
INSERT INTO Reservation (id_passager, id_prix_vol, date_reservation, siege, statut, adult_count, child_count)
VALUES (
    (SELECT id_passager FROM Passager WHERE email='jean.dupont@example.com'),
    (SELECT id_prix FROM prix_vol WHERE id_vol=1 AND classe='Economy' LIMIT 1),
    NOW(), '12A', 'confirmée', 1, 0
), (
    (SELECT id_passager FROM Passager WHERE email='mialy.rasoa@example.com'),
    (SELECT id_prix FROM prix_vol WHERE id_vol=2 AND classe='Business' LIMIT 1),
    NOW(), '2B', 'confirmée', 1, 0
);

INSERT INTO Societe (nom) VALUES ('Vaniala'), ('Lewis');

INSERT INTO Publicite (id_societe, titre) VALUES
  ((SELECT id_societe FROM Societe WHERE nom='Vaniala'), 'Pub Vaniala'),
  ((SELECT id_societe FROM Societe WHERE nom='Lewis'), 'Pub Lewis');

INSERT INTO diffusion_pub (id_publicite, id_vol, annee, mois, nombre_diffusions, prix_par_diffusion) VALUES
    ((SELECT id_publicite FROM Publicite WHERE titre='Pub Vaniala'), 8, 2025, 12, 20, 400000),
    ((SELECT id_publicite FROM Publicite WHERE titre='Pub Lewis'), 8, 2025, 12, 10, 400000);

-- Paiements de test
INSERT INTO paiement_pub (id_diffusion, montant) VALUES
    ((SELECT id_diffusion FROM diffusion_pub LIMIT 1), 1000000);


-- CA prenant en compte le prix enfant déjà réduit
SELECT v.id_vol,
       SUM(
           r.adult_count * pv.prix
           + r.child_count * (CASE WHEN pv.prix_reduction > 0 THEN pv.prix_reduction ELSE pv.prix END)
       ) AS ca_reel
FROM Vol v
JOIN prix_vol pv ON pv.id_vol = v.id_vol
JOIN Reservation r ON r.id_prix_vol = pv.id_prix
WHERE v.id_vol = 1
GROUP BY v.id_vol;

--Requête CA total en décembre 2025
SELECT SUM(nombre_diffusions * prix_par_diffusion) AS ca_total
FROM diffusion_pub
WHERE annee = 2025 AND mois = 12;

-- Requête CA par société en décembre 2025
SELECT s.nom AS societe,
       SUM(d.nombre_diffusions * d.prix_par_diffusion) AS ca_mensuel
FROM diffusion_pub d
JOIN Publicite p ON d.id_publicite = p.id_publicite
JOIN Societe s ON p.id_societe = s.id_societe
WHERE d.annee = 2025 AND d.mois = 12
GROUP BY s.nom;