package com.aerienne.gestion.controller.pub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aerienne.gestion.model.pub.DiffusionPub;
import com.aerienne.gestion.model.pub.Publicite;
import com.aerienne.gestion.model.pub.Societe;
import com.aerienne.gestion.service.aeroports.AeroportService;
import com.aerienne.gestion.service.compagnies.CompagnieService;
import com.aerienne.gestion.service.pub.PubService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PubController {

    @Autowired
    private PubService pubService;

    @Autowired
    private AeroportService aeroportService;

    @Autowired
    private CompagnieService compagnieService;

    @GetMapping("/pub/societes")
    public String listSocietes(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        model.addAttribute("societes", pubService.listSocietes());
        return "views/pub/societe-list";
    }

    @GetMapping("/pub/societes/add")
    public String showSocieteAdd(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        model.addAttribute("societe", new Societe());
        return "views/pub/societe-form";
    }

    @PostMapping("/pub/societes/add")
    public String addSociete(@RequestParam String nom,
                             @RequestParam(required = false) String contact,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            pubService.saveSociete(null, nom, contact);
            redirectAttributes.addFlashAttribute("success", "Société ajoutée");
            return "redirect:/pub/societes";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pub/societes/add";
        }
    }

    @GetMapping("/pub/societes/edit/{id}")
    public String showSocieteEdit(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        var s = pubService.getSociete(id);
        if (s.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Société introuvable");
            return "redirect:/pub/societes";
        }
        model.addAttribute("societe", s.get());
        model.addAttribute("societeId", id);
        return "views/pub/societe-form";
    }

    @PostMapping("/pub/societes/edit/{id}")
    public String editSociete(@PathVariable Long id,
                              @RequestParam String nom,
                              @RequestParam(required = false) String contact,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            pubService.saveSociete(id, nom, contact);
            redirectAttributes.addFlashAttribute("success", "Société mise à jour");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pub/societes/edit/" + id;
        }
        return "redirect:/pub/societes";
    }

    @PostMapping("/pub/societes/delete/{id}")
    public String deleteSociete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        pubService.deleteSociete(id);
        redirectAttributes.addFlashAttribute("success", "Société supprimée");
        return "redirect:/pub/societes";
    }

    @GetMapping("/pub/publicites")
    public String listPublicites(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        model.addAttribute("publicites", pubService.listPublicites());
        return "views/pub/publicite-list";
    }

    @GetMapping("/pub/publicites/add")
    public String showPubliciteAdd(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        preparePubliciteForm(model, new Publicite());
        return "views/pub/publicite-form";
    }

    @PostMapping("/pub/publicites/add")
    public String addPublicite(@RequestParam Long societeId,
                               @RequestParam(required = false) String titre,
                               @RequestParam(required = false) String description,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            pubService.savePublicite(null, societeId, titre, description);
            redirectAttributes.addFlashAttribute("success", "Publicité ajoutée");
            return "redirect:/pub/publicites";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pub/publicites/add";
        }
    }

    @GetMapping("/pub/publicites/edit/{id}")
    public String showPubliciteEdit(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        var pub = pubService.getPublicite(id);
        if (pub.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Publicité introuvable");
            return "redirect:/pub/publicites";
        }
        preparePubliciteForm(model, pub.get());
        model.addAttribute("publiciteId", id);
        return "views/pub/publicite-form";
    }

    @PostMapping("/pub/publicites/edit/{id}")
    public String editPublicite(@PathVariable Long id,
                                @RequestParam Long societeId,
                                @RequestParam(required = false) String titre,
                                @RequestParam(required = false) String description,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            pubService.savePublicite(id, societeId, titre, description);
            redirectAttributes.addFlashAttribute("success", "Publicité mise à jour");
            return "redirect:/pub/publicites";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pub/publicites/edit/" + id;
        }
    }

    @PostMapping("/pub/publicites/delete/{id}")
    public String deletePublicite(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        pubService.deletePublicite(id);
        redirectAttributes.addFlashAttribute("success", "Publicité supprimée");
        return "redirect:/pub/publicites";
    }

    @GetMapping("/pub")
    public String listPub(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        model.addAttribute("diffusions", pubService.listDiffusions());
        return "views/pub/list";
    }

    @GetMapping("/pub/add")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        prepareFormModel(model, new DiffusionPub());
        return "views/pub/form";
    }

    @PostMapping("/pub/add")
    public String addPub(@RequestParam Long publiciteId,
                         @RequestParam(required = false) Long volId,
                         @RequestParam Integer annee,
                         @RequestParam Integer mois,
                         @RequestParam Integer nombreDiffusions,
                         @RequestParam Double prixParDiffusion,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            pubService.saveDiffusion(null, publiciteId, volId, annee, mois, nombreDiffusions, prixParDiffusion);
            redirectAttributes.addFlashAttribute("success", "Diffusion pub ajoutée");
            return "redirect:/pub";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pub/add";
        }
    }

    @GetMapping("/pub/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        var diffusionOpt = pubService.getDiffusion(id);
        if (diffusionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Diffusion introuvable");
            return "redirect:/pub";
        }
        prepareFormModel(model, diffusionOpt.get());
        model.addAttribute("diffusionId", id);
        return "views/pub/form";
    }

    @PostMapping("/pub/edit/{id}")
    public String editPub(@PathVariable Long id,
                          @RequestParam Long publiciteId,
                          @RequestParam(required = false) Long volId,
                          @RequestParam Integer annee,
                          @RequestParam Integer mois,
                          @RequestParam Integer nombreDiffusions,
                          @RequestParam Double prixParDiffusion,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        try {
            pubService.saveDiffusion(id, publiciteId, volId, annee, mois, nombreDiffusions, prixParDiffusion);
            redirectAttributes.addFlashAttribute("success", "Diffusion pub mise à jour");
            return "redirect:/pub";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/pub/edit/" + id;
        }
    }

    @PostMapping("/pub/delete/{id}")
    public String deletePub(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        pubService.deleteDiffusion(id);
        redirectAttributes.addFlashAttribute("success", "Diffusion supprimée");
        return "redirect:/pub";
    }

    @GetMapping("/pub/stats")
    public String showPubStats(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               @RequestParam(required = false) Long departId,
                               @RequestParam(required = false) Long arriveeId,
                               @RequestParam(required = false) Long compagnieId,
                               Model model,
                               HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        var stats = pubService.getVolAdRevenue(startDate, endDate, departId, arriveeId, compagnieId);
        var topVol = stats.isEmpty() ? null : stats.get(0);

        List<String> labels = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();
        stats.forEach(s -> {
            if (s.getVol() != null) {
                labels.add(s.getVol().getAeroportDepart().getCodeIata() + " -> " + s.getVol().getAeroportArrivee().getCodeIata());
            } else {
                labels.add("N/A");
            }
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

        return "views/pub/stats";
    }

    private void prepareFormModel(Model model, DiffusionPub diffusion) {
        model.addAttribute("diffusion", diffusion);
        model.addAttribute("publicites", pubService.listPublicites());
        model.addAttribute("vols", pubService.listVols());
    }

    private void preparePubliciteForm(Model model, Publicite publicite) {
        model.addAttribute("publicite", publicite);
        model.addAttribute("societes", pubService.listSocietes());
    }
}
