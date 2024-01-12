package com.example.demo5.models;

public class UsefulEntity {
    Long idclient;
    boolean state;
    int idmodule;

    public UsefulEntity(){}
    public Long getIdclient() {
        return idclient;
    }

    public void setIdclient(Long idclient) {
        this.idclient = idclient;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getIdmodule() {
        return idmodule;
    }

    public void setIdmodule(int idmodule) {
        this.idmodule = idmodule;
    }
}
