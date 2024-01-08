package com.example.demo5.models;


import javax.persistence.*;

@Entity
@Table(name = "module")
public class ModuleSolar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "qrcode")
    private String qrcode;

    @Column(name = "nommodule")
    private String nommodule;

    @Column(name = "ssid")
    private String ssid;

    @Column(name = "pass")
    private String pass;

    @ManyToOne
    @JoinColumn(name = "idbatterie", referencedColumnName = "id")
    private TypeBatterie typeBatterie;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getNommodule() {
        return nommodule;
    }

    public void setNommodule(String nommodule) {
        this.nommodule = nommodule;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public TypeBatterie getTypeBatterie() {
        return typeBatterie;
    }

    public void setTypeBatterie(TypeBatterie typeBatterie) {
        this.typeBatterie = typeBatterie;
    }
}
