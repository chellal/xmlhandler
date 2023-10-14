package com.example.java_xml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "masterM2I")
public class MasterM2I {
    private Utilisateurs utilisateurs;

    @XmlElement(name = "utilisateurs")
    public Utilisateurs getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Utilisateurs utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
}

