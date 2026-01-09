package com.aerienne.gestion.controller.reservations;

import com.aerienne.gestion.service.reservations.ReservationService;
import com.aerienne.gestion.model.reservations.Reservation;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations/list")
    public String listReservations(Model model, HttpSession session) {        
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "views/reservations/list";
    }

    @GetMapping("/reservations/{id}")
    public String getReservation(@PathVariable("id") Integer id, Model model, HttpSession session) {
        System.out.println("=== DÉTAIL RÉSERVATION APPELÉ avec id: " + id + " ===");
        
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        return "views/reservations/detail";
    }
}