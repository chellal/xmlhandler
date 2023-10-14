package com.example.java_xml.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Semestre {
    private int num;
    private List<Module> modules;

    @XmlAttribute
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @XmlElementWrapper(name = "modules")
    @XmlElement(name = "module")
    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "Semestre{" +
                "num=" + num +
                ", modules=" + modules +
                '}';
    }
}
