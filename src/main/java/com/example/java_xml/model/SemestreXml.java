package com.example.java_xml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class SemestreXml {
    private int num;
    private List<EtudiantXml> etudiants;
    @XmlAttribute
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }

    @XmlElement(name = "etudiants")
    public List<EtudiantXml> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<EtudiantXml> etudiants) {
        this.etudiants = etudiants;
    }
}
