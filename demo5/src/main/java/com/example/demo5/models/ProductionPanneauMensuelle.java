package com.example.demo5.models;

import java.util.ArrayList;

public class ProductionPanneauMensuelle {
    int idmois;
    int idmodule;

    ArrayList<Object> semaine1;
    ArrayList<Object> semaine2;
    ArrayList<Object> semaine3;
    ArrayList<Object> semaine4;
    ArrayList<Object> semaine5;

    public ProductionPanneauMensuelle(){}

    public int getIdmois() {
        return idmois;
    }

    public void setIdmois(int idmois) {
        this.idmois = idmois;
    }

    public int getIdmodule() {
        return idmodule;
    }

    public void setIdmodule(int idmodule) {
        this.idmodule = idmodule;
    }

    public ArrayList<Object> getSemaine1() {
        return semaine1;
    }

    public void setSemaine1(ArrayList<Object> semaine1) {
        this.semaine1 = semaine1;
    }

    public ArrayList<Object> getSemaine2() {
        return semaine2;
    }

    public void setSemaine2(ArrayList<Object> semaine2) {
        this.semaine2 = semaine2;
    }

    public ArrayList<Object> getSemaine3() {
        return semaine3;
    }

    public void setSemaine3(ArrayList<Object> semaine3) {
        this.semaine3 = semaine3;
    }

    public ArrayList<Object> getSemaine4() {
        return semaine4;
    }

    public void setSemaine4(ArrayList<Object> semaine4) {
        this.semaine4 = semaine4;
    }

    public ArrayList<Object> getSemaine5() {
        return semaine5;
    }

    public void setSemaine5(ArrayList<Object> semaine5) {
        this.semaine5 = semaine5;
    }
}
