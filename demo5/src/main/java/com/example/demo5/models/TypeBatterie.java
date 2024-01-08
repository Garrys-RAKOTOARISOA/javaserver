package com.example.demo5.models;

import javax.persistence.*;

@Entity
@Table(name = "typebatterie")
public class TypeBatterie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "valeur")
    private double Valeur;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public double getValeur() {
        return Valeur;
    }

    public void setValeur(double valeur) {
        Valeur = valeur;
    }
}
