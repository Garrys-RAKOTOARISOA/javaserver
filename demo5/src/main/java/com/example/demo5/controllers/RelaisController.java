package com.example.demo5.controllers;

import com.example.demo5.models.*;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.RelaisBatterieRepository;
import com.example.demo5.repositories.RelaisPanneauRepository;
import com.example.demo5.repositories.RelaisPriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solarrelais")
@CrossOrigin("*")
public class RelaisController {
    private final RelaisBatterieRepository relaisBatterieRepository;
    private final RelaisPriseRepository relaisPriseRepository;
    private final RelaisPanneauRepository relaisPanneauRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public RelaisController(RelaisBatterieRepository relaisBatterieRepository, RelaisPriseRepository relaisPriseRepository, RelaisPanneauRepository relaisPanneauRepository, ModuleSolarRepository moduleSolarRepository){
        this.moduleSolarRepository = moduleSolarRepository;
        this.relaisBatterieRepository = relaisBatterieRepository;
        this.relaisPriseRepository = relaisPriseRepository;
        this.relaisPanneauRepository = relaisPanneauRepository;
    }

    @GetMapping("/switchrelaisbatterie/{idmodule}")
    public String switchrelaisbatterie(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        RelaisBatterie relais = relaisBatterieRepository.findByModule(module);
        if(relais.getState().equals("HIGH")){
            relais.setState("LOW");
        }
        else{
            relais.setState("HIGH");
        }
        relaisBatterieRepository.save(relais);
        return "Switched batterie";
    }

    @GetMapping("/switchrelaisprise/{idmodule}")
    public String switchrelaisprise(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        RelaisPrise relais = relaisPriseRepository.findByModule(module);
        if(relais.getState().equals("HIGH")){
            relais.setState("LOW");
        }
        else{
            relais.setState("HIGH");
        }
        relaisPriseRepository.save(relais);
        return "Switched prise";
    }

    @GetMapping("/switchrelaispanneau/{idmodule}")
    public String switchrelaispanneau(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        RelaisPanneau relais = relaisPanneauRepository.findByModule(module);
        if(relais.getState().equals("HIGH")){
            relais.setState("LOW");
        }
        else{
            relais.setState("HIGH");
        }
        relaisPanneauRepository.save(relais);
        return "Switched Panneau";
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