package com.aerienne.gestion.service.pub;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aerienne.gestion.model.pub.DiffusionPub;
import com.aerienne.gestion.model.pub.FacturePub;
import com.aerienne.gestion.model.pub.PaiementFacturePub;
import com.aerienne.gestion.model.pub.PaiementPub;
import com.aerienne.gestion.model.pub.Publicite;
import com.aerienne.gestion.model.pub.Societe;
import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.pub.DiffusionPubRepository;
import com.aerienne.gestion.repository.pub.FacturePubRepository;
import com.aerienne.gestion.repository.pub.PaiementFacturePubRepository;
import com.aerienne.gestion.repository.pub.PaiementPubRepository;
import com.aerienne.gestion.repository.pub.PubRevenueView;
import com.aerienne.gestion.repository.pub.PubliciteRepository;
import com.aerienne.gestion.repository.pub.SocieteRepository;
import com.aerienne.gestion.repository.pub.VolSocieteRevenueView;
import com.aerienne.gestion.repository.vol.VolRepository;

@Service
public class PubService {

        @Autowired
        private DiffusionPubRepository diffusionPubRepository;

        @Autowired
        private PaiementPubRepository paiementPubRepository;

        @Autowired
        private PubliciteRepository publiciteRepository;

        @Autowired
        private SocieteRepository societeRepository;

        @Autowired
        private VolRepository volRepository;

        @Autowired
        private FacturePubRepository facturePubRepository;

        @Autowired
        private PaiementFacturePubRepository paiementFacturePubRepository;

        public List<DiffusionPub> listDiffusions() {
                return diffusionPubRepository.findAll();
        }

        public List<Publicite> listPublicites() {
                return publiciteRepository.findAll();
        }

        public List<Societe> listSocietes() {
                return societeRepository.findAll();
        }

        public List<Vol> listVols() {
                return volRepository.findAll();
        }

        public Optional<DiffusionPub> getDiffusion(Long id) {
                return diffusionPubRepository.findById(id);
        }

        public Optional<Publicite> getPublicite(Long id) {
                return publiciteRepository.findById(id);
        }

        public Optional<Societe> getSociete(Long id) {
                return societeRepository.findById(id);
        }

        @Transactional
        public Societe saveSociete(Long id, String nom, String contact) {
                if (nom == null || nom.isBlank()) {
                        throw new IllegalArgumentException("Nom requis");
                }
                Societe entity = id != null
                                ? societeRepository.findById(id).orElse(new Societe())
                                : new Societe();
                entity.setNom(nom.trim());
                entity.setContact(contact);
                return societeRepository.save(entity);
        }

        public void deleteSociete(Long id) {
                societeRepository.deleteById(id);
        }

        @Transactional
        public Publicite savePublicite(Long id, Long societeId, String titre, String description) {
                if (societeId == null) {
                        throw new IllegalArgumentException("Societe requise");
                }
                Societe societe = societeRepository.findById(societeId)
                                .orElseThrow(() -> new IllegalArgumentException("Societe introuvable"));
                Publicite entity = id != null
                                ? publiciteRepository.findById(id).orElse(new Publicite())
                                : new Publicite();
                entity.setSociete(societe);
                entity.setTitre(titre);
                entity.setDescription(description);
                return publiciteRepository.save(entity);
        }

        public void deletePublicite(Long id) {
                publiciteRepository.deleteById(id);
        }

        @Transactional
        public DiffusionPub saveDiffusion(Long id,
                                                                          Long publiciteId,
                                                                          Long volId,
                                                                          Integer nombreDiffusions,
                                                                          Double prixParDiffusion,
                                                                          Double paiement) {
                if (publiciteId == null) {
                        throw new IllegalArgumentException("Publicite requise");
                }
                if (volId == null) {
                        throw new IllegalArgumentException("Vol requis");
                }

                DiffusionPub entity = id != null
                                ? diffusionPubRepository.findById(id).orElse(new DiffusionPub())
                                : new DiffusionPub();

                Publicite pub = publiciteRepository.findById(publiciteId)
                                .orElseThrow(() -> new IllegalArgumentException("Publicite introuvable"));
                entity.setPublicite(pub);

                Vol vol = volRepository.findById(volId)
                                .orElseThrow(() -> new IllegalArgumentException("Vol introuvable"));
                entity.setVol(vol);

                if (vol.getDateDepart() != null) {
                        entity.setAnnee(vol.getDateDepart().getYear());
                        entity.setMois(vol.getDateDepart().getMonthValue());
                } else {
                        entity.setAnnee(null);
                        entity.setMois(null);
                }
                entity.setNombreDiffusions(nombreDiffusions != null ? nombreDiffusions : 0);
                entity.setPrixParDiffusion(prixParDiffusion != null ? prixParDiffusion : 0d);
                DiffusionPub saved = diffusionPubRepository.save(entity);

                if (paiement != null && paiement > 0) {
                        PaiementPub pay = new PaiementPub();
                        pay.setDiffusion(saved);
                        pay.setMontant(paiement);
                        paiementPubRepository.save(pay);
                }
                return saved;
        }

        @Transactional
        public FacturePub refreshFactureForSociete(Long societeId) {
                if (societeId == null) {
                        throw new IllegalArgumentException("Societe requise");
                }
                double total = diffusionPubRepository.sumAmountBySociete(societeId);
                FacturePub facture = facturePubRepository.findBySociete_IdSociete(societeId)
                                .orElseGet(() -> {
                                        FacturePub f = new FacturePub();
                                        Societe s = societeRepository.findById(societeId)
                                                        .orElseThrow(() -> new IllegalArgumentException("Societe introuvable"));
                                        f.setSociete(s);
                                        return f;
                                });
                facture.setMontantTotal(total);
                double paid = facture.getMontantPaye() != null ? facture.getMontantPaye() : 0d;
                facture.setStatut(paid >= total ? "SOLDEE" : "EN_COURS");
                return facturePubRepository.save(facture);
        }

        @Transactional
        public FacturePub paySociete(Long societeId, Double montant) {
                if (montant == null || montant <= 0) {
                        throw new IllegalArgumentException("Montant invalide");
                }
                FacturePub facture = refreshFactureForSociete(societeId);
                facture.setMontantPaye((facture.getMontantPaye() != null ? facture.getMontantPaye() : 0d) + montant);
                double total = facture.getMontantTotal() != null ? facture.getMontantTotal() : 0d;
                facture.setStatut(facture.getMontantPaye() >= total ? "SOLDEE" : "EN_COURS");
                facture = facturePubRepository.save(facture);

                PaiementFacturePub payment = new PaiementFacturePub();
                payment.setFacture(facture);
                payment.setMontant(montant);
                paiementFacturePubRepository.save(payment);
                return facture;
        }

        public List<FacturePub> listFactures() {
                return facturePubRepository.findAll();
        }

        public Map<Long, FacturePub> factureBySociete() {
                var societes = societeRepository.findAll();
                societes.forEach(s -> refreshFactureForSociete(s.getIdSociete()));
                return facturePubRepository.findAll().stream()
                                .collect(Collectors.toMap(f -> f.getSociete().getIdSociete(), f -> f));
        }

        @Transactional
        public void payDiffusion(Long diffusionId, Double montant) {
                if (diffusionId == null) {
                        throw new IllegalArgumentException("Diffusion requise");
                }
                if (montant == null || montant <= 0) {
                        throw new IllegalArgumentException("Montant invalide");
                }
                DiffusionPub diffusion = diffusionPubRepository.findById(diffusionId)
                                .orElseThrow(() -> new IllegalArgumentException("Diffusion introuvable"));

                PaiementPub paiement = new PaiementPub();
                paiement.setDiffusion(diffusion);
                paiement.setMontant(montant);
                paiementPubRepository.save(paiement);
        }

        public void deleteDiffusion(Long id) {
                diffusionPubRepository.deleteById(id);
        }

    public List<PubRevenueView> getVolAdRevenue(LocalDate startDate,
                                                LocalDate endDate,
                                                Long departId,
                                                Long arriveeId,
                                                Long compagnieId) {
        Integer startYm = startDate != null ? startDate.getYear() * 100 + startDate.getMonthValue() : null;
        Integer endYm = endDate != null ? endDate.getYear() * 100 + endDate.getMonthValue() : null;

        List<PubRevenueView> aggregated = diffusionPubRepository.findRevenueByVol(startYm, endYm, departId, arriveeId, compagnieId, true);

        Map<Long, PubRevenueView> byVolId = aggregated.stream()
                .filter(v -> v.getVol() != null && v.getVol().getIdVol() != null)
                .collect(Collectors.toMap(v -> v.getVol().getIdVol(), v -> v));

        Predicate<Vol> filter = vol -> (departId == null || Objects.equals(vol.getAeroportDepart().getIdAeroport(), departId))
                && (arriveeId == null || Objects.equals(vol.getAeroportArrivee().getIdAeroport(), arriveeId))
                && (compagnieId == null || Objects.equals(vol.getAvion().getCompagnie().getIdCompagnie(), compagnieId));

        volRepository.findAll().stream()
                .filter(filter)
                .filter(vol -> !byVolId.containsKey(vol.getIdVol()))
                .forEach(vol -> byVolId.put(vol.getIdVol(), new PubRevenueView(vol, 0d, 0L)));

        return byVolId.values().stream()
                .sorted((a, b) -> Double.compare(b.getRevenue(), a.getRevenue()))
                .collect(Collectors.toList());
    }

        public List<VolSocieteRevenueView> getVolAdRevenueBySociete(LocalDate startDate,
                                                                                                                                LocalDate endDate,
                                                                                                                                Long departId,
                                                                                                                                Long arriveeId,
                                                                                                                                Long compagnieId) {
                Integer startYm = startDate != null ? startDate.getYear() * 100 + startDate.getMonthValue() : null;
                Integer endYm = endDate != null ? endDate.getYear() * 100 + endDate.getMonthValue() : null;

                return diffusionPubRepository.findRevenueByVolAndSociete(startYm, endYm, departId, arriveeId, compagnieId, true);
        }

        public Map<Long, Double> paiementTotalsByDiffusion(List<Long> diffusionIds) {
                if (diffusionIds == null || diffusionIds.isEmpty()) {
                        return Map.of();
                }
                return paiementPubRepository.sumByDiffusionIds(diffusionIds).stream()
                                .collect(Collectors.toMap(
                                                r -> (Long) r[0],
                                                r -> r[1] != null ? ((Number) r[1]).doubleValue() : 0d
                                ));
        }
}
