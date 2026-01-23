INSERT INTO paiement_pub (id_diffusion, montant) VALUES
    ((SELECT id_diffusion FROM diffusion_pub LIMIT 1), 1000000);

SELECT * from paiement_pub;