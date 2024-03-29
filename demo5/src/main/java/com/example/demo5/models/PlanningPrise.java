package com.example.demo5.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "planningprise")
public class PlanningPrise {

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
    private Timestamp datedebut;

    @Column(nullable = false)
    private Timestamp datefin;

    @Column(insertable = false, updatable = false)
    private Timestamp dateaction;

    @Column(nullable = false)
    private Double valeurconsommation;

    @Column(nullable = false)
    private Boolean done = false;

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

    public Timestamp getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Timestamp datedebut) {
        this.datedebut = datedebut;
    }

    public Timestamp getDatefin() {
        return datefin;
    }

    public void setDatefin(Timestamp datefin) {
        this.datefin = datefin;
    }

    public Timestamp getDateaction() {
        return dateaction;
    }

    public void setDateaction(Timestamp dateaction) {
        this.dateaction = dateaction;
    }

    public Double getValeurconsommation() {
        return valeurconsommation;
    }

    public void setValeurconsommation(Double valeurconsommation) {
        this.valeurconsommation = valeurconsommation;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}

