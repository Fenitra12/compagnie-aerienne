package com.aerienne.gestion.controller.reservations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aerienne.gestion.model.prix.PrixVol;
import com.aerienne.gestion.model.reservations.Reservation;
import com.aerienne.gestion.service.passagers.PassagerService;
import com.aerienne.gestion.service.prix.PrixVolService;
import com.aerienne.gestion.service.reservations.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private PrixVolService prixVolService;

    @GetMapping
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "views/reservation/list";
    }

    @GetMapping("/add")
    public String showAddForm(@RequestParam(required = false) Long volId, Model model) {
        model.addAttribute("reservation", new Reservation());
        List<PrixVol> prixVols = new ArrayList<>();
        if (volId != null) {
            prixVols = prixVolService.findByVolId(volId);
        }
        System.out.println("volId: " + volId + ", prixVols size: " + prixVols.size());
        model.addAttribute("prixVols", prixVols);
        return "views/reservation/add";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute Reservation reservation) {
        // Charger le PrixVol complet pour obtenir le Vol associé
        if (reservation.getPrixVol() != null && reservation.getPrixVol().getIdPrix() != null) {
            var prixVol = prixVolService.getPrixVolById(reservation.getPrixVol().getIdPrix());
            reservation.setPrixVol(prixVol);
        }

        // Réutiliser le passager par email s'il existe, sinon le créer
        if (reservation.getPassager() != null) {
            var p = reservation.getPassager();
            if (p.getEmail() != null && !p.getEmail().isBlank()) {
                var existing = passagerService.getByEmail(p.getEmail());
                if (existing != null) {
                    reservation.setPassager(existing);
                } else if (p.getIdPassager() == null) {
                    var saved = passagerService.savePassager(p);
                    reservation.setPassager(saved);
                }
            } else if (p.getIdPassager() == null) {
                var saved = passagerService.savePassager(p);
                reservation.setPassager(saved);
            }
        }

        reservationService.saveReservation(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }
}