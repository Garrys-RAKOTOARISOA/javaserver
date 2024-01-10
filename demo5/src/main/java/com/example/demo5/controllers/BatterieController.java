package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.*;
import com.example.demo5.repositories.*;
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
    private final PlanningBatterieRepository planningBatterieRepository;
    private final RelaisBatterieRepository relaisBatterieRepository;

    @Autowired
    public BatterieController(BatterieDataRepository batterieDataRepository, ModuleSolarRepository moduleSolarRepository, TypeBatterieRepository typeBatterieRepository, PlanningBatterieRepository planningBatterieRepository, RelaisBatterieRepository relaisBatterieRepository){
        this.batterieDataRepository = batterieDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.typeBatterieRepository = typeBatterieRepository;
        this.planningBatterieRepository = planningBatterieRepository;
        this.relaisBatterieRepository = relaisBatterieRepository;
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
        RelaisBatterie relais = relaisBatterieRepository.findByModule(module);
        List<PlanningBatterie> listeplanning = planningBatterieRepository.findAllByOrderByDatedebutAscAndIdmodule(module.getId());
        for (int i=0; i<listeplanning.size(); i++){
            if(!listeplanning.get(i).getDone()){
                if((listeplanning.get(i).getDatedebut().equals(temps))&&(courant==0)){
                    if(relais.getState()){
                        relais.setState(false);
                    }
                    else{
                        relais.setState(true);
                    }
                }
                if(listeplanning.get(i).getDatefin().equals(temps)){
                    if(relais.getState()){
                        relais.setState(false);
                    }
                    else{
                        relais.setState(true);
                    }
                    listeplanning.get(i).setDone(true);
                }
                if(courant >= listeplanning.get(i).getValeurenergie()){
                    if(relais.getState()){
                        relais.setState(false);
                    }
                    else{
                        relais.setState(true);
                    }
                    listeplanning.get(i).setDone(true);
                }
                planningBatterieRepository.save(listeplanning.get(i));
            }
        }
    }
}
