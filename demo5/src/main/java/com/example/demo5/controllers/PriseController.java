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

    @RestController
    @RequestMapping("/api/solarprise")
    @CrossOrigin("*")
    public class PriseController {
        private final PriseDataRepository priseDataRepository;
        private final ModuleSolarRepository moduleSolarRepository;
        private final PlanningPriseRepository planningPriseRepository;
        private final RelaisPriseRepository relaisPriseRepository;
        private final NotificationModuleRepository notificationModuleRepository;

        private final CouleurBoutonPriseRepository couleurBoutonPriseRepository;

        @Autowired
        public PriseController(PriseDataRepository priseDataRepository, ModuleSolarRepository moduleSolarRepository, PlanningPriseRepository planningPriseRepository, RelaisPriseRepository relaisPriseRepository, NotificationModuleRepository notificationModuleRepository, CouleurBoutonPriseRepository couleurBoutonPriseRepository){
            this.priseDataRepository = priseDataRepository;
            this.moduleSolarRepository = moduleSolarRepository;
            this.planningPriseRepository = planningPriseRepository;
            this.relaisPriseRepository = relaisPriseRepository;
            this.notificationModuleRepository = notificationModuleRepository;
            this.couleurBoutonPriseRepository = couleurBoutonPriseRepository;
        }

        @GetMapping("/listeprisedatabyidmodule/{idmodule}")
        public List<PriseData> listeDonnee(@PathVariable("idmodule")Long idmodule){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            return priseDataRepository.findByModule(module);
        }

        @GetMapping("/insertprisedata/{idmodule}/{consommation}/{tension}/{puissance}/{courant}")
        public void insertPriseData(@PathVariable("idmodule")Long id,
                                    @PathVariable("consommation") double consommation,
                                    @PathVariable("tension") double tension,
                                    @PathVariable("puissance") double puissance,
                                    @PathVariable("courant") double courant){
            Timestamp temps = Fonction.getCurrentTimestamp();
            ModuleSolar module = moduleSolarRepository.findById(id).get();
            PriseData priseData = new PriseData();
            priseData.setModule(module);
            priseData.setConsommation(consommation);
            priseData.setTension(tension);
            priseData.setPuissance(puissance);
            priseData.setCourant(courant);
            priseDataRepository.save(priseData);

            CouleurBoutonPrise couleurBoutonPrise = couleurBoutonPriseRepository.findByModule(module);
            if(courant==0){
                couleurBoutonPrise.setCouleur("rouge");
            }
            else{
                couleurBoutonPrise.setCouleur("vert");
            }
            couleurBoutonPriseRepository.save(couleurBoutonPrise);

            RelaisPrise relais = relaisPriseRepository.findByModule(module);
            List<PlanningPrise> listeprise = planningPriseRepository.findByModuleOrderByDatedebut(module);
            for (int i=0; i<listeprise.size(); i++){
                if(!listeprise.get(i).getDone()){
                    if((listeprise.get(i).getDatedebut().equals(temps))&&(courant==0)){
                        NotificationModule notification = new NotificationModule();
                        notification.setTemps(temps);
                        notification.setTexte("Relais prise a ete allumee a "+temps);
                        notification.setModule(module);
                        notificationModuleRepository.save(notification);
                        if(relais.getState()){
                            relais.setState(false);
                        }
                        else{
                            relais.setState(true);
                        }
                    }
                    if(listeprise.get(i).getDatefin().equals(temps)){
                        NotificationModule notification = new NotificationModule();
                        notification.setTemps(temps);
                        notification.setTexte("Relais prise a ete eteint a "+temps);
                        notification.setModule(module);
                        notificationModuleRepository.save(notification);
                        if(relais.getState()){
                            relais.setState(false);
                        }
                        else{
                            relais.setState(true);
                        }
                        listeprise.get(i).setDone(true);
                    }
                    if(courant >= listeprise.get(i).getValeurconsommation()){
                        NotificationModule notification = new NotificationModule();
                        notification.setTemps(temps);
                        notification.setTexte("Relais prise a ete eteint, consommation "+listeprise.get(i).getValeurconsommation()+" Watt atteint a "+temps);
                        notification.setModule(module);
                        notificationModuleRepository.save(notification);
                        if(relais.getState()){
                            relais.setState(false);
                        }
                        else{
                            relais.setState(true);
                        }
                        listeprise.get(i).setDone(true);
                    }
                    planningPriseRepository.save(listeprise.get(i));
                }
            }
        }

        @GetMapping("/getTensionPriseByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
        public double getTensionPriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps = Fonction.getTimestamp(date,heure,minute);
            double toreturn = 0;
            for(int i=0; i<liste.size(); i++){
                if(liste.get(i).getTemps().equals(temps)){
                    toreturn = liste.get(i).getTension();
                }
            }
            return toreturn;
        }

        @GetMapping("/getConsommationPriseByIdModuleAndTemps1Temps2/{idmodule}/{date}/{heure1}/{minute1}/{heure2}/{minute2}")
        public double getConsommationPriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure1") int heure1, @PathVariable("minute1") int minute1, @PathVariable("heure2") int heure2, @PathVariable("minute2") int minute2){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps1 = Fonction.getTimestamp(date,heure1,minute1);
            Timestamp temps2 = Fonction.getTimestamp(date,heure2,minute2);
            double conso1 = 0;
            double conso2 = 0;
            for(int i=0; i<liste.size(); i++){
                if(liste.get(i).getTemps().equals(temps1)){
                    conso1 = liste.get(i).getConsommation();
                }
                if(liste.get(i).getTemps().equals(temps2)){
                    conso2 = liste.get(i).getConsommation();
                }
            }
            return conso2 - conso1;
        }

        @GetMapping("/getCourantPriseByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
        public double getCourantPriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps = Fonction.getTimestamp(date,heure,minute);
            double toreturn = 0;
            for(int i=0; i<liste.size(); i++){
                if(liste.get(i).getTemps().equals(temps)){
                    toreturn = liste.get(i).getCourant();
                }
            }
            return toreturn;
        }

        @GetMapping("/getPuissancePriseByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}")
        public double getPuissancePriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date, @PathVariable("heure") int heure, @PathVariable("minute") int minute){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps = Fonction.getTimestamp(date,heure,minute);
            double toreturn = 0;
            for(int i=0; i<liste.size(); i++){
                if(liste.get(i).getTemps().equals(temps)){
                    toreturn = liste.get(i).getPuissance();
                }
            }
            return toreturn;
        }

        @GetMapping("/listePriseDataByDateAndIdModule/{date}/{idmodule}")
        public List<PriseData> listePriseDataByDateAndIdModule(@PathVariable("date") Date date, @PathVariable("idmodule") Long idmodule){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get() ;
            List<PriseData> liste = priseDataRepository.findByModule(module);
            List<PriseData> toreturn = new ArrayList<>();
            for(int i=0; i<liste.size(); i++){
                Date dataDate = Fonction.generateDate(liste.get(i).getTemps().getDate(),liste.get(i).getTemps().getMonth(),liste.get(i).getTemps().getYear());
                if(dataDate.equals(date)){
                    toreturn.add(liste.get(i));
                }
            }
            return toreturn;
        }

        @GetMapping("/getConsommationPriseByIdModuleAndDate/{idmodule}/{date}")
        public double getConsommationPriseByIdModuleAndDate(@PathVariable("idmodule") Long idmodule, @PathVariable("date") Date date){
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            List<PriseData> realliste = new ArrayList<>();
            for(int i=0; i<liste.size(); i++){
                Date dataDate = Fonction.generateDate(liste.get(i).getTemps().getDate(),liste.get(i).getTemps().getMonth(),liste.get(i).getTemps().getYear());
                if(date.equals(dataDate)){
                    realliste.add(liste.get(i));
                }
            }
            double toreturn = 0;
            if(realliste.size()!=0){
                toreturn = realliste.get(realliste.size() - 1).getConsommation();
            }
            return toreturn;
        }
    }