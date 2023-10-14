package com.example.java_xml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Utilisateurs {
    private List<Etudiant> etudiants;
    private Administrateur administrateur;

    @XmlElementWrapper(name = "etudiants")
    @XmlElement(name = "etudiant")
    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    @XmlElement(name = "administrateur")
    public Administrateur getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(Administrateur administrateur) {
        this.administrateur = administrateur;
    }
}
