package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PlanningBatterie;
import com.example.demo5.models.PlanningPrise;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.PlanningBatterieRepository;
import com.example.demo5.repositories.PlanningPriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/solarplanningprise")
@CrossOrigin("*")
public class PlanningPriseController {
    private final PlanningPriseRepository planningPriseRepository;
    private final ModuleSolarRepository moduleSolarRepository;
    private final PlanningBatterieRepository planningBatterieRepository;

    @Autowired
    public PlanningPriseController(PlanningPriseRepository planningPriseRepository, ModuleSolarRepository moduleSolarRepository, PlanningBatterieRepository planningBatterieRepository){
        this.planningPriseRepository = planningPriseRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.planningBatterieRepository = planningBatterieRepository;
    }

    @GetMapping("/insertplanningpriseoubatterie/{type}/{idmodule}/{date}/{tempsdebut}/{tempsfin}/{valeur}")
    public String insertplanning(@PathVariable("idmodule")Long idmodule,
                                 @PathVariable("type") int type,
                                 @PathVariable("date") String date,
                               @PathVariable("tempsdebut") String tempsdebut,
                               @PathVariable("tempsfin")String tempsfin,
                               @PathVariable("valeur")double valeur) throws ParseException {
        // 1 = prise
        // 2 = batterie

        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();

        Timestamp datedebut = Fonction.StringToTimestamp((date + " " + tempsdebut));
        Timestamp datefin = Fonction.StringToTimestamp((date + " " + tempsfin));
        
        if(datefin.before(datedebut)){
            return "dates invalides";
        }
        else{
            if(type == 1){
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
                    planningPrise.setValeurconsommation(valeur);
                    planningPriseRepository.save(planningPrise);
                    return "planning insere";
                }
            }
            if(type == 2){
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
                    planningBatterie.setValeurenergie(valeur);
                    planningBatterieRepository.save(planningBatterie);
                    return "planning insere";
                }
            }
            else {
                return "type invalide";
            }
        }
    }

    @GetMapping("/listeplanningbyidmodule/{idmodule}")
    public List<PlanningPrise> listeplanning(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return planningPriseRepository.findByModule(module);
    }
}
