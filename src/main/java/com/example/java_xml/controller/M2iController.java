package com.example.java_xml.controller;


import com.example.java_xml.metier.Metier;
import com.example.java_xml.model.Etudiant;
import com.example.java_xml.model.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class M2iController {
    @PostMapping(path = "/login")
    public String test(Model model, @RequestParam("username") String login, @RequestParam("password") String pass) {
        Etudiant etudiant = null;
        String render = "";
        try {
            etudiant = Metier.loginEtudiant(login, pass);
            System.out.println(etudiant);
            if (etudiant==null) {
                // Handle failed login (for example, redirect back to login with an error message)
                model.addAttribute("error", "Invalid credentials. Please try again.");
                render = "index";
            }else {
                model.addAttribute("etudiant", etudiant);
                render = "profil";
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            return render;
        }
    }

    @GetMapping(path = "/")
    public String accueil(Model model) {
        return ("index");
    }

    @GetMapping(path = "/all")
    public String getAllEtudiants(Model model,
                                  @RequestParam(name = "keyword", defaultValue = "") String keyword
    ) {
        List<Etudiant> etudiants = null;
        try {
            etudiants = Metier.getAllEtudiants().stream()
                    .filter(etudiant -> etudiant.getNom().contains(keyword) || etudiant.getPrenom().contains(keyword))
                    .collect(Collectors.toList());
            model.addAttribute("keyword", keyword);
            model.addAttribute("etudiants", etudiants);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            return "etudiants";
        }
    }

    @GetMapping(path = "/etudiantssemestre")
    public String getEtudiantsBySemestre(Model model, @RequestParam(name = "semestre", defaultValue = "0") String semestre
    ) {
        List<Etudiant> etudiants = null;
        try {
            if (semestre.equals("0")) {
                etudiants = Metier.getAllEtudiants();
            } else {
                etudiants = Metier.getEtudiantBySemestre(Integer.parseInt(semestre));
            }
            model.addAttribute("semestre", semestre);
            model.addAttribute("etudiants", etudiants);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            return "semestre";
        }
    }

    @GetMapping(path = "/etudiantsmodule")
    public String getEtudiantsByModule(Model model, @RequestParam(name = "module", defaultValue = "0") String moduleId
    ) {
        List<Etudiant> etudiants = null;
        try {
            if (moduleId.equals("0")) {
                etudiants = Metier.getAllEtudiants();
            } else {
                etudiants = Metier.getEtudiantByModule(Integer.parseInt(moduleId));
            }
            List<Module> modules = Metier.getAllModules();
            model.addAttribute("modules", modules);
            model.addAttribute("module", moduleId);
            model.addAttribute("etudiants", etudiants);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            return "module";
        }
    }

    @GetMapping(path = "/profil")
    public String getEtudiant(Model model,
                              @RequestParam(name = "id", defaultValue = "") String id
    ) {
        Etudiant etudiant = null;
        try {
            etudiant = Metier.getAllEtudiants().stream()
                    .filter(etud -> etud.getId() == Integer.parseInt(id))
                    .findFirst()
                    .orElse(null);
            model.addAttribute("etudiant", etudiant);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            return "profil";
        }
    }


}
