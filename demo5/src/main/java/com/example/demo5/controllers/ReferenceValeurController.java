package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.ReferenceValeurBatterie;
import com.example.demo5.models.ReferenceValeurPrise;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.ReferenceValeurBatterieRepository;
import com.example.demo5.repositories.ReferenceValeurPriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/solarreferencevaleur")
@CrossOrigin("*")
public class ReferenceValeurController {
    private final ModuleSolarRepository moduleSolarRepository;

    private final ReferenceValeurPriseRepository referenceValeurPriseRepository;

    private final ReferenceValeurBatterieRepository referenceValeurBatterieRepository;

    @Autowired
    public ReferenceValeurController(ModuleSolarRepository moduleSolarRepository, ReferenceValeurBatterieRepository referenceValeurBatterieRepository, ReferenceValeurPriseRepository referenceValeurPriseRepository){
        this.moduleSolarRepository = moduleSolarRepository;
        this.referenceValeurBatterieRepository = referenceValeurBatterieRepository;
        this.referenceValeurPriseRepository = referenceValeurPriseRepository;
    }

    @GetMapping("/insertion/{choix}/{valeur}/{idmodule}")
    public String insertion(@PathVariable("choix") int choix, @PathVariable("valeur") double valeur, @PathVariable("idmodule") Long idmodule){
        // 1 = prise
        // 2 = batterie

        String message = "";
        Date today = Fonction.getCurrentDate();
        ModuleSolar moduleSolar = moduleSolarRepository.findById(idmodule).get();

        if(choix == 1){
            List<ReferenceValeurPrise> liste = referenceValeurPriseRepository.findByDateAndModule(today, moduleSolar);
            if(liste.isEmpty()){
                ReferenceValeurPrise reference = new ReferenceValeurPrise();
                reference.setModule(moduleSolar);
                reference.setValeurlimite(valeur);
                reference.setDate(today);
                referenceValeurPriseRepository.save(reference);
                message += "Insertion du reference";
            }
            else{
                message += "Il existe deja une a cette date";
            }
        }
        else{
            List<ReferenceValeurBatterie> liste = referenceValeurBatterieRepository.findByDateAndModule(today, moduleSolar);
            if(liste.isEmpty()){
                ReferenceValeurBatterie reference = new ReferenceValeurBatterie();
                reference.setModule(moduleSolar);
                reference.setValeurlimite(valeur);
                reference.setDate(today);
                referenceValeurBatterieRepository.save(reference);
                message += "Insertion du reference";
            }
            else{
                message += "Il existe deja une a cette date";
            }
        }
        return message;
    }
}
