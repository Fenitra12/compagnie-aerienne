package com.aerienne.gestion.controller.vol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.service.aeroports.AeroportService;
import com.aerienne.gestion.service.avions.AvionService;
import com.aerienne.gestion.service.compagnies.CompagnieService;
import com.aerienne.gestion.service.reservations.ReservationService;
import com.aerienne.gestion.service.vol.StatutVolService;
import com.aerienne.gestion.service.vol.VolService;

import jakarta.servlet.http.HttpSession;

@Controller
public class VolController {

    @Autowired
    private VolService volService;

    @Autowired
    private AvionService avionService;

    @Autowired
    private AeroportService aeroportService;

    @Autowired
    private StatutVolService statutVolService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CompagnieService compagnieService;

    @GetMapping("/vol")
    public String listVols(@RequestParam(required = false) String depart,
                           @RequestParam(required = false) String arrivee,
                           Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        List<Vol> vols = volService.getAllVols();
        if (depart != null && !depart.isEmpty()) {
            vols = vols.stream().filter(v -> v.getAeroportDepart().getNom().toLowerCase().contains(depart.toLowerCase()) ||
                                              v.getAeroportDepart().getCodeIata().toLowerCase().contains(depart.toLowerCase())).collect(Collectors.toList());
        }
        if (arrivee != null && !arrivee.isEmpty()) {
            vols = vols.stream().filter(v -> v.getAeroportArrivee().getNom().toLowerCase().contains(arrivee.toLowerCase()) ||
                                              v.getAeroportArrivee().getCodeIata().toLowerCase().contains(arrivee.toLowerCase())).collect(Collectors.toList());
        }
        model.addAttribute("vols", vols);
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        return "views/vol/list";
    }

    @GetMapping("/vol/add")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("vol", new Vol());
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("statuts", statutVolService.getAllStatuts());
        return "views/vol/add";
    }

    @PostMapping("/vol/add")
    public String addVol(@ModelAttribute Vol vol, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        volService.saveVol(vol);
        return "redirect:/vol";
    }

    @GetMapping("/vol/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        Vol vol = volService.getVolById(id);
        model.addAttribute("vol", vol);
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("statuts", statutVolService.getAllStatuts());
        return "views/vol/edit";
    }

    @PostMapping("/vol/edit/{id}")
    public String editVol(@PathVariable Long id, @ModelAttribute Vol vol, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        vol.setIdVol(id);
        volService.saveVol(vol);
        return "redirect:/vol";
    }

    @GetMapping("/vol/delete/{id}")
    public String deleteVol(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        volService.deleteVol(id);
        return "redirect:/vol";
    }

    @GetMapping("/vol/stats")
    public String showVolStats(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               @RequestParam(required = false) Long departId,
                               @RequestParam(required = false) Long arriveeId,
                               @RequestParam(required = false) Long compagnieId,
                               Model model,
                               HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        var stats = reservationService.getVolRevenue(startDate, endDate, departId, arriveeId, compagnieId);
        var topVol = stats.isEmpty() ? null : stats.get(0);

        List<String> labels = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();
        stats.forEach(s -> {
            labels.add(s.getVol().getAeroportDepart().getCodeIata() + " -> " + s.getVol().getAeroportArrivee().getCodeIata());
            revenues.add(s.getRevenue());
        });

        model.addAttribute("stats", stats);
        model.addAttribute("topVol", topVol);
        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartRevenues", revenues);
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("compagnies", compagnieService.getAllCompagnies());

        model.addAttribute("selectedStart", startDate);
        model.addAttribute("selectedEnd", endDate);
        model.addAttribute("selectedDepart", departId);
        model.addAttribute("selectedArrivee", arriveeId);
        model.addAttribute("selectedCompagnie", compagnieId);

        return "views/vol/stats";
    }
}
