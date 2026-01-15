package com.aerienne.gestion.controller.revenumax;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aerienne.gestion.service.aeroports.AeroportService;
import com.aerienne.gestion.service.vol.VolService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MaxRevenuController {

    @Autowired
    private VolService volService;

    @Autowired
    private AeroportService aeroportService;

    @GetMapping("/maxvalue")
    public String getMaxRevenuVols(
            @RequestParam(required = false) Long depart,
            @RequestParam(required = false) Long arrivee,
            Model model,
            HttpSession session) {

        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("depart", depart);
        model.addAttribute("arrivee", arrivee);

        List<Map<String, Object>> maxRevenuVols = volService.findMaxRevenuParVolFiltered(depart, arrivee);
        model.addAttribute("maxRevenuVols", maxRevenuVols);

        return "views/vol/revenumax";
    }
}
