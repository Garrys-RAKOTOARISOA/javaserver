package com.example.demo5.controllers;

import com.example.demo5.models.*;
import com.example.demo5.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solarrelais")
@CrossOrigin("*")
public class RelaisController {
    private final RelaisBatterieRepository relaisBatterieRepository;
    private final RelaisPriseRepository relaisPriseRepository;
    private final RelaisPanneauRepository relaisPanneauRepository;
    private final CouleurBoutonPanneauRepository couleurBoutonPanneauRepository;
    private final CouleurBoutonPriseRepository couleurBoutonPriseRepository;
    private final CouleurBoutonBatterieRepository couleurBoutonBatterieRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public RelaisController(RelaisBatterieRepository relaisBatterieRepository, RelaisPriseRepository relaisPriseRepository, RelaisPanneauRepository relaisPanneauRepository, CouleurBoutonBatterieRepository couleurBoutonBatterieRepository, CouleurBoutonPanneauRepository couleurBoutonPanneauRepository, CouleurBoutonPriseRepository couleurBoutonPriseRepository, ModuleSolarRepository moduleSolarRepository){
        this.moduleSolarRepository = moduleSolarRepository;
        this.relaisBatterieRepository = relaisBatterieRepository;
        this.relaisPriseRepository = relaisPriseRepository;
        this.relaisPanneauRepository = relaisPanneauRepository;
        this.couleurBoutonPanneauRepository = couleurBoutonPanneauRepository;
        this.couleurBoutonBatterieRepository = couleurBoutonBatterieRepository;
        this.couleurBoutonPriseRepository = couleurBoutonPriseRepository;
    }

    @GetMapping("/switchrelaisbatterie/{idmodule}")
    public ClassSuccess switchrelaisbatterie(@PathVariable("idmodule") Long idmodule) {
        ClassSuccess toreturn = new ClassSuccess();
        ModuleSolar module = moduleSolarRepository.findById(idmodule).orElse(null);

        if (module == null) {
            toreturn.setMessage("Module non trouvé");
            return toreturn;
        }

        RelaisBatterie relais = relaisBatterieRepository.findByModule(module);
        CouleurBoutonBatterie couleurBoutonBatterie = couleurBoutonBatterieRepository.findByModule(module);

        if (couleurBoutonBatterie.getCouleur().equals("green")) {
            toreturn.setMessage("Relais Batterie éteint");
            relais.setState("HIGH");
            couleurBoutonBatterie.setCouleur("red");
        } else {
            toreturn.setMessage("Relais Batterie allumé");
            relais.setState("LOW");
            couleurBoutonBatterie.setCouleur("green");
        }

        relaisBatterieRepository.save(relais);
        couleurBoutonBatterieRepository.save(couleurBoutonBatterie);
        return toreturn;
    }

    @GetMapping("/switchrelaisprise/{idmodule}")
    public ClassSuccess switchrelaisprise(@PathVariable("idmodule") Long idmodule) {
        ClassSuccess toreturn = new ClassSuccess();
        ModuleSolar module = moduleSolarRepository.findById(idmodule).orElse(null);

        if (module == null) {
            toreturn.setMessage("Module non trouvé");
            return toreturn;
        }

        RelaisPrise relais = relaisPriseRepository.findByModule(module);
        CouleurBoutonPrise couleurBoutonPrise = couleurBoutonPriseRepository.findByModule(module);

        if (couleurBoutonPrise.getCouleur().equals("green")) {
            toreturn.setMessage("Relais prise éteint");
            relais.setState("HIGH");
            couleurBoutonPrise.setCouleur("red");
        } else {
            toreturn.setMessage("Relais prise allumé");
            relais.setState("LOW");
            couleurBoutonPrise.setCouleur("green");
        }

        relaisPriseRepository.save(relais);
        couleurBoutonPriseRepository.save(couleurBoutonPrise);
        return toreturn;
    }

    @GetMapping("/switchrelaispanneau/{idmodule}")
    public ClassSuccess switchrelaispanneau(@PathVariable("idmodule") Long idmodule) {
        ClassSuccess toreturn = new ClassSuccess();
        ModuleSolar module = moduleSolarRepository.findById(idmodule).orElse(null);

        if (module == null) {
            toreturn.setMessage("Module non trouvé");
            return toreturn;
        }

        RelaisPanneau relais = relaisPanneauRepository.findByModule(module);
        CouleurBoutonPanneau couleurBoutonPanneau = couleurBoutonPanneauRepository.findByModule(module);

        if (couleurBoutonPanneau.getCouleur().equals("green")) {
            toreturn.setMessage("Relais panneau éteint");
            relais.setState("HIGH");
            couleurBoutonPanneau.setCouleur("red");
        } else {
            toreturn.setMessage("Relais panneau allumé");
            relais.setState("LOW");
            couleurBoutonPanneau.setCouleur("green");
        }

        relaisPanneauRepository.save(relais);
        couleurBoutonPanneauRepository.save(couleurBoutonPanneau);
        return toreturn;
    }

    @GetMapping("/getrelaisbatteriebyidmodule/{idmodule}")
    public RelaisBatterie getrelaisbatterie(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return relaisBatterieRepository.findByModule(module);
    }

    @GetMapping("/getrelaisprisebyidmodule/{idmodule}")
    public RelaisPrise getrelaisprise(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return relaisPriseRepository.findByModule(module);
    }

    @GetMapping("/getrelaispanneaubyidmodule/{idmodule}")
    public RelaisPanneau getrelaispanneau(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return relaisPanneauRepository.findByModule(module);
    }
}