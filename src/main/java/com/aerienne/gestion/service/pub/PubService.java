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
import com.aerienne.gestion.model.pub.Publicite;
import com.aerienne.gestion.model.pub.Societe;
import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.pub.DiffusionPubRepository;
import com.aerienne.gestion.repository.pub.PubRevenueView;
import com.aerienne.gestion.repository.pub.PubliciteRepository;
import com.aerienne.gestion.repository.pub.SocieteRepository;
import com.aerienne.gestion.repository.vol.VolRepository;

@Service
public class PubService {

        @Autowired
        private DiffusionPubRepository diffusionPubRepository;

        @Autowired
        private PubliciteRepository publiciteRepository;

        @Autowired
        private SocieteRepository societeRepository;

        @Autowired
        private VolRepository volRepository;

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
                                                                          Integer annee,
                                                                          Integer mois,
                                                                          Integer nombreDiffusions,
                                                                          Double prixParDiffusion) {
                if (publiciteId == null) {
                        throw new IllegalArgumentException("Publicite requise");
                }
                if (annee == null || mois == null) {
                        throw new IllegalArgumentException("Annee et mois requis");
                }

                DiffusionPub entity = id != null
                                ? diffusionPubRepository.findById(id).orElse(new DiffusionPub())
                                : new DiffusionPub();

                Publicite pub = publiciteRepository.findById(publiciteId)
                                .orElseThrow(() -> new IllegalArgumentException("Publicite introuvable"));
                entity.setPublicite(pub);

                if (volId != null) {
                        Vol vol = volRepository.findById(volId).orElse(null);
                        entity.setVol(vol);
                } else {
                        entity.setVol(null);
                }

                entity.setAnnee(annee);
                entity.setMois(mois);
                entity.setNombreDiffusions(nombreDiffusions != null ? nombreDiffusions : 0);
                entity.setPrixParDiffusion(prixParDiffusion != null ? prixParDiffusion : 0d);
                return diffusionPubRepository.save(entity);
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
}
