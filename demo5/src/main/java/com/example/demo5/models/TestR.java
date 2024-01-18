package com.example.demo5.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "test")
public class TestR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Timestamp temps1;

    @Column(insertable = false, updatable = true)
    private Timestamp temps2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTemps1() {
        return temps1;
    }

    public void setTemps1(Timestamp temps1) {
        this.temps1 = temps1;
    }

    public Timestamp getTemps2() {
        return temps2;
    }

    public void setTemps2(Timestamp temps2) {
        this.temps2 = temps2;
    }
}
