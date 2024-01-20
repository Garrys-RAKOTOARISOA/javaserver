package com.example.demo5.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "relaisprise")
public class RelaisPrise {

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
    private String state;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

