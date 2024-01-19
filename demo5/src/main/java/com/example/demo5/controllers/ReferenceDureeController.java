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

    @GetMapping("/getreference/{choix}/{idmodule}")
    public Object getreference(@PathVariable("choix") int choix, @PathVariable("idmodule") Long idmodule){
        Date today = Fonction.getCurrentDate();
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        Object toreturn = new Object();
        if(choix == 1){
            List<ReferenceDureePrise> referenceDureePrise = referenceDureePriseRepository.findByDateAndModule(today,module);
            if(!referenceDureePrise.isEmpty()){
                toreturn = referenceDureePrise.get(0);
            }
        }
        if(choix == 2){
            List<ReferenceDureeBatterie> referenceDureeBatterie = referenceDureeBatterieRepository.findByDateAndModule(today,module);
            if(!referenceDureeBatterie.isEmpty()){
                toreturn = referenceDureeBatterie.get(0);
            }
        }
        return toreturn;
    }

    @GetMapping("/modification/{choix}/{duree}/{idmodule}")
    public String modification(@PathVariable("choix") int choix, @PathVariable("duree") double duree, @PathVariable("idmodule") Long idmodule){
        String text = "";
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        Date today = Fonction.getCurrentDate();
        if(choix == 1){
            ReferenceDureePrise referenceDureePrise = referenceDureePriseRepository.findByDateAndModule(today,module).get(0);
            referenceDureePrise.setDureelimite(duree);
            referenceDureePriseRepository.save(referenceDureePrise);
            text += "prise";
        }
        if(choix == 2){
            ReferenceDureeBatterie referenceDureeBatterie = referenceDureeBatterieRepository.findByDateAndModule(today,module).get(0);
            referenceDureeBatterie.setDureelimite(duree);
            referenceDureeBatterieRepository.save(referenceDureeBatterie);
            text += "batterie";
        }
        return "Reference duree modifiee "+text;
    }
}