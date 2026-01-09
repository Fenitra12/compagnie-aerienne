package com.aerienne.gestion.controller.compagnies;

import com.aerienne.gestion.model.compagnies.Compagnie;
import com.aerienne.gestion.service.compagnies.CompagnieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/compagnies")
public class CompagnieController {
    @Autowired
    private CompagnieService compagnieService;

    @GetMapping
    public String listCompagnies(Model model) {
        List<Compagnie> compagnies = compagnieService.getAllCompagnies();
        model.addAttribute("compagnies", compagnies);
        return "views/compagnies/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("compagnie", new Compagnie());
        return "views/compagnies/add";
    }

    @PostMapping("/add")
    public String addCompagnie(@ModelAttribute Compagnie compagnie) {
        compagnieService.saveCompagnie(compagnie);
        return "redirect:/compagnies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Compagnie compagnie = compagnieService.getCompagnieById(id);
        if (compagnie == null) {
            return "redirect:/compagnies";
        }
        model.addAttribute("compagnie", compagnie);
        return "views/compagnies/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCompagnie(@PathVariable Long id, @ModelAttribute Compagnie compagnie) {
        compagnie.setIdCompagnie(id);
        compagnieService.saveCompagnie(compagnie);
        return "redirect:/compagnies";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompagnie(@PathVariable Long id) {
        compagnieService.deleteCompagnie(id);
        return "redirect:/compagnies";
    }
}
