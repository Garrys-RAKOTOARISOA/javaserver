package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PlanningPrise;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.PlanningPriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/solarplanningprise")
@CrossOrigin("*")
public class PlanningPriseController {
    private final PlanningPriseRepository planningPriseRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public PlanningPriseController(PlanningPriseRepository planningPriseRepository, ModuleSolarRepository moduleSolarRepository){
        this.planningPriseRepository = planningPriseRepository;
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/insertplanning/{idmodule}/{datedebut}/{datefin}/{valeurconsommation}")
    public String insertplanning(@PathVariable("idmodule")Long idmodule,
                               @PathVariable("datedebut") Timestamp datedebut,
                               @PathVariable("datefin")Timestamp datefin,
                               @PathVariable("valeurconsommation")double valeurconsommation){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();

        boolean planningExiste = planningPriseRepository.existsByModuleAndDatedebutLessThanEqualAndDatefinGreaterThanEqual(
                module, datefin, datedebut);

        if (planningExiste) {
            return "il existe deja un planning a votre date";
        }
        else{
            PlanningPrise planningPrise = new PlanningPrise();
            planningPrise.setModule(module);
            planningPrise.setDatedebut(datedebut);
            planningPrise.setDatefin(datefin);
            planningPrise.setValeurconsommation(valeurconsommation);
            planningPriseRepository.save(planningPrise);
            return "planning insere";
        }
    }

    @GetMapping("/listeplanningbyidmodule/{idmodule}")
    public List<PlanningPrise> listeplanning(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return planningPriseRepository.findByModule(module);
    }

}
