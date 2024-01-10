package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PlanningBatterie;
import com.example.demo5.models.PlanningPrise;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.PlanningBatterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/solarplanningbatterie")
@CrossOrigin("*")
public class PlanningBatterieController {
    private final PlanningBatterieRepository planningBatterieRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public PlanningBatterieController(PlanningBatterieRepository planningBatterieRepository, ModuleSolarRepository moduleSolarRepository){
        this.planningBatterieRepository = planningBatterieRepository;
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/insertplanning/{idmodule}/{datedebut}/{datefin}/{valeurenergie}")
    public String insertplanning(@PathVariable("idmodule")Long idmodule,
                                 @PathVariable("datedebut") Timestamp datedebut,
                                 @PathVariable("datefin")Timestamp datefin,
                                 @PathVariable("valeurenergie")double valeurenergie){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();

        boolean planningExiste = planningBatterieRepository.existsByModuleAndDatedebutLessThanEqualAndDatefinGreaterThanEqual(
                module, datefin, datedebut);

        if (planningExiste) {
            return "il existe deja un planning a votre date";
        }
        else{
            PlanningBatterie planningBatterie = new PlanningBatterie();
            planningBatterie.setModule(module);
            planningBatterie.setDatedebut(datedebut);
            planningBatterie.setDatefin(datefin);
            planningBatterie.setDateaction(Fonction.getCurrentTimestamp());
            planningBatterie.setValeurenergie(valeurenergie);
            planningBatterieRepository.save(planningBatterie);
            return "planning insere";
        }
    }

    @GetMapping("/listeplanningbyidmodule/{idmodule}")
    public List<PlanningBatterie> listeplanning(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return planningBatterieRepository.findByModule(module);
    }

}
