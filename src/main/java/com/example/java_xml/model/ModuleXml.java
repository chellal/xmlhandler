package com.example.java_xml.model;
import com.example.java_xml.model.EtudiantXml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ModuleXml {
    private String nom;
    private int id;
    private List<EtudiantXml> etudiants;

    @XmlElement(name = "nom")
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @XmlElement(name = "etudiants")
    public List<EtudiantXml> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<EtudiantXml> etudiants) {
        this.etudiants = etudiants;
    }
}
