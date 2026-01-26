package com.aerienne.gestion.controller.vol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aerienne.gestion.model.prix.PrixVol;
import com.aerienne.gestion.model.pub.DiffusionPub;
import com.aerienne.gestion.model.pub.FacturePub;
import com.aerienne.gestion.model.vol.Vol;
import com.aerienne.gestion.model.vol.VolPlaceClasse;
import com.aerienne.gestion.repository.pub.PubRevenueView;
import com.aerienne.gestion.repository.pub.VolSocieteRevenueView;
import com.aerienne.gestion.service.aeroports.AeroportService;
import com.aerienne.gestion.service.avions.AvionService;
import com.aerienne.gestion.service.compagnies.CompagnieService;
import com.aerienne.gestion.service.pub.PubService;
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

    @Autowired
    private PubService pubService;

    @GetMapping("/vol/benefice")
    public String listVolsBenefice(@RequestParam(required = false) String depart,
                                   @RequestParam(required = false) String arrivee,
                                   @RequestParam(required = false) Long departId,
                                   @RequestParam(required = false) Long arriveeId,
                                   Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        List<Vol> vols = volService.getAllVols();
        if (departId != null) {
            vols = vols.stream().filter(v -> v.getAeroportDepart().getIdAeroport().equals(departId)).collect(Collectors.toList());
        } else if (depart != null && !depart.isEmpty()) {
            String dep = depart.toLowerCase();
            vols = vols.stream().filter(v -> v.getAeroportDepart().getNom().toLowerCase().contains(dep) ||
                                              v.getAeroportDepart().getCodeIata().toLowerCase().contains(dep)).collect(Collectors.toList());
        }
        if (arriveeId != null) {
            vols = vols.stream().filter(v -> v.getAeroportArrivee().getIdAeroport().equals(arriveeId)).collect(Collectors.toList());
        } else if (arrivee != null && !arrivee.isEmpty()) {
            String arr = arrivee.toLowerCase();
            vols = vols.stream().filter(v -> v.getAeroportArrivee().getNom().toLowerCase().contains(arr) ||
                                              v.getAeroportArrivee().getCodeIata().toLowerCase().contains(arr)).collect(Collectors.toList());
        }

        model.addAttribute("vols", vols);
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("selectedDepart", depart);
        model.addAttribute("selectedArrivee", arrivee);
        model.addAttribute("selectedDepartId", departId);
        model.addAttribute("selectedArriveeId", arriveeId);
        return "views/vol/benefice-list";
    }

    @GetMapping("/vol")
    public String listVols(@RequestParam(required = false) String depart,
                           @RequestParam(required = false) String arrivee,
                           @RequestParam(required = false) Long departId,
                           @RequestParam(required = false) Long arriveeId,
                           Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        List<Vol> vols = volService.getAllVols();
        if (departId != null) {
            vols = vols.stream().filter(v -> v.getAeroportDepart().getIdAeroport().equals(departId)).collect(Collectors.toList());
        } else if (depart != null && !depart.isEmpty()) {
            String dep = depart.toLowerCase();
            vols = vols.stream().filter(v -> v.getAeroportDepart().getNom().toLowerCase().contains(dep) ||
                                              v.getAeroportDepart().getCodeIata().toLowerCase().contains(dep)).collect(Collectors.toList());
        }
        if (arriveeId != null) {
            vols = vols.stream().filter(v -> v.getAeroportArrivee().getIdAeroport().equals(arriveeId)).collect(Collectors.toList());
        } else if (arrivee != null && !arrivee.isEmpty()) {
            String arr = arrivee.toLowerCase();
            vols = vols.stream().filter(v -> v.getAeroportArrivee().getNom().toLowerCase().contains(arr) ||
                                              v.getAeroportArrivee().getCodeIata().toLowerCase().contains(arr)).collect(Collectors.toList());
        }
        model.addAttribute("vols", vols);
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("selectedDepart", depart);
        model.addAttribute("selectedArrivee", arrivee);
        model.addAttribute("selectedDepartId", departId);
        model.addAttribute("selectedArriveeId", arriveeId);
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
    public String addVol(@ModelAttribute Vol vol, HttpSession session,
                         @RequestParam(name = "classe", required = false) List<String> classes,
                         @RequestParam(name = "seatsClasse", required = false) List<Integer> seatsClasse,
                         @RequestParam(name = "prixClasse", required = false) List<Double> prixClasse,
                         @RequestParam(name = "prixReductionClasse", required = false) List<Double> prixReductionClasse,
                         @RequestParam(name = "prixBebeClasse", required = false) List<Double> prixBebeClasse) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        Vol saved = volService.saveVol(vol);
        volService.replaceClassesAndPrices(saved, classes, seatsClasse, prixClasse, prixReductionClasse, prixBebeClasse);
        return "redirect:/vol";
    }

    @GetMapping("/vol/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        Vol vol = volService.getVolById(id);
        List<VolPlaceClasse> classes = volService.getClasses(id);
        List<PrixVol> prix = volService.getPrixByVol(id);
        Map<String, Double> prixMap = prix.stream()
            .collect(Collectors.toMap(PrixVol::getClasse, PrixVol::getPrix, (a, b) -> a));
        Map<String, Double> prixReductionMap = prix.stream()
            .collect(Collectors.toMap(PrixVol::getClasse, p -> p.getPrixReduction() != null ? p.getPrixReduction() : p.getPrix(), (a, b) -> a));
        Map<String, Double> prixBebeMap = prix.stream()
            .collect(Collectors.toMap(PrixVol::getClasse, p -> p.getPrixBebe() != null ? p.getPrixBebe() : p.getPrix() * 0.1, (a, b) -> a));
        model.addAttribute("vol", vol);
        model.addAttribute("avions", avionService.getAllAvions());
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("statuts", statutVolService.getAllStatuts());
        model.addAttribute("classes", classes);
        model.addAttribute("prix", prix);
        model.addAttribute("prixMap", prixMap);
        model.addAttribute("prixReductionMap", prixReductionMap);
        model.addAttribute("prixBebeMap", prixBebeMap);
        return "views/vol/edit";
    }

    @PostMapping("/vol/edit/{id}")
    public String editVol(@PathVariable Long id, @ModelAttribute Vol vol, HttpSession session,
                          @RequestParam(name = "classe", required = false) List<String> classes,
                          @RequestParam(name = "seatsClasse", required = false) List<Integer> seatsClasse,
                          @RequestParam(name = "prixClasse", required = false) List<Double> prixClasse,
                          @RequestParam(name = "prixReductionClasse", required = false) List<Double> prixReductionClasse,
                          @RequestParam(name = "prixBebeClasse", required = false) List<Double> prixBebeClasse) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        vol.setIdVol(id);
        Vol saved = volService.saveVol(vol);
        volService.replaceClassesAndPrices(saved, classes, seatsClasse, prixClasse, prixReductionClasse, prixBebeClasse);
        return "redirect:/vol";
    }

    @GetMapping("/vol/delete/{id}")
    public String deleteVol(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        try {
            volService.deleteVol(id);
            redirectAttributes.addFlashAttribute("success", "Vol supprimé avec succès");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
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

        var ticketStats = reservationService.getVolRevenue(startDate, endDate, departId, arriveeId, compagnieId);
        var adStats = pubService.getVolAdRevenue(startDate, endDate, departId, arriveeId, compagnieId);
        var adStatsBySociete = pubService.getVolAdRevenueBySociete(startDate, endDate, departId, arriveeId, compagnieId);
        Map<Long, FacturePub> factureMap = pubService.factureBySociete();
        var diffusions = pubService.listDiffusions();
        var paiementMap = pubService.paiementTotalsByDiffusion(diffusions.stream().map(DiffusionPub::getIdDiffusion).toList());

        Map<Long, Double> adRevenueByVol = adStats.stream()
            .filter(s -> s.getVol() != null && s.getVol().getIdVol() != null)
            .collect(Collectors.toMap(s -> s.getVol().getIdVol(), PubRevenueView::getRevenue, (a, b) -> a));

        Map<Long, Double> paidByVol = new HashMap<>();
        for (DiffusionPub d : diffusions) {
            if (d.getVol() == null || d.getVol().getIdVol() == null) {
                continue;
            }
            double paid = paiementMap.getOrDefault(d.getIdDiffusion(), 0d);
            paidByVol.merge(d.getVol().getIdVol(), paid, Double::sum);
        }

        Map<Long, Double> adPaidByVol = new HashMap<>();
        for (VolSocieteRevenueView view : adStatsBySociete) {
            if (view.getVol() == null || view.getVol().getIdVol() == null || view.getSociete() == null || view.getSociete().getIdSociete() == null) {
                continue;
            }
            FacturePub facture = factureMap.get(view.getSociete().getIdSociete());
            double ratio = 0d;
            if (facture != null) {
                double total = facture.getMontantTotal() != null ? facture.getMontantTotal() : 0d;
                double paye = facture.getMontantPaye() != null ? facture.getMontantPaye() : 0d;
                ratio = total > 0 ? Math.min(1d, paye / total) : 0d;
            }
            double paidShare = view.getRevenue() * ratio;
            adPaidByVol.merge(view.getVol().getIdVol(), paidShare, Double::sum);
        }

        List<VolStatsView> stats = ticketStats.stream()
            .map(s -> {
                double adRevenue = adRevenueByVol.getOrDefault(s.getVol().getIdVol(), 0d);
                double paid = adPaidByVol.getOrDefault(s.getVol().getIdVol(), 0d);
                double paidPct = adRevenue > 0 ? Math.min(100d, (paid / adRevenue) * 100d) : 0d;
                double paidDiffusion = paidByVol.getOrDefault(s.getVol().getIdVol(), 0d);
                double resteDiffusion = Math.max(0d, adRevenue - paidDiffusion);
                return new VolStatsView(s.getVol(), s.getRevenue(), adRevenue, s.getReservationsCount(), paidPct, paidDiffusion, resteDiffusion);
            })
            .sorted(Comparator.comparingDouble(VolStatsView::getTotalRevenue).reversed())
            .collect(Collectors.toList());

        var topVol = stats.isEmpty() ? null : stats.get(0);

        double totalTicketRevenue = stats.stream().mapToDouble(VolStatsView::getTicketRevenue).sum();
        double totalAdRevenue = stats.stream().mapToDouble(VolStatsView::getAdRevenue).sum();
        double totalPaidByDiffusion = stats.stream().mapToDouble(VolStatsView::getAdPaidByDiffusion).sum();
        double totalResteByDiffusion = stats.stream().mapToDouble(VolStatsView::getAdResteByDiffusion).sum();
        double totalCombinedRevenue = totalTicketRevenue + totalAdRevenue;
        long totalReservations = stats.stream().mapToLong(VolStatsView::getReservationsCount).sum();
        VolTotals totals = new VolTotals(totalTicketRevenue, totalAdRevenue, totalPaidByDiffusion, totalResteByDiffusion, totalCombinedRevenue, totalReservations);

        List<String> labels = new ArrayList<>();
        List<Double> ticketRevenues = new ArrayList<>();
        List<Double> adRevenues = new ArrayList<>();
        stats.forEach(s -> {
            labels.add(s.getVol().getAeroportDepart().getCodeIata() + " -> " + s.getVol().getAeroportArrivee().getCodeIata());
            ticketRevenues.add(s.getTicketRevenue());
            adRevenues.add(s.getAdRevenue());
        });

        model.addAttribute("stats", stats);
        model.addAttribute("topVol", topVol);
        model.addAttribute("totals", totals);
        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartTicketRevenues", ticketRevenues);
        model.addAttribute("chartAdRevenues", adRevenues);
        model.addAttribute("aeroports", aeroportService.getAllAeroports());
        model.addAttribute("compagnies", compagnieService.getAllCompagnies());

        model.addAttribute("selectedStart", startDate);
        model.addAttribute("selectedEnd", endDate);
        model.addAttribute("selectedDepart", departId);
        model.addAttribute("selectedArrivee", arriveeId);
        model.addAttribute("selectedCompagnie", compagnieId);

        return "views/vol/stats";
    }

    @GetMapping("/vol/benefice/{id}")
    public String showVolBenefice(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        Vol vol = volService.getVolById(id);
        if (vol == null) {
            return "redirect:/vol/benefice";
        }

        Double revenueMax = volService.getMaxRevenue(id);
        var classRevenue = volService.getClassRevenue(id);

        model.addAttribute("vol", vol);
        model.addAttribute("revenueMax", revenueMax != null ? revenueMax : 0d);
        model.addAttribute("classRevenue", classRevenue);
        return "views/vol/benefice-detail";
    }

    public static class VolStatsView {
        private final Vol vol;
        private final double ticketRevenue;
        private final double adRevenue;
        private final double adPaidPercent;
        private final double adUnpaidPercent;
        private final double adPaidByDiffusion;
        private final double adResteByDiffusion;
        private final long reservationsCount;

        public VolStatsView(Vol vol, Double ticketRevenue, Double adRevenue, Long reservationsCount, Double adPaidPercent,
                            Double adPaidByDiffusion, Double adResteByDiffusion) {
            this.vol = vol;
            this.ticketRevenue = ticketRevenue != null ? ticketRevenue : 0d;
            this.adRevenue = adRevenue != null ? adRevenue : 0d;
            double paidPct = adPaidPercent != null ? adPaidPercent : 0d;
            this.adPaidPercent = clampPercent(paidPct);
            this.adUnpaidPercent = clampPercent(100d - this.adPaidPercent);
            this.adPaidByDiffusion = adPaidByDiffusion != null ? adPaidByDiffusion : 0d;
            this.adResteByDiffusion = adResteByDiffusion != null ? Math.max(0d, adResteByDiffusion) : 0d;
            this.reservationsCount = reservationsCount != null ? reservationsCount : 0L;
        }

        public Vol getVol() {
            return vol;
        }

        public double getTicketRevenue() {
            return ticketRevenue;
        }

        public double getAdRevenue() {
            return adRevenue;
        }

        public double getAdPaidPercent() {
            return adPaidPercent;
        }

        public double getAdUnpaidPercent() {
            return adUnpaidPercent;
        }

        public double getAdPaidByDiffusion() {
            return adPaidByDiffusion;
        }

        public double getAdResteByDiffusion() {
            return adResteByDiffusion;
        }

        public double getTotalRevenue() {
            return ticketRevenue + adRevenue;
        }

        public long getReservationsCount() {
            return reservationsCount;
        }

        private double clampPercent(double value) {
            if (value < 0) {
                return 0d;
            }
            if (value > 100) {
                return 100d;
            }
            return value;
        }
    }

    public static class VolTotals {
        private final double ticketRevenue;
        private final double adRevenue;
        private final double adPaidByDiffusion;
        private final double adResteByDiffusion;
        private final double totalRevenue;
        private final long reservationsCount;

        public VolTotals(double ticketRevenue, double adRevenue, double adPaidByDiffusion, double adResteByDiffusion, double totalRevenue, long reservationsCount) {
            this.ticketRevenue = ticketRevenue;
            this.adRevenue = adRevenue;
            this.adPaidByDiffusion = adPaidByDiffusion;
            this.adResteByDiffusion = adResteByDiffusion;
            this.totalRevenue = totalRevenue;
            this.reservationsCount = reservationsCount;
        }

        public double getTicketRevenue() {
            return ticketRevenue;
        }

        public double getAdRevenue() {
            return adRevenue;
        }

        public double getAdPaidByDiffusion() {
            return adPaidByDiffusion;
        }

        public double getAdResteByDiffusion() {
            return adResteByDiffusion;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public long getReservationsCount() {
            return reservationsCount;
        }
    }
}
