package com.example.demo5.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PanneauData;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.PanneauDataRepository;
import org.hibernate.dialect.TimesTenDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/solarpanneau")
@CrossOrigin("*")

public class PanneauController {
    private final PanneauDataRepository panneauDataRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public PanneauController(PanneauDataRepository panneauDataRepository, ModuleSolarRepository moduleSolarRepository){
        this.panneauDataRepository = panneauDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/listepanneaudatabyidmodule/{idmodule}")
    public List<PanneauData> getliste(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return panneauDataRepository.findByModule(module);
    }

    @GetMapping("/insertpanneaudata/{idmodule}/{production}/{tension}/{puissance}/{courant}")
    public void insertPanneauData(@PathVariable("idmodule") Long idmodule,
                                  @PathVariable("production") double production,
                                  @PathVariable("tension") double tension,
                                  @PathVariable("puissance") double puissance,
                                  @PathVariable("courant") double courant){
        Timestamp temps = Fonction.getCurrentTimestamp();
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        PanneauData panneauData = new PanneauData();
        panneauData.setModule(module);
        panneauData.setProduction(production);
        panneauData.setTension(tension);
        panneauData.setPuissance(puissance);
        panneauData.setCourant(courant);
        panneauDataRepository.save(panneauData);
    }

    @GetMapping("/getproductionspecifiee/{idmodule}/{tempsdebut}/{tempsfin}")
    public List<PanneauData> getListe(
            @PathVariable("idmodule") Long idmodule,
            @PathVariable("tempsdebut") Timestamp tempsdebut,
            @PathVariable("tempsfin") Timestamp tempsfin) {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        List<PanneauData> realliste = new ArrayList<>();
        for (PanneauData panneauData : liste) {
            if (panneauData.getTemps().compareTo(tempsdebut) >= 0 && panneauData.getTemps().compareTo(tempsfin) <= 0) {
                realliste.add(panneauData);
            }
        }
        return realliste;
    }
}