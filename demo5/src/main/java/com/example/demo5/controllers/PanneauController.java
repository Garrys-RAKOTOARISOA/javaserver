package com.example.demo5.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.BatterieData;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PanneauData;
import com.example.demo5.models.PriseData;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.PanneauDataRepository;
import org.hibernate.dialect.TimesTenDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/solarpanneau")
@CrossOrigin("*")

public class PanneauController {
    private final PanneauDataRepository panneauDataRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public PanneauController(PanneauDataRepository panneauDataRepository, ModuleSolarRepository moduleSolarRepository){
        this.panneauDataRepository = panneauDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/listepanneaudatabyidmodule/{idmodule}")
    public List<PanneauData> getliste(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return panneauDataRepository.findByModule(module);
    }

    @GetMapping("/insertpanneaudata/{idmodule}/{production}/{tension}/{puissance}/{courant}")
    public void insertPanneauData(@PathVariable("idmodule") Long idmodule,
                                  @PathVariable("production") double production,
                                  @PathVariable("tension") double tension,
                                  @PathVariable("puissance") double puissance,
                                  @PathVariable("courant") double courant){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        PanneauData panneauData = new PanneauData();
        panneauData.setModule(module);
        panneauData.setProduction(production);
        panneauData.setTension(tension);
        panneauData.setPuissance(puissance);
        panneauData.setCourant(courant);
        panneauDataRepository.save(panneauData);
    }

    @GetMapping("/getproductionspecifiee/{idmodule}/{tempsdebut}/{tempsfin}")
    public List<PanneauData> getListe(
            @PathVariable("idmodule") Long idmodule,
            @PathVariable("tempsdebut") Timestamp tempsdebut,
            @PathVariable("tempsfin") Timestamp tempsfin) {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        List<PanneauData> realliste = new ArrayList<>();
        for (PanneauData panneauData : liste) {
            if (panneauData.getTemps().compareTo(tempsdebut) >= 0 && panneauData.getTemps().compareTo(tempsfin) <= 0) {
                realliste.add(panneauData);
            }
        }
        return realliste;
    }

    @GetMapping("/getTensionPanneauByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
    public double getTensionPanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(date,heure,minute);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps)){
                toreturn = liste.get(i).getTension();
            }
        }
        return toreturn;
    }

    @GetMapping("/getProductionPanneauByIdModuleAndTemps1Temps2/{idmodule}/{date}/{heure1}/{minute1}/{heure2}/{minute2}")
    public double getProductionPanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure1") int heure1, @PathVariable("minute1") int minute1, @PathVariable("heure2") int heure2, @PathVariable("minute2") int minute2){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps1 = Fonction.getTimestamp(date,heure1,minute1);
        Timestamp temps2 = Fonction.getTimestamp(date,heure2,minute2);
        double prod1 = 0;
        double prod2 = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps1)){
                prod1 = liste.get(i).getProduction();
            }
            if(liste.get(i).getTemps().equals(temps2)){
                prod2 = liste.get(i).getProduction();
            }
        }
        return prod2 - prod1;
    }

    @GetMapping("/getCourantPanneauByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
    public double getCourantPanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(date,heure,minute);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps)){
                toreturn = liste.get(i).getCourant();
            }
        }
        return toreturn;
    }

    @GetMapping("/getPuissancePanneauByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
    public double getPuissancePanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(date,heure,minute);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getTemps().equals(temps)){
                toreturn = liste.get(i).getPuissance();
            }
        }
        return toreturn;
    }

    @GetMapping("/listePanneauDataByDateAndIdModule/{date}/{idmodule}")
    public List<PanneauData> listePanneauDataByDateAndIdModule(@PathVariable("date") Date date, @PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get() ;
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        List<PanneauData> toreturn = new ArrayList<>();
        for(int i=0; i<liste.size(); i++){
            Date dataDate = Fonction.generateDate(liste.get(i).getTemps().getDate(),liste.get(i).getTemps().getMonth(),liste.get(i).getTemps().getYear());
            if(dataDate.equals(date)){
                toreturn.add(liste.get(i));
            }
        }
        return toreturn;
    }

    @GetMapping("/getProductionPanneauByIdModuleAndDate/{idmodule}/{date}")
    public double getProductionPanneauByIdModuleAndDate(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        List<PanneauData> realliste = new ArrayList<>();
        for(int i=0; i<liste.size(); i++){
            Date dataDate = Fonction.generateDate(liste.get(i).getTemps().getDate(),liste.get(i).getTemps().getMonth(),liste.get(i).getTemps().getYear());
            if(date.equals(dataDate)){
                realliste.add(liste.get(i));
            }
        }
        double toreturn = 0;
        if(realliste.size()!=0){
            toreturn = realliste.get(realliste.size() - 1).getProduction();
        }
        return toreturn;
    }
}