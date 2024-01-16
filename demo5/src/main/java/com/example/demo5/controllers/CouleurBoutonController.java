package com.example.demo5.controllers;

import com.example.demo5.models.CouleurBoutonBatterie;
import com.example.demo5.models.CouleurBoutonPanneau;
import com.example.demo5.models.CouleurBoutonPrise;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.repositories.CouleurBoutonBatterieRepository;
import com.example.demo5.repositories.CouleurBoutonPanneauRepository;
import com.example.demo5.repositories.CouleurBoutonPriseRepository;
import com.example.demo5.repositories.ModuleSolarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solarbouton")
@CrossOrigin("*")
public class CouleurBoutonController {
    private final CouleurBoutonBatterieRepository couleurBoutonBatterieRepository;
    private final CouleurBoutonPriseRepository couleurBoutonPriseRepository;
    private final CouleurBoutonPanneauRepository couleurBoutonPanneauRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public CouleurBoutonController(CouleurBoutonPriseRepository couleurBoutonPriseRepository, CouleurBoutonBatterieRepository couleurBoutonBatterieRepository, CouleurBoutonPanneauRepository couleurBoutonPanneauRepository, ModuleSolarRepository moduleSolarRepository){
        this.couleurBoutonBatterieRepository = couleurBoutonBatterieRepository;
        this.couleurBoutonPriseRepository = couleurBoutonPriseRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.couleurBoutonPanneauRepository = couleurBoutonPanneauRepository;
    }

    @GetMapping("/couleurbatteriebyidmodule/{idmodule}")
    public CouleurBoutonBatterie getByIdModuleBatterie(@PathVariable("idmodule")Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return couleurBoutonBatterieRepository.findByModule(module);
    }

    @GetMapping("/couleurprisebyidmodule/{idmodule}")
    public CouleurBoutonPrise getByIdModulePrise(@PathVariable("idmodule")Long idmodule){
        ModuleSolar moduleSolar = moduleSolarRepository.findById(idmodule).get();
        return couleurBoutonPriseRepository.findByModule(moduleSolar);
    }

    @GetMapping("/couleurpanneaubyidmodule/{idmodule}")
    public CouleurBoutonPanneau getByIdModulePanneau(@PathVariable("idmodule") Long idmodule){
        ModuleSolar moduleSolar = moduleSolarRepository.findById(idmodule).get();
        return couleurBoutonPanneauRepository.findByModule(moduleSolar);
    }
}
