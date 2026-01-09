package com.aerienne.gestion.controller.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aerienne.gestion.model.passagers.Passager;
import com.aerienne.gestion.model.prix.PrixVol;
import com.aerienne.gestion.model.reservations.Reservation;
import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.repository.passager.PassagerRepository;
import com.aerienne.gestion.repository.prix.PrixVolRepository;
import com.aerienne.gestion.repository.reservation.ReservationRepository;
import com.aerienne.gestion.repository.vol.VolRepository;
import com.aerienne.gestion.service.reservation.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PassagerRepository passagerRepository;

    @Autowired
    private PrixVolRepository prixVolRepository;

    @Autowired
    private VolRepository volRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("reservations", reservations);
        return "views/reservation/reservation";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Reservation reservation = new Reservation();
        reservation.setPassager(new Passager());
        reservation.setPrixVol(new PrixVol());
        reservation.setVol(new Vol());
        model.addAttribute("reservation", reservation);
        // List<PrixVol> prixVols = prixVolRepository.findAll();
        List<Vol> vols = volRepository.findAll();
        // model.addAttribute("prixVols", prixVols);
        model.addAttribute("vols", vols);
        return "views/reservation/add";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute Reservation reservation) {
        Passager passagerPayload = reservation.getPassager();
        if (passagerPayload == null || !StringUtils.hasText(passagerPayload.getEmail())) {
            throw new IllegalArgumentException("Les informations du passager sont obligatoires");
        }

        Passager passager = passagerRepository.findByEmail(passagerPayload.getEmail())
            .map(existing -> {
                existing.setNom(passagerPayload.getNom());
                existing.setPrenom(passagerPayload.getPrenom());
                existing.setDateNaissance(passagerPayload.getDateNaissance());
                return passagerRepository.save(existing);
            })
            .orElseGet(() -> passagerRepository.save(passagerPayload));

        Long volId = reservation.getVol() != null ? reservation.getVol().getIdVol() : null;
        if (volId == null) {
            throw new IllegalArgumentException("Le vol est obligatoire");
        }

        Vol vol = volRepository.findById(volId)
            .orElseThrow(() -> new IllegalArgumentException("Vol introuvable"));

        PrixVol prixVol = prixVolRepository.findFirstByVolIdVolOrderByDateMajDesc(volId)
                .orElseGet(() -> createDefaultPrice(vol));

        reservation.setPassager(passager);
        reservation.setPrixVol(prixVol);
        reservation.setVol(vol);

        reservationService.saveReservation(reservation);
        return "redirect:/reservations";
    }

    private PrixVol createDefaultPrice(Vol vol) {
        PrixVol prixVol = new PrixVol();
        prixVol.setVol(vol);
        prixVol.setCompagnie(vol.getAvion().getCompagnie());
        prixVol.setClasse("Economique");
        prixVol.setPrix(0d);
        prixVol.setDateMaj(LocalDateTime.now());
        return prixVolRepository.save(prixVol);
    }
}
