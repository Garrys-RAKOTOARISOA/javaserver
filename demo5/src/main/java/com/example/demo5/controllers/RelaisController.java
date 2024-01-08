package com.example.demo5.controllers;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.RelaisBatterie;
import com.example.demo5.models.RelaisPrise;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.RelaisBatterieRepository;
import com.example.demo5.repositories.RelaisPriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solarrelais")
@CrossOrigin("*")
public class RelaisController {
    private final RelaisBatterieRepository relaisBatterieRepository;
    private final RelaisPriseRepository relaisPriseRepository;

    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public RelaisController(RelaisBatterieRepository relaisBatterieRepository, RelaisPriseRepository relaisPriseRepository, ModuleSolarRepository moduleSolarRepository){
        this.moduleSolarRepository = moduleSolarRepository;
        this.relaisBatterieRepository = relaisBatterieRepository;
        this.relaisPriseRepository = relaisPriseRepository;
    }

    @GetMapping("/switchrelaisbatterie/{idmodule}")
    public String switchrelaisbatterie(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        RelaisBatterie relais = relaisBatterieRepository.findByModule(module);
        if(relais.getState()){
            relais.setState(false);
        }
        else{
            relais.setState(true);
        }
        return "Switched batterie";
    }

    @GetMapping("/switchrelaisprise/{idmodule}")
    public String switchrelaisprise(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        RelaisPrise relais = relaisPriseRepository.findByModule(module);
        if(relais.getState()){
            relais.setState(false);
        }
        else{
            relais.setState(true);
        }
        return "Switched prise";
    }
}
