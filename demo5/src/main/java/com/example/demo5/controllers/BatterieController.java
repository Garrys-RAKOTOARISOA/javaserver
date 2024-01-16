package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.*;
import com.example.demo5.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@RestController
@RequestMapping("/api/solarbatterie")
@CrossOrigin("*")
public class BatterieController {
    private final BatterieDataRepository batterieDataRepository;
    private final ModuleSolarRepository moduleSolarRepository;
    private final TypeBatterieRepository typeBatterieRepository;
    private final PlanningBatterieRepository planningBatterieRepository;
    private final RelaisBatterieRepository relaisBatterieRepository;
    private final NotificationModuleRepository notificationModuleRepository;

    private final CouleurBoutonBatterieRepository couleurBoutonBatterieRepository;

    private final DureeUtilisationBatterieRepository dureeUtilisationBatterieRepository;

    @Autowired
    public BatterieController(BatterieDataRepository batterieDataRepository, ModuleSolarRepository moduleSolarRepository, TypeBatterieRepository typeBatterieRepository, PlanningBatterieRepository planningBatterieRepository, RelaisBatterieRepository relaisBatterieRepository, NotificationModuleRepository notificationModuleRepository, CouleurBoutonBatterieRepository couleurBoutonBatterieRepository, DureeUtilisationBatterieRepository dureeUtilisationBatterieRepository){
        this.batterieDataRepository = batterieDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.typeBatterieRepository = typeBatterieRepository;
        this.planningBatterieRepository = planningBatterieRepository;
        this.relaisBatterieRepository = relaisBatterieRepository;
        this.notificationModuleRepository = notificationModuleRepository;
        this.couleurBoutonBatterieRepository = couleurBoutonBatterieRepository;
        this.dureeUtilisationBatterieRepository = dureeUtilisationBatterieRepository;
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
        batterieData.setPourcentage((tension * 100)/typeBatterie.getValeur());
        batterieDataRepository.save(batterieData);

        Date todaydate = Fonction.getCurrentDate();

        List<DureeUtilisationBatterie> existingEntries = dureeUtilisationBatterieRepository.findByDateAndModule(todaydate, module);

        DureeUtilisationBatterie dureeUtilisationBatterie;

        if (existingEntries.isEmpty()) {
            dureeUtilisationBatterie = new DureeUtilisationBatterie();
            dureeUtilisationBatterie.setDuree((double) 0);
            dureeUtilisationBatterie.setDate(todaydate);
            dureeUtilisationBatterie.setModule(module);
            dureeUtilisationBatterieRepository.save(dureeUtilisationBatterie);
        } else {
            if(courant > 0){
                dureeUtilisationBatterie = existingEntries.get(0);
                dureeUtilisationBatterie.setDuree(dureeUtilisationBatterie.getDuree() + 1);
                dureeUtilisationBatterieRepository.save(dureeUtilisationBatterie);
            }
        }

        CouleurBoutonBatterie couleurBoutonBatterie = couleurBoutonBatterieRepository.findByModule(module);
        if(courant==0){
            couleurBoutonBatterie.setCouleur("rouge");
        }
        else{
            couleurBoutonBatterie.setCouleur("vert");
        }
        couleurBoutonBatterieRepository.save(couleurBoutonBatterie);

        RelaisBatterie relais = relaisBatterieRepository.findByModule(module);
        List<PlanningBatterie> listeplanning = planningBatterieRepository.findByModuleOrderByDatedebut(module);
        for (int i=0; i<listeplanning.size(); i++){
            if(!listeplanning.get(i).getDone()){
                if((listeplanning.get(i).getDatedebut().equals(temps))&&(courant==0)){
                    NotificationModule notification = new NotificationModule();
                    notification.setTemps(temps);
                    notification.setTexte("Relais batterie a ete allumee a "+temps);
                    notification.setModule(module);
                    notificationModuleRepository.save(notification);
                    if(relais.getState()){
                        relais.setState(false);
                    }
                    else{
                        relais.setState(true);
                    }
                }
                if(listeplanning.get(i).getDatefin().equals(temps)){
                    NotificationModule notification = new NotificationModule();
                    notification.setTemps(temps);
                    notification.setTexte("Relais batterie a ete eteint a "+temps);
                    notification.setModule(module);
                    notificationModuleRepository.save(notification);
                    if(relais.getState()){
                        relais.setState(false);
                    }
                    else{
                        relais.setState(true);
                    }
                    listeplanning.get(i).setDone(true);
                }
                if(courant >= listeplanning.get(i).getValeurenergie()){
                    NotificationModule notification = new NotificationModule();
                    notification.setTemps(temps);
                    notification.setTexte("Relais batterie a ete eteint, energie "+listeplanning.get(i).getValeurenergie()+" V atteint a "+temps);
                    notification.setModule(module);
                    notificationModuleRepository.save(notification);
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

    @GetMapping("/getDureeBatterieByIdModuleAndDate/{idmodule}/{date}")
    public DureeUtilisationBatterie getDureeBatterieByIdModuleAndDate(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return dureeUtilisationBatterieRepository.findByDateAndModule(date,module).get(0);
    }

    @GetMapping("/getTensionBatterieByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
    public double getTensionBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(date,heure,minute);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps)){
                toreturn = liste.get(i).getTension();
            }
        }
        return toreturn;
    }

    @GetMapping("/getEnergieBatterieByIdModuleAndTemps1Temps2/{idmodule}/{date}/{heure1}/{minute1}/{heure2}/{minute2}")
    public double getEnergieBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure1") int heure1, @PathVariable("minute1") int minute1, @PathVariable("heure2") int heure2, @PathVariable("minute2") int minute2){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        Timestamp temps1 = Fonction.getTimestamp(date,heure1,minute1);
        Timestamp temps2 = Fonction.getTimestamp(date,heure2,minute2);
        double energie1 = 0;
        double energie2 = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps1)){
                energie1 = liste.get(i).getEnergie();
            }
            if(liste.get(i).getTemps().equals(temps2)){
                energie2 = liste.get(i).getEnergie();
            }
        }
        return energie2 - energie1;
    }

    @GetMapping("/getCourantBatterieByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
    public double getCourantBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(date,heure,minute);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps)){
                toreturn = liste.get(i).getCourant();
            }
        }
        return toreturn;
    }

    @GetMapping("/getPuissanceBatterieByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
    public double getPuissanceBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(date,heure,minute);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps)){
                toreturn = liste.get(i).getPuissance();
            }
        }
        return toreturn;
    }

    @GetMapping("/listeBatterieDataByDateAndIdModule/{date}/{idmodule}")
    public List<BatterieData> listeBatterieDataByDateAndIdModule(@PathVariable("date") Date date, @PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get() ;
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        List<BatterieData> toreturn = new ArrayList<>();
        for(int i=0; i<liste.size(); i++){
            Date dataDate = Fonction.generateDate(liste.get(i).getTemps().getDate(),liste.get(i).getTemps().getMonth(),liste.get(i).getTemps().getYear());
            if(dataDate.equals(date)){
                toreturn.add(liste.get(i));
            }
        }
        return toreturn;
    }
}