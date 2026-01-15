package com.aerienne.gestion.controller.avions;

import com.aerienne.gestion.model.avions.Avion;
import com.aerienne.gestion.model.compagnies.Compagnie;
import com.aerienne.gestion.service.avions.AvionService;
import com.aerienne.gestion.service.compagnies.CompagnieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/avions")
public class AvionController {

    @Autowired
    private AvionService avionService;

    @Autowired
    private CompagnieService compagnieService;

    @GetMapping
    public String listAvions(Model model) {
        List<Avion> avions = avionService.getAllAvions();
        model.addAttribute("avions", avions);
        return "views/avions/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("avion", new Avion());
        model.addAttribute("compagnies", compagnieService.getAllCompagnies());
        return "views/avions/add";
    }

    @PostMapping("/add")
    public String addAvion(@ModelAttribute Avion avion) {
        avionService.saveAvion(avion);
        return "redirect:/avions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Avion avion = avionService.getAvionById(id);
        if (avion == null) {
            return "redirect:/avions";
        }
        model.addAttribute("avion", avion);
        model.addAttribute("compagnies", compagnieService.getAllCompagnies());
        return "views/avions/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAvion(@PathVariable Long id, @ModelAttribute Avion avion) {
        avion.setIdAvion(id);
        avionService.saveAvion(avion);
        return "redirect:/avions";
    }

    @GetMapping("/delete/{id}")
    public String deleteAvion(@PathVariable Long id) {
        avionService.deleteAvion(id);
        return "redirect:/avions";
    }
}
