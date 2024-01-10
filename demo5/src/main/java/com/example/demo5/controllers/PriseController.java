    package com.example.demo5.controllers;

    import com.example.demo5.fonc.Fonction;
    import com.example.demo5.models.*;
    import com.example.demo5.repositories.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.sql.Timestamp;
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
            priseData.setTemps(temps);
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
    }