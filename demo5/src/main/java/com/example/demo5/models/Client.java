package com.example.demo5.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String pass;

    @Column(nullable = false)
    private String codepostal;

    @Column(nullable = false)
    private String lienimage;

    @ManyToOne
    @JoinColumn(name = "idmodule", nullable = false)
    @JsonIgnore
    private ModuleSolar module;

    @JsonProperty("idmodule")
    public Long getModuleId() {
        return (module != null) ? module.getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getLienimage() {
        return lienimage;
    }

    public void setLienimage(String lienimage) {
        this.lienimage = lienimage;
    }

    public ModuleSolar getModule() {
        return module;
    }

    public void setModule(ModuleSolar module) {
        this.module = module;
    }
}

