package com.example.java_xml.controller;


import com.example.java_xml.metier.Metier;
import com.example.java_xml.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class M2iController {
    @PostMapping(path = "/login")
    public String test(Model model, @RequestParam("username") String login, @RequestParam("password") String pass) throws JAXBException {
        Etudiant etudiant = null;
        Administrateur administrateur = Metier.loginAdmin(login, pass);
        String render = "";
        etudiant = Metier.loginEtudiant(login, pass);
        System.out.println(etudiant);
        if (etudiant == null) {
            if (administrateur == null) {
                model.addAttribute("error", "Invalid credentials. Please try again.");
                render = "index";
            } else {
                render = "redirect:/all";
            }
        } else {
            model.addAttribute("etudiant", etudiant);
            render = "profil";
        }
        return render;
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

    @GetMapping(value = "/xmlmodule", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> xmlResponse(@RequestParam(name = "id", defaultValue = "1") String moduleId, Model model) throws JAXBException {
        int id = Integer.parseInt(moduleId);
        ModuleXml moduleXml = Metier.moduleXmlExport(id);
        StringWriter stringWriter = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(ModuleXml.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(moduleXml, stringWriter);
        // Set the response headers to trigger download
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=module_"+id+".xml");
        // Return XML content as a downloadable file
        return ResponseEntity.ok()
                .headers(headers)
                .body(stringWriter.toString());
    }
    @GetMapping(value = "/jsonsemestre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> jsonResponse(@RequestParam(name = "id", defaultValue = "1") String semestreId, Model model) throws JAXBException, JsonProcessingException {
            int id = Integer.parseInt(semestreId);
            SemestreXml semestreXml = Metier.semestreJsonExport(id);

            // Serialize the SemestreXml object to JSON using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(semestreXml);

            // Set the response headers to trigger download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=semestre_" + id + ".json");

            // Return JSON content as a downloadable file
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(json);
        }
}
