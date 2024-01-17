package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.ReferenceDureeBatterie;
import com.example.demo5.models.ReferenceDureePrise;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.ReferenceDureeBatterieRepository;
import com.example.demo5.repositories.ReferenceDureePriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/solarreferenceduree")
@CrossOrigin("*")
public class ReferenceDureeController {
    private final ModuleSolarRepository moduleSolarRepository;

    private final ReferenceDureeBatterieRepository referenceDureeBatterieRepository;

    private final ReferenceDureePriseRepository referenceDureePriseRepository;
    @Autowired
    public ReferenceDureeController(ModuleSolarRepository moduleSolarRepository, ReferenceDureePriseRepository referenceDureePriseRepository, ReferenceDureeBatterieRepository referenceDureeBatterieRepository){
        this.moduleSolarRepository = moduleSolarRepository;
        this.referenceDureePriseRepository = referenceDureePriseRepository;
        this.referenceDureeBatterieRepository = referenceDureeBatterieRepository;
    }

    @GetMapping("/insertion/{choix}/{duree}/{idmodule}")
    public String insertion(@PathVariable("choix") int choix, @PathVariable("duree") double duree, @PathVariable("idmodule") Long idmodule){
        // 1 = prise
        // 2 = batterie
        String message = "";
        Date today = Fonction.getCurrentDate();
        ModuleSolar moduleSolar = moduleSolarRepository.findById(idmodule).get();
        if(choix==1){
            List<ReferenceDureePrise> liste = referenceDureePriseRepository.findByDateAndModule(today, moduleSolar);
            if(liste.isEmpty()){
                ReferenceDureePrise reference = new ReferenceDureePrise();
                reference.setModule(moduleSolar);
                reference.setDureelimite(duree);
                reference.setDate(today);
                referenceDureePriseRepository.save(reference);
                message += "Insertion du reference";
            }
            else{
                message += "Il existe deja une a cette date";
            }
        }
        else{
            List<ReferenceDureeBatterie> liste = referenceDureeBatterieRepository.findByDateAndModule(today, moduleSolar);
            if(liste.isEmpty()){
                ReferenceDureeBatterie reference = new ReferenceDureeBatterie();
                reference.setModule(moduleSolar);
                reference.setDureelimite(duree);
                reference.setDate(today);
                referenceDureeBatterieRepository.save(reference);
                message += "Insertion du reference";
            }
            else{
                message += "Il existe deja une a cette date";
            }
        }
        return message;
    }
}
