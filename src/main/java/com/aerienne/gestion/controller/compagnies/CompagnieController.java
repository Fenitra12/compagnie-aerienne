package com.aerienne.gestion.controller.compagnies;

import com.aerienne.gestion.model.compagnies.Compagnie;
import com.aerienne.gestion.service.compagnies.CompagnieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class CompagnieController {
    @Autowired
    private CompagnieService compagnieService;

    @GetMapping("/compagnies")
    public String listCompagnies(Model model) {
        List<Compagnie> compagnies = compagnieService.getAllCompagnies();
        model.addAttribute("compagnies", compagnies);
        return "views/compagnies/list";
    }
}
