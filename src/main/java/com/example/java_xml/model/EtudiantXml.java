package com.example.java_xml.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class EtudiantXml {
    private int id;
    private String apogee;
    private String nom;
    private String prenom;
    private String email;
    private Double noteNormale;
    private Double noteRatrappage;
    @JsonIgnore
    private String motdepasse;


    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getApogee() {
        return apogee;
    }

    public void setApogee(String apogee) {
        this.apogee = apogee;
    }

    @XmlElement
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @XmlElement
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @XmlElement
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement
    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }
    @XmlElement
    public Double getNoteNormale() {
        return noteNormale;
    }

    public void setNoteNormale(Double noteNormale) {
        this.noteNormale = noteNormale;
    }
    @XmlElement
    public Double getNoteRatrappage() {
        return noteRatrappage;
    }

    public void setNoteRatrappage(Double noteRatrappage) {
        this.noteRatrappage = noteRatrappage;
    }

    @Override
    public String toString() {
        return "EtudiantXml{" +
                "id=" + id +
                ", apogee='" + apogee + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motdepasse='" + motdepasse + '\'' +
                '}';
    }
}
