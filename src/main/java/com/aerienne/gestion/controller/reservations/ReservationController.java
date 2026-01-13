package com.aerienne.gestion.controller.reservations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aerienne.gestion.model.passagers.Passager;
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
    public String showAddForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("passagers", passagerService.getAllPassagers());
        model.addAttribute("prixVols", prixVolService.getAllPrixVols());
        return "views/reservation/add";
    }

    @PostMapping("/add")
    public String addReservation(@ModelAttribute Reservation reservation, @ModelAttribute Passager passager) {
        // Sauvegarder le passager si nouveau
        if (passager.getIdPassager() == null) {
            passagerService.savePassager(passager);
        }
        reservation.setPassager(passager);
        reservationService.saveReservation(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }
}