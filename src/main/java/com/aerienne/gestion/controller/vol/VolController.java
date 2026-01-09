package com.aerienne.gestion.controller.vol;

import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.service.vol.VolService;
import com.aerienne.gestion.service.vol.StatutVolService;
import com.aerienne.gestion.service.avions.AvionService;
import com.aerienne.gestion.service.aeroports.AeroportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

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

    @GetMapping("/vol")
    public String listVols(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        List<Vol> vols = volService.getAllVols();
        model.addAttribute("vols", vols);
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
}
