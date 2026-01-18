-- Mise à jour du prix adulte et du prix enfant (prix_reduction) pour le vol 8
UPDATE prix_vol
SET prix = 250.00,
	prix_reduction = 250.00
WHERE id_vol = 8 AND classe = 'Economy';

UPDATE prix_vol
SET prix = 250.00,
	prix_reduction = 250.00
WHERE id_vol = 8 AND classe = 'premium';

UPDATE prix_vol
SET prix = 250.00,
	prix_reduction = 250.00
WHERE id_vol = 8 AND classe = 'First';
