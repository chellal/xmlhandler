package com.example.java_xml.metier;


import com.example.java_xml.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Metier {

    public static List<Etudiant> getAllEtudiants() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MasterM2I.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        MasterM2I masterM2I = connexionXmlFile("data/database.xml");
        List<Etudiant> etudiants = masterM2I.getUtilisateurs().getEtudiants();

        return etudiants;
    }

    public static Etudiant getEtudiantByApogee(String apogee) throws JAXBException {
        Etudiant etudiant = getAllEtudiants().stream()
                .filter(etud -> etud.getApogee().equals(apogee))
                .findFirst()
                .orElse(null);
        return etudiant;
    }
    public static List<Etudiant> getEtudiantBySemestre(int num) throws JAXBException {
        List<Etudiant> etudiantFiltre = getAllEtudiants().stream()
                .filter(etudiant -> etudiant.getSemestres().stream()
                        .anyMatch(semestre -> semestre.getNum() == num))
                .collect(Collectors.toList());
        return etudiantFiltre;
    }

    public static List<Etudiant> getEtudiantByModule(int id) throws JAXBException {
        List<Etudiant> etudiantFiltre = getAllEtudiants().stream()
                .filter(etudiant -> etudiant.getSemestres().stream()
                        .anyMatch(semestre -> semestre.getModules().stream().anyMatch(module -> module.getId() == id)))
                .collect(Collectors.toList());
        return etudiantFiltre;
    }

    public static List<Module> getAllModules() throws JAXBException {
        List<Etudiant> etudiants = getAllEtudiants();

        List<Module> modules = etudiants.stream()
                .flatMap(etudiant -> etudiant.getSemestres().stream()
                        .flatMap(semestre -> semestre.getModules().stream()))
                .collect(Collectors.toMap(
                        Module::getId, // Key is the ID of the module
                        Function.identity(), // Value is the module itself
                        (existing, replacement) -> existing // Merge function to keep existing modules (remove duplicates)
                ))
                .values().stream()
                .collect(Collectors.toList());
        return modules;
    }

    public static Etudiant loginEtudiant(String apogee, String motDePasse) throws JAXBException {
        Etudiant etudiant = getAllEtudiants().stream()
                .filter(etud -> etud.getApogee().equals(apogee) && etud.getMotdepasse().equals(motDePasse))
                .findFirst()
                .orElse(null);
        return etudiant;
    }

    public static Administrateur loginAdmin(String email, String motDePasse) throws JAXBException {
        MasterM2I masterM2I = connexionXmlFile("data/database.xml");
        Administrateur administrateur = masterM2I.getUtilisateurs().getAdministrateur();
        System.out.println(administrateur);
        if (administrateur != null) {
            if ((administrateur.getEmail().equals(email)) && (administrateur.getMotdepasse().equals(motDePasse))) {
                return administrateur;
            }
        }
        return null;
    }

    public static Etudiant addEtudiant(Etudiant etudiant) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MasterM2I.class);
            MasterM2I masterM2I = connexionXmlFile("data/database.xml");
            List<Etudiant> etudiants = masterM2I.getUtilisateurs().getEtudiants();
            etudiants.add(etudiant);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Save the updated XML data to a file
            marshaller.marshal(masterM2I, new File("updated_data.xml"));


        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return etudiant;
    }

    public static void deleteEtudiant(int id) throws JAXBException {
        List<Etudiant> etudiants = getAllEtudiants();

        // Remove the Etudiant with the specified ID from the list
        List<Etudiant> listEtudiants = etudiants.stream()
                .filter(etudiant -> etudiant.getId() != id)
                .collect(Collectors.toList());
        if (listEtudiants.isEmpty()) {
            System.out.println("Aucun etudiant avec ce id");
        } else {
            // Save the updated list of Etudiants back to the XML file
            saveEtudiantsToXml(listEtudiants, "data/updated_data.xml");
        }

    }

    public static void modifierNoteNormal(String apogee, int moduleId, Double noteNormale) throws JAXBException {
        List<Etudiant> etudiants = getAllEtudiants();
        // Trouver l'étudiant par son apogee
        Etudiant targetEtudiant = etudiants.stream()
                .filter(etudiant -> etudiant.getApogee().equals(apogee))
                .findFirst()
                .orElse(null);
        // Si l'étudiant avec l'apogee spécifié est trouvé, mettre à jour les notes du module spécifié
        if (targetEtudiant != null) {
            // Trouver le module par son ID
            Module targetModule = targetEtudiant.getSemestres().stream()
                    .flatMap(semestre -> semestre.getModules().stream())
                    .filter(module -> module.getId() == moduleId)
                    .findFirst()
                    .orElse(null);

            // Si le module avec l'ID spécifié est trouvé, mettre à jour les notes
            if (targetModule != null) {
                targetModule.setNoteNormale(noteNormale);

                // Enregistrez la liste d'étudiants mise à jour dans le fichier XML
                saveEtudiantsToXml(etudiants, "data/updated_data.xml");
            }
        } else {
            System.out.println("Aucun étudiant trouvé avec l'apogée spécifié.");
        }
    }

    public static void modifierNoteRattrapage(String apogee, int moduleId, Double noteNRattrapage) throws JAXBException {
        List<Etudiant> etudiants = getAllEtudiants();
        // Trouver l'étudiant par son apogee
        Etudiant targetEtudiant = etudiants.stream()
                .filter(etudiant -> etudiant.getApogee().equals(apogee))
                .findFirst()
                .orElse(null);
        // Si l'étudiant avec l'apogee spécifié est trouvé, mettre à jour les notes du module spécifié
        if (targetEtudiant != null) {
            // Trouver le module par son ID
            Module targetModule = targetEtudiant.getSemestres().stream()
                    .flatMap(semestre -> semestre.getModules().stream())
                    .filter(module -> module.getId() == moduleId)
                    .findFirst()
                    .orElse(null);

            // Si le module avec l'ID spécifié est trouvé, mettre à jour les notes
            if (targetModule != null) {
                targetModule.setNoteRattrapage(noteNRattrapage);

                // Enregistrez la liste d'étudiants mise à jour dans le fichier XML
                saveEtudiantsToXml(etudiants, "data/updated_data.xml");
            }
        } else {
            System.out.println("Aucun étudiant trouvé avec l'apogée spécifié.");
        }
    }

    public static ModuleXml moduleXmlExport(int id) throws JAXBException{
        Module module = getAllModules().stream()
                .filter(module1 -> module1.getId() == id)
                .findFirst()
                .orElse(null);
        ModuleXml moduleXmlResult = new ModuleXml();
        moduleXmlResult.setId(module.getId());
        moduleXmlResult.setNom(module.getNom());
        List<Etudiant> etudiants = getEtudiantByModule(id);
        List<EtudiantXml> etudiantToAdd = new ArrayList<>();
        etudiants.forEach(etudiant -> {
            EtudiantXml etudiantXml = new EtudiantXml();
            etudiantXml.setApogee(etudiant.getApogee());
            etudiantXml.setId(etudiant.getId());
            etudiantXml.setNom(etudiant.getNom());
            etudiantXml.setPrenom(etudiant.getPrenom());
            etudiantXml.setEmail(etudiant.getEmail());
            etudiantXml.setApogee(etudiant.getApogee());
            etudiantToAdd.add(etudiantXml);
        });
        moduleXmlResult.setEtudiants(etudiantToAdd);
        return moduleXmlResult;
    }

    public static SemestreXml semestreJsonExport(int num) throws JAXBException{
        SemestreXml semestreXml  = new SemestreXml();
        semestreXml.setNum(num);
        List<Etudiant> etudiants = getEtudiantBySemestre(num);
        List<EtudiantXml> etudiantToAdd = new ArrayList<>();
        etudiants.forEach(etudiant -> {
            EtudiantXml etudiantXml = new EtudiantXml();
            etudiantXml.setApogee(etudiant.getApogee());
            etudiantXml.setId(etudiant.getId());
            etudiantXml.setNom(etudiant.getNom());
            etudiantXml.setPrenom(etudiant.getPrenom());
            etudiantXml.setEmail(etudiant.getEmail());
            etudiantXml.setApogee(etudiant.getApogee());
            etudiantToAdd.add(etudiantXml);
        });
        semestreXml.setEtudiants(etudiantToAdd);
        return semestreXml;
    }

    private static void saveEtudiantsToXml(List<Etudiant> etudiants, String fileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MasterM2I.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Utilisateurs utilisateurs = new Utilisateurs();
            utilisateurs.setEtudiants(etudiants);

            MasterM2I masterM2I = new MasterM2I();
            masterM2I.setUtilisateurs(utilisateurs);

            // Marshal the updated data back to XML
            marshaller.marshal(masterM2I, new File(fileName));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static MasterM2I connexionXmlFile(String file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(MasterM2I.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // XML file to be unmarshalled
        File xmlFile = new File(file);
        MasterM2I masterM2I = (MasterM2I) unmarshaller.unmarshal(xmlFile);
        return masterM2I;
    }
}
