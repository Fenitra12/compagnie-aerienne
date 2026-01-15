package com.aerienne.gestion.controller.prix;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aerienne.gestion.model.prix.PrixVol;
import com.aerienne.gestion.service.prix.PrixVolService;
import com.aerienne.gestion.service.vol.VolService;

@Controller
@RequestMapping("/prix-vol")
public class PrixVolController {

    @Autowired
    private PrixVolService prixVolService;

    @Autowired
    private VolService volService;

    @GetMapping
    public String listPrixVols(Model model) {
        List<PrixVol> prixVols = prixVolService.getAllPrixVols();
        model.addAttribute("prixVols", prixVols);
        return "views/prix/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("prixVol", new PrixVol());
        model.addAttribute("vols", volService.getAllVols());
        return "views/prix/add";
    }

    @PostMapping("/add")
    public String addPrixVol(@ModelAttribute PrixVol prixVol) {
        prixVolService.savePrixVol(prixVol);
        return "redirect:/prix-vol";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        PrixVol prixVol = prixVolService.getPrixVolById(id);
        model.addAttribute("prixVol", prixVol);
        model.addAttribute("vols", volService.getAllVols());
        return "views/prix/edit";
    }

    @PostMapping("/edit/{id}")
    public String editPrixVol(@PathVariable Long id, @ModelAttribute PrixVol prixVol) {
        prixVol.setIdPrix(id);
        prixVolService.savePrixVol(prixVol);
        return "redirect:/prix-vol";
    }

    @GetMapping("/delete/{id}")
    public String deletePrixVol(@PathVariable Long id) {
        prixVolService.deletePrixVol(id);
        return "redirect:/prix-vol";
    }
}