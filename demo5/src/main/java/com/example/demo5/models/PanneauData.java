package com.example.demo5.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "panneaudata")
public class PanneauData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idmodule", nullable = false)
    @JsonIgnore
    private ModuleSolar module;

    @JsonProperty("idmodule")
    public Long getModuleId() {
        return (module != null) ? module.getId() : null;
    }

    @Column(nullable = false)
    private Double production;

    @Column(nullable = false)
    private Double tension;

    @Column(nullable = false)
    private Double puissance;

    @Column(nullable = false)
    private Double courant;

    @Column(insertable = false, updatable = false)
    private Timestamp temps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModuleSolar getModule() {
        return module;
    }

    public void setModule(ModuleSolar module) {
        this.module = module;
    }

    public Double getProduction() {
        return production;
    }

    public void setProduction(Double production) {
        this.production = production;
    }

    public Double getTension() {
        return tension;
    }

    public void setTension(Double tension) {
        this.tension = tension;
    }

    public Double getPuissance() {
        return puissance;
    }

    public void setPuissance(Double puissance) {
        this.puissance = puissance;
    }

    public Double getCourant() {
        return courant;
    }

    public void setCourant(Double courant) {
        this.courant = courant;
    }

    public Timestamp getTemps() {
        return temps;
    }

    public void setTemps(Timestamp temps) {
        this.temps = temps;
    }
}

