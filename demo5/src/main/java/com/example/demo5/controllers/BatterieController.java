package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.BatterieData;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.TypeBatterie;
import com.example.demo5.repositories.BatterieDataRepository;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.TypeBatterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/solarbatterie")
@CrossOrigin("*")
public class BatterieController {
    private final BatterieDataRepository batterieDataRepository;
    private final ModuleSolarRepository moduleSolarRepository;
    private final TypeBatterieRepository typeBatterieRepository;

    @Autowired
    public BatterieController(BatterieDataRepository batterieDataRepository, ModuleSolarRepository moduleSolarRepository, TypeBatterieRepository typeBatterieRepository){
        this.batterieDataRepository = batterieDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.typeBatterieRepository = typeBatterieRepository;
    }

    @GetMapping("/listebatteriedatabyidmodule/{idmodule}")
    public List<BatterieData> getliste(@PathVariable("idmodule")Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return batterieDataRepository.findByModule(module);
    }

    @GetMapping("/insertbatteriedata/{idmodule}/{tension}/{energie}/{courant}/{puissance}")
    public void insertion(@PathVariable("idmodule")Long idmodule,
                          @PathVariable("tension")double tension,
                          @PathVariable("energie")double energie,
                          @PathVariable("courant")double courant,
                          @PathVariable("puissance")double puissance){
        Timestamp temps = Fonction.getCurrentTimestamp();
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        TypeBatterie typeBatterie = typeBatterieRepository.findById(module.getTypeBatterie().getId()).get();
        BatterieData batterieData =  new BatterieData();
        batterieData.setModule(module);
        batterieData.setTension(tension);
        batterieData.setEnergie(energie);
        batterieData.setCourant(courant);
        batterieData.setPuissance(puissance);
        batterieData.setTemps(temps);
        batterieData.setPourcentage((tension * 100)/typeBatterie.getValeur());
        batterieDataRepository.save(batterieData);
    }
}
