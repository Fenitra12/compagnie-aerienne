package com.aerienne.gestion.controller.login;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aerienne.gestion.model.utilisateurs.Utilisateur;
import com.aerienne.gestion.service.utilisateurs.UtilisateurService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/")
    public String loginForm() {
        return "login/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String mdp,
                        Model model,
                        HttpSession session) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.login(username, mdp);
        
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            session.setAttribute("user", utilisateur);
            session.setAttribute("username", utilisateur.getUsername());
            session.setAttribute("role", utilisateur.getRole());
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
