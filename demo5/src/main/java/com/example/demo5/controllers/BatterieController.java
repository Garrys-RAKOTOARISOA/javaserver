package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.*;
import com.example.demo5.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    private final NotificationModuleRepository notificationModuleRepository;
    private final CouleurBoutonBatterieRepository couleurBoutonBatterieRepository;
    private final DureeUtilisationBatterieRepository dureeUtilisationBatterieRepository;
    private final ReferenceValeurBatterieRepository referenceValeurBatterieRepository;
    private final ReferenceDureeBatterieRepository referenceDureeBatterieRepository;
    @Autowired
    public BatterieController(BatterieDataRepository batterieDataRepository, ModuleSolarRepository moduleSolarRepository, TypeBatterieRepository typeBatterieRepository, PlanningBatterieRepository planningBatterieRepository, RelaisBatterieRepository relaisBatterieRepository, NotificationModuleRepository notificationModuleRepository, CouleurBoutonBatterieRepository couleurBoutonBatterieRepository, DureeUtilisationBatterieRepository dureeUtilisationBatterieRepository, ReferenceDureeBatterieRepository referenceDureeBatterieRepository, ReferenceValeurBatterieRepository referenceValeurBatterieRepository){
        this.batterieDataRepository = batterieDataRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.typeBatterieRepository = typeBatterieRepository;
        this.planningBatterieRepository = planningBatterieRepository;
        this.relaisBatterieRepository = relaisBatterieRepository;
        this.notificationModuleRepository = notificationModuleRepository;
        this.couleurBoutonBatterieRepository = couleurBoutonBatterieRepository;
        this.dureeUtilisationBatterieRepository = dureeUtilisationBatterieRepository;
        this.referenceDureeBatterieRepository = referenceDureeBatterieRepository;
        this.referenceValeurBatterieRepository = referenceValeurBatterieRepository;
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
                Timestamp tempsFin = new Timestamp(listeplanning.get(i).getDatefin().getTime() / 1000 * 1000);
                Timestamp tempsDebut = new Timestamp(listeplanning.get(i).getDatedebut().getTime() / 1000 * 1000);
                if((tempsDebut.equals(temps))&&(courant==0)){
                    NotificationModule notification = new NotificationModule();
                    notification.setTemps(temps);
                    notification.setTexte("Le temps de planification sur la batterie commence  a "+temps);
                    notification.setModule(module);
                    notificationModuleRepository.save(notification);
                    if(relais.getState()){
                        relais.setState(false);
                    }
                    else{
                        relais.setState(true);
                    }
                }
                if(tempsFin.equals(temps)&&courant!=0){
                    NotificationModule notification = new NotificationModule();
                    notification.setTemps(temps);
                    notification.setTexte("Le temps d’utilisation planifié sur votre batterie s’est écoulé a "+temps);
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
                if((courant==0) && temps.after(tempsDebut) && temps.before(tempsFin)){
                    listeplanning.get(i).setDone(true);
                }
                if(courant >= listeplanning.get(i).getValeurenergie()){
                    NotificationModule notification = new NotificationModule();
                    notification.setTemps(temps);
                    notification.setTexte("La valeur d'energie dans la planification de la batterie a ete atteint, valeur= "+listeplanning.get(i).getValeurenergie()+" V a "+temps);
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

        DureeUtilisationBatterie dureeUtilisation = dureeUtilisationBatterieRepository.findByDateAndModule(todaydate, module).get(0);
        List<ReferenceDureeBatterie> referenceDureeBatterie = referenceDureeBatterieRepository.findByDateAndModule(todaydate, module);
        if(!referenceDureeBatterie.isEmpty()){
            if(!referenceDureeBatterie.get(0).isDone()){
                if(dureeUtilisation.getDuree()/3600 >= referenceDureeBatterie.get(0).getDureelimite()){
                    NotificationModule notification = new NotificationModule();
                    notification.setModule(module);
                    notification.setTexte("la duree d'utilisation du batterie a atteint la limite de celle du reference a"+temps);
                    notification.setTemps(temps);
                    notificationModuleRepository.save(notification);
                    referenceDureeBatterie.get(0).setDone(true);
                    referenceDureeBatterieRepository.save(referenceDureeBatterie.get(0));
                }
            }
        }

        List<ReferenceValeurBatterie> referenceValeurBatterie = referenceValeurBatterieRepository.findByDateAndModule(todaydate, module);
        if(!referenceValeurBatterie.isEmpty()){
            if(!referenceValeurBatterie.get(0).isDone()){
                if(energie > referenceValeurBatterie.get(0).getValeurlimite()){
                    NotificationModule notification = new NotificationModule();
                    notification.setModule(module);
                    notification.setTexte("l'energie de la batterie a atteint la limite de celle du reference a"+temps);
                    notification.setTemps(temps);
                    notificationModuleRepository.save(notification);
                    referenceValeurBatterie.get(0).setDone(true);
                    referenceValeurBatterieRepository.save(referenceValeurBatterie.get(0));
                }
            }
        }
    }

    @GetMapping("/getTensionBatterieByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
    public Object[] getTensionBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
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

    @GetMapping("/getEnergieBatterieByIdModuleAndTemps1Temps2/{idmodule}/{date}/{heure1}/{minute1}/{seconde1}/{heure2}/{minute2}/{seconde2}")
    public Object[] getEnergieBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure1") int heure1, @PathVariable("minute1") int minute1, @PathVariable("seconde1") int seconde1, @PathVariable("heure2") int heure2, @PathVariable("minute2") int minute2, @PathVariable("seconde2") int seconde2) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        Timestamp temps1 = Fonction.getTimestamp(Fonction.makeDate(date),heure1,minute1,seconde1);
        Timestamp temps2 = Fonction.getTimestamp(Fonction.makeDate(date),heure2,minute2,seconde2);
        double energie1 = 0;
        double energie2 = 0;
        for(int i=0; i<liste.size(); i++){
            Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
            if(tempsData.equals(temps1)){
                energie1 = liste.get(i).getEnergie();
            }
            if(tempsData.equals(temps2)){
                energie2 = liste.get(i).getEnergie();
            }
        }
        Object[] toreturn = new Object[3];
        toreturn[0] = temps1;
        toreturn[1] = temps2;
        toreturn[2] = energie2 - energie1;
        return toreturn;
    }

    @GetMapping("/getCourantBatterieByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
    public Object[] getCourantBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
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

    @GetMapping("/getPuissanceBatterieByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
    public Object[] getPuissanceBatterieByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute, seconde);
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

    @GetMapping("/listeBatterieDataByDateAndIdModule/{date}/{idmodule}")
    public List<BatterieData> listeBatterieDataByDateAndIdModule(@PathVariable("date") String date, @PathVariable("idmodule") Long idmodule) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<BatterieData> liste = batterieDataRepository.findByModule(module);
        List<BatterieData> toreturn = new ArrayList<>();
        LocalDate targetDate = Fonction.convertDateToLocalDate(Fonction.makeDate(date));
        for (int i = 0; i < liste.size(); i++) {
            LocalDate dataDate = Fonction.convertDateToLocalDate(Fonction.generateDate(liste.get(i).getTemps().getDate(),liste.get(i).getTemps().getMonth(),Fonction.makeYear(liste.get(i).getTemps())));
            if (dataDate.equals(targetDate)) {
                toreturn.add(liste.get(i));
            }
        }
        return toreturn;
    }

    @GetMapping("/getDureeBatterieByIdModuleAndDate/{idmodule}/{date}")
    public double getDureeBatterieByIdModuleAndDate(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date) throws ParseException {
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        double toreturn = 0;
        if(!dureeUtilisationBatterieRepository.findByDateAndModule(Fonction.makeDate(date), module).isEmpty()){
            toreturn = dureeUtilisationBatterieRepository.findByDateAndModule(Fonction.makeDate(date),module).get(0).getDuree()/3600;
        }
        return toreturn;
    }
    @GetMapping("/listeDureeBatterieMensuelleByIdModuleAndMonth/{idmodule}/{idmois}")
    public DureeBatterieMensuelle listeDureeBatterieMensuelle(@PathVariable("idmodule") Long idmodule, @PathVariable("idmois") int idmois){
        int annee = 2024;
        RestTemplate restTemplate = new RestTemplate();
        List<LocalDate> listedates = Fonction.getAllDatesInMonth(annee, Math.toIntExact(idmois));
        Double[][] toreturn = new Double[listedates.size()][2];
        for(int i=0; i<listedates.size(); i++){
            String url = "https://javaserver-production.up.railway.app/api/solarbatterie/getDureeBatterieByIdModuleAndDate/"+ idmodule +"/"+listedates.get(i).toString();
            toreturn[i][0] = (double) (i+1);
            toreturn[i][1] = restTemplate.getForObject(url, Double.class);
        }
        ArrayList<Object> semaine1 = new ArrayList<>();
        ArrayList<Object> semaine2 = new ArrayList<>();
        ArrayList<Object> semaine3 = new ArrayList<>();
        ArrayList<Object> semaine4 = new ArrayList<>();
        ArrayList<Object> semaine5 = new ArrayList<>();
        for(int i=0; i<7; i++){
            semaine1.add(listedates.get(i)+" "+ Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine1.add(toreturn[i][1]);
        }
        for(int i=7; i<14; i++){
            semaine2.add(listedates.get(i)+" "+ Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine2.add(toreturn[i][1]);
        }
        for(int i=14; i<21; i++){
            semaine3.add(listedates.get(i)+" "+ Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine3.add(toreturn[i][1]);
        }
        for(int i=21; i<28; i++){
            semaine4.add(listedates.get(i)+" "+ Fonction.castToFrDate(listedates.get(i).getDayOfWeek().toString()));
            semaine4.add(toreturn[i][1]);
        }
        if(listedates.size() == 29){
            semaine5.add(listedates.get(28)+" "+ Fonction.castToFrDate(listedates.get(28).getDayOfWeek().toString()));
            semaine5.add(toreturn[28][1]);
        }
        if(listedates.size() == 30){
            semaine5.add(listedates.get(28)+" "+ Fonction.castToFrDate(listedates.get(28).getDayOfWeek().toString()));
            semaine5.add(toreturn[28][1]);
            semaine5.add(listedates.get(29)+" "+ Fonction.castToFrDate(listedates.get(29).getDayOfWeek().toString()));
            semaine5.add(toreturn[29][1]);
        }
        if(listedates.size() == 31){
            semaine5.add(listedates.get(28)+" "+ Fonction.castToFrDate(listedates.get(28).getDayOfWeek().toString()));
            semaine5.add(toreturn[28][1]);
            semaine5.add(listedates.get(29)+" "+ Fonction.castToFrDate(listedates.get(29).getDayOfWeek().toString()));
            semaine5.add(toreturn[29][1]);
            semaine5.add(listedates.get(30)+" "+ Fonction.castToFrDate(listedates.get(30).getDayOfWeek().toString()));
            semaine5.add(toreturn[30][1]);
        }

        DureeBatterieMensuelle dureeBatterieMensuelle = new DureeBatterieMensuelle();
        dureeBatterieMensuelle.setIdmois(idmois);
        dureeBatterieMensuelle.setIdmodule(Math.toIntExact(idmodule));
        dureeBatterieMensuelle.setSemaine1(semaine1);
        dureeBatterieMensuelle.setSemaine2(semaine1);
        dureeBatterieMensuelle.setSemaine3(semaine3);
        dureeBatterieMensuelle.setSemaine4(semaine4);
        dureeBatterieMensuelle.setSemaine5(semaine5);
        return dureeBatterieMensuelle;
    }

    @GetMapping("/getDureeUtilisationBatterieIdMoisIdModule/{idmois}/{idmodule}")
    public double getDureeUtilisationMoisIdModule(@PathVariable("idmois") Long idmois, @PathVariable("idmodule") Long idmodule){
        int annee = 2024;
        List<LocalDate> listedates = Fonction.getAllDatesInMonth(annee, Math.toIntExact(idmois));

        RestTemplate restTemplate = new RestTemplate();
        double totalDuration = 0;

        for (LocalDate date : listedates) {
            String formattedDate = date.toString();
            String url = "https://javaserver-production.up.railway.app/api/solarbatterie/getDureeBatterieByIdModuleAndDate/" + idmodule + "/" + formattedDate;

            double duration = restTemplate.getForObject(url, Double.class);
            totalDuration += duration;
        }
        return totalDuration;
    }
    @GetMapping("/getDureeUtilisationBatterieAnuelleByIdModule/{idmodule}")
    public double[] getDureeUtilisationBatterieByIdModule(@PathVariable("idmodule") Long idmodule){
        double[] toreturn = new double[12];
        RestTemplate restTemplate = new RestTemplate();

        for (int i=0; i<12; i++){
            String url = "https://javaserver-production.up.railway.app/api/solarbatterie/getDureeUtilisationBatterieIdMoisIdModule/" + (i+1) + "/" + idmodule;
            toreturn[i] = restTemplate.getForObject(url, Double.class);
        }
        return toreturn;
    }
}