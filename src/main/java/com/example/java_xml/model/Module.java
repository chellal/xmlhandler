package com.example.java_xml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Module {
    private int id;
    private String nom;
    private Double noteNormale;
    private Double noteRattrapage;

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    @XmlElement
    public Double getNoteNormale() {
        return noteNormale;
    }

    public void setNoteNormale(Double noteNormale) {
        this.noteNormale = noteNormale;
    }

    @XmlElement
    public Double getNoteRattrapage() {
        return noteRattrapage;
    }

    public void setNoteRattrapage(Double noteRattrapage) {
        this.noteRattrapage = noteRattrapage;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", noteNormale=" + noteNormale +
                ", noteRattrapage=" + noteRattrapage +
                '}';
    }
}
