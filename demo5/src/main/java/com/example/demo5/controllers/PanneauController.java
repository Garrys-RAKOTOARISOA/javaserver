package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.*;
import com.example.demo5.repositories.CouleurBoutonPanneauRepository;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.PanneauDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/solarpanneau")
@CrossOrigin("*")

public class PanneauController {
    private final PanneauDataRepository panneauDataRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    private final CouleurBoutonPanneauRepository couleurBoutonPanneauRepository;
    @Autowired
    public PanneauController(PanneauDataRepository panneauDataRepository, ModuleSolarRepository moduleSolarRepository, CouleurBoutonPanneauRepository couleurBoutonPanneauRepository){
        this.panneauDataRepository = panneauDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.couleurBoutonPanneauRepository = couleurBoutonPanneauRepository;
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
//        CouleurBoutonPanneau bouton = couleurBoutonPanneauRepository.findByModule(module);
//        if(courant>0){
//            bouton.setCouleur("green");
//        }
//        else{
//            bouton.setCouleur("red");
//        }
//        couleurBoutonPanneauRepository.save(bouton);
    }

    @GetMapping("/getTensionPanneauByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
    public Object[] getTensionPanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute,seconde);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
            if(tempsData.equals(temps)){
                toreturn = liste.get(i).getTension();
            }
        }
        Object[] ret = new Object[2];
        ret[0] = temps;
        ret[1] = toreturn;
        return ret;
    }

    @GetMapping("/getProductionPanneauByIdModuleAndTemps1Temps2/{idmodule}/{date}/{heure1}/{minute1}/{seconde1}/{heure2}/{minute2}/{seconde2}")
    public Object[] getProductionPanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure1") int heure1, @PathVariable("minute1") int minute1, @PathVariable("heure2") int heure2, @PathVariable("minute2") int minute2, @PathVariable("seconde1") int seconde1, @PathVariable("seconde2") int seconde2) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps1 = Fonction.getTimestamp(Fonction.makeDate(date),heure1,minute1,seconde1);
        Timestamp temps2 = Fonction.getTimestamp(Fonction.makeDate(date),heure2,minute2,seconde2);
        double prod1 = 0;
        double prod2 = 0;
        for(int i=0; i<liste.size(); i++){
            Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
            if(tempsData.equals(temps1)){
                prod1 = liste.get(i).getProduction();
            }
            if(tempsData.equals(temps2)){
                prod2 = liste.get(i).getProduction();
            }
        }
        Object[] toreturn = new Object[3];
        toreturn[0] = temps1;
        toreturn[1] = temps2;
        toreturn[2] = prod2 - prod1;
        return toreturn;
    }

    @GetMapping("/getCourantPanneauByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
    public Object[] getCourantPanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute,seconde);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
            if(tempsData.equals(temps)){
                toreturn = liste.get(i).getCourant();
            }
        }
        Object[] ret = new Object[2];
        ret[0] = temps;
        ret[1] = toreturn;
        return ret;
    }

    @GetMapping("/getPuissancePanneauByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
    public Object[] getPuissancePanneauByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute,seconde);
        double toreturn = 0;
        for(int i=0; i<liste.size(); i++){
            Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
            if(tempsData.equals(temps)){
                toreturn = liste.get(i).getPuissance();
            }
        }
        Object[] ret = new Object[2];
        ret[0] = temps;
        ret[1] = toreturn;
        return ret;
    }

    @GetMapping("/listePanneauDataByDateAndIdModule/{date}/{idmodule}")
    public List<PanneauData> listePanneauDataByDateAndIdModule(@PathVariable("date") String date, @PathVariable("idmodule") Long idmodule) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get() ;
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        List<PanneauData> toreturn = new ArrayList<>();
        for(int i=0; i<liste.size(); i++){
            LocalDate dataDate = Fonction.timeStampToLocalDate(liste.get(i).getTemps());
            if(dataDate.equals(Fonction.convertDateToLocalDate(Fonction.makeDate(date)))){
                toreturn.add(liste.get(i));
            }
        }
        return toreturn;
    }

    @GetMapping("/getProductionPanneauByIdModuleAndDate/{idmodule}/{date}")
    public double getProductionPanneauByIdModuleAndDate(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<PanneauData> liste = panneauDataRepository.findByModule(module);
        List<PanneauData> realliste = new ArrayList<>();
        for(int i=0; i<liste.size(); i++){
            LocalDate dataDate = Fonction.timeStampToLocalDate(liste.get(i).getTemps());
            if(Fonction.makeDate(date).equals(dataDate)){
                realliste.add(liste.get(i));
            }
        }
        double toreturn = 0;
        if(realliste.size()!=0){
            toreturn = realliste.get(realliste.size() - 1).getProduction();
        }
        return toreturn;
    }

    @GetMapping("/getProductionPanneauIdMoisIdModule/{idmois}/{idmodule}")
    public double getProductionPanneauIdMoisIdModule(@PathVariable("idmois") Long idmois, @PathVariable("idmodule") Long idmodule){
        int annee = 2024;
        List<LocalDate> listedates = Fonction.getAllDatesInMonth(annee, Math.toIntExact(idmois));
        RestTemplate restTemplate = new RestTemplate();
        double totalProduction = 0;

        for (LocalDate date : listedates) {
            String formattedDate = date.toString();
            String url = "https://javaserver-production.up.railway.app/api/solarpanneau/getProductionPanneauByIdModuleAndDate/" + idmodule + "/" + formattedDate;

            double duration = restTemplate.getForObject(url, Double.class);
            totalProduction += duration;
        }
        return totalProduction;
    }

    @GetMapping("/getProductionPanneauAnnuelleIdModule/{idmodule}")
    public double[] getProductionPanneauAnnuelleIdModule(@PathVariable("idmodule") Long idmodule){
        RestTemplate restTemplate = new RestTemplate();
        double[] toreturn = new double[12];
        for(int i=0; i<toreturn.length; i++){
            String url = "https://javaserver-production.up.railway.app/api/solarpanneau/getProductionPanneauIdMoisIdModule/"+ (i+1) +"/"+ idmodule;
            toreturn[i] = restTemplate.getForObject(url, Double.class);
        }
        return toreturn;
    }

    @GetMapping("/listeProductionPanneauMensuelleByIdModuleAndMonth/{idmodule}/{idmois}")
    public ProductionPanneauMensuelle listeProductionPanneauMensuelleByIdModuleAndMonth(@PathVariable("idmodule") Long idmodule, @PathVariable("idmois") Long idmois){
        int annee = 2024;
        RestTemplate restTemplate = new RestTemplate();
        List<LocalDate> listedates = Fonction.getAllDatesInMonth(annee, Math.toIntExact(idmois));
        Double[][] toreturn = new Double[listedates.size()][2];
        for(int i=0; i<listedates.size(); i++){
            String url = "https://javaserver-production.up.railway.app/api/solarpanneau/getProductionPanneauByIdModuleAndDate/"+ idmodule +"/"+ listedates.get(i).toString();
            toreturn[i][0] = (double) (i+1);
            toreturn[i][1] = restTemplate.getForObject(url, Double.class);
            System.out.println(restTemplate.getForObject(url, Double.class));
        }
        ArrayList<Object> semaine1 = new ArrayList<>();
        ArrayList<Object> semaine2 = new ArrayList<>();
        ArrayList<Object> semaine3 = new ArrayList<>();
        ArrayList<Object> semaine4 = new ArrayList<>();
        ArrayList<Object> semaine5 = new ArrayList<>();
        for(int i=0; i<7; i++){
            semaine1.add(listedates.get(i) + " " + Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine1.add(toreturn[i][1]);
        }
        for(int i=7; i<14; i++){
            semaine2.add(listedates.get(i) + " " + Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine2.add(toreturn[i][1]);
        }
        for(int i=14; i<21; i++){
            semaine3.add(listedates.get(i) + " " + Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine3.add(toreturn[i][1]);
        }
        for(int i=21; i<28; i++){
            semaine4.add(listedates.get(i) + " " + Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine4.add(toreturn[i][1]);
        }
        if(listedates.size() == 29){
            semaine5.add(listedates.get(28) + " " + Fonction.castToFrDate(listedates.get(28).getDayOfWeek().toString()));
            semaine5.add(toreturn[28][1]);
        }
        if(listedates.size() == 30){
            semaine5.add(listedates.get(28) + " " + Fonction.castToFrDate(listedates.get(28).getDayOfWeek().toString()));
            semaine5.add(toreturn[28][1]);
            semaine5.add(listedates.get(29) + " " + Fonction.castToFrDate(listedates.get(29).getDayOfWeek().toString()));
            semaine5.add(toreturn[29][1]);
        }
        if(listedates.size() == 31){
            semaine5.add(listedates.get(28) + " " + Fonction.castToFrDate(listedates.get(28).getDayOfWeek().toString()));
            semaine5.add(toreturn[28][1]);
            semaine5.add(listedates.get(29) + " " + Fonction.castToFrDate(listedates.get(29).getDayOfWeek().toString()));
            semaine5.add(toreturn[29][1]);
            semaine5.add(listedates.get(30) + " " + Fonction.castToFrDate(listedates.get(30).getDayOfWeek().toString()));
            semaine5.add(toreturn[30][1]);
        }
        ProductionPanneauMensuelle productionPanneauMensuelle = new ProductionPanneauMensuelle();
        productionPanneauMensuelle.setIdmois(Math.toIntExact(idmois));
        productionPanneauMensuelle.setIdmodule(Math.toIntExact(idmodule));
        productionPanneauMensuelle.setSemaine1(semaine1);
        productionPanneauMensuelle.setSemaine2(semaine2);
        productionPanneauMensuelle.setSemaine3(semaine3);
        productionPanneauMensuelle.setSemaine4(semaine4);
        productionPanneauMensuelle.setSemaine5(semaine5);

        return productionPanneauMensuelle;
    }
}