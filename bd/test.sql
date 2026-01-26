-- Jeu de données correspondant au scénario maquette
-- Hypothèse : les tables sont vides (hors admin et StatutVol) après exécution de 00_script.sql

-- Sociétés
INSERT INTO Societe (nom) VALUES
  ('Lewis'),
  ('Vaniala'),
  ('Socobis'),
  ('Jejoo');

-- Compagnie
INSERT INTO Compagnie (nom, pays, code_iata, code_icao) VALUES
  ('Air Madagascar', 'Madagascar', 'MD', 'MDG');

-- Aéroports (TNR -> NOS)
INSERT INTO Aeroport (nom, ville, pays, code_iata, code_icao) VALUES
  ('Ivato', 'Tananarive', 'Madagascar', 'TNR', 'FMMI'),
  ('Fascene', 'Nosy Be', 'Madagascar', 'NOS', 'FMNN');

-- Avion
INSERT INTO Avion (id_compagnie, modele, capacite, numero_immatriculation) VALUES
  ((SELECT id_compagnie FROM Compagnie WHERE nom = 'Air Madagascar'), 'ATR 72-600', 100, 'TR-045');

-- Vols (TNR -> NOS)
INSERT INTO Vol (id_avion, id_aeroport_depart, id_aeroport_arrivee, date_depart, date_arrivee, seats_total, seats_available, id_statut)
VALUES
  ((SELECT id_avion FROM Avion WHERE numero_immatriculation = 'TR-045'),
   (SELECT id_aeroport FROM Aeroport WHERE code_iata = 'TNR'),
   (SELECT id_aeroport FROM Aeroport WHERE code_iata = 'NOS'),
   '2026-01-20 10:00:00', '2026-01-20 12:00:00', 100, 100, 1),
  ((SELECT id_avion FROM Avion WHERE numero_immatriculation = 'TR-045'),
   (SELECT id_aeroport FROM Aeroport WHERE code_iata = 'TNR'),
   (SELECT id_aeroport FROM Aeroport WHERE code_iata = 'NOS'),
   '2026-01-21 10:00:00', '2026-01-21 12:00:00', 100, 100, 1),
  ((SELECT id_avion FROM Avion WHERE numero_immatriculation = 'TR-045'),
   (SELECT id_aeroport FROM Aeroport WHERE code_iata = 'TNR'),
   (SELECT id_aeroport FROM Aeroport WHERE code_iata = 'NOS'),
   '2026-01-21 15:00:00', '2026-01-21 17:00:00', 100, 100, 1);

-- Prix par classe (optionnel, pour préparer la billetterie)
INSERT INTO prix_vol (id_vol, classe, prix, prix_reduction, prix_bebe)
SELECT v.id_vol, c.classe, c.prix, c.prix_reduction, 0
FROM (
  VALUES
    ('Economy', 800000.00, 800000.00),
    ('Business', 1200000.00, 1200000.00)
) AS c(classe, prix, prix_reduction)
CROSS JOIN Vol v
WHERE v.id_vol IN (
  SELECT id_vol FROM Vol WHERE date_depart IN ('2026-01-20 10:00:00', '2026-01-21 10:00:00', '2026-01-21 15:00:00')
);

-- Répartition des places par classe (optionnel, cohérent avec l'avion 100 places)
INSERT INTO Vol_Place_Classe (id_vol, classe, seats_total, seats_available)
SELECT v.id_vol, c.classe, c.seats_total, c.seats_total
FROM (
  VALUES
    ('Economy', 80),
    ('Business', 20)
) AS c(classe, seats_total)
CROSS JOIN Vol v
WHERE v.id_vol IN (
  SELECT id_vol FROM Vol WHERE date_depart IN ('2026-01-20 10:00:00', '2026-01-21 10:00:00', '2026-01-21 15:00:00')
);

-- Publicités pour les sociétés (sans diffusions ni paiements, à compléter au besoin)
INSERT INTO Publicite (id_societe, titre)
VALUES
  ((SELECT id_societe FROM Societe WHERE nom = 'Vaniala'), 'Pub Vaniala 1'),
  ((SELECT id_societe FROM Societe WHERE nom = 'Lewis'), 'Pub Lewis 1'),
  ((SELECT id_societe FROM Societe WHERE nom = 'Socobis'), 'Pub Socobis 2'),
  ((SELECT id_societe FROM Societe WHERE nom = 'Jejoo'), 'Pub Jejoo 1');
