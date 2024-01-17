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
            Timestamp temps =  Fonction.getCurrentTimestamp();
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
                    Timestamp tempsFin = new Timestamp(listeprise.get(i).getDatefin().getTime() / 1000 * 1000);
                    Timestamp tempsDebut = new Timestamp(listeprise.get(i).getDatedebut().getTime() / 1000 * 1000);
                    if((tempsDebut.equals(temps))&&(courant==0)){
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
                    if(tempsFin.equals(temps)&&courant!=0){
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

        @GetMapping("/getTensionPriseByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
        public double getTensionPriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute,seconde);
            double toreturn = 0;
            for(int i=0; i<liste.size(); i++){
                Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
                if(tempsData.equals(temps)){
                    toreturn = liste.get(i).getTension();
                }
            }
            return toreturn;
        }

        @GetMapping("/getConsommationPriseByIdModuleAndTemps1Temps2/{idmodule}/{date}/{heure1}/{minute1}/{seconde1}/{heure2}/{minute2}/{seconde2}")
        public double getConsommationPriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure1") int heure1, @PathVariable("minute1") int minute1, @PathVariable("heure2") int heure2, @PathVariable("minute2") int minute2, @PathVariable("seconde1") int seconde1, @PathVariable("seconde2") int seconde2) throws ParseException {
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps1 = Fonction.getTimestamp(Fonction.makeDate(date),heure1,minute1,seconde1);
            Timestamp temps2 = Fonction.getTimestamp(Fonction.makeDate(date),heure2,minute2,seconde2);
            double conso1 = 0;
            double conso2 = 0;
            for(int i=0; i<liste.size(); i++){
                Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
                if(tempsData.equals(temps1)){
                    conso1 = liste.get(i).getConsommation();
                }
                if(tempsData.equals(temps2)){
                    conso2 = liste.get(i).getConsommation();
                }
            }
            return conso2 - conso1;
        }

        @GetMapping("/getCourantPriseByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
        public double getCourantPriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute,seconde);
            double toreturn = 0;
            for(int i=0; i<liste.size(); i++){
                Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
                if(tempsData.equals(temps)){
                    toreturn = liste.get(i).getCourant();
                }
            }
            return toreturn;
        }

        @GetMapping("/getPuissancePriseByIdModuleAndTemps/{idmodule}/{date}/{heure}/{minute}/{seconde}")
        public double getPuissancePriseByIdModuleAndTemps(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date, @PathVariable("heure") int heure, @PathVariable("minute") int minute, @PathVariable("seconde") int seconde) throws ParseException {
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            Timestamp temps = Fonction.getTimestamp(Fonction.makeDate(date),heure,minute,seconde);
            double toreturn = 0;
            for(int i=0; i<liste.size(); i++){
                Timestamp tempsData = new Timestamp(liste.get(i).getTemps().getTime() / 1000 * 1000);
                if(tempsData.equals(temps)){
                    toreturn = liste.get(i).getPuissance();
                }
            }
            return toreturn;
        }

        @GetMapping("/listePriseDataByDateAndIdModule/{date}/{idmodule}")
        public List<PriseData> listePriseDataByDateAndIdModule(@PathVariable("date") String date, @PathVariable("idmodule") Long idmodule) throws ParseException {
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get() ;
            List<PriseData> liste = priseDataRepository.findByModule(module);
            List<PriseData> toreturn = new ArrayList<>();
            for(int i=0; i<liste.size(); i++){
                LocalDate dataDate = Fonction.timeStampToLocalDate(liste.get(i).getTemps());
                if(dataDate.equals(Fonction.convertDateToLocalDate(Fonction.makeDate(date)))){
                    toreturn.add(liste.get(i));
                }
            }
            return toreturn;
        }

        @GetMapping("/getConsommationPriseByIdModuleAndDate/{idmodule}/{date}")
        public double getConsommationPriseByIdModuleAndDate(@PathVariable("idmodule") Long idmodule, @PathVariable("date") String date) throws ParseException {
            ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
            List<PriseData> liste = priseDataRepository.findByModule(module);
            List<PriseData> realliste = new ArrayList<>();
            for(int i=0; i<liste.size(); i++){
                LocalDate dataDate = Fonction.timeStampToLocalDate(liste.get(i).getTemps());
                if(Fonction.convertDateToLocalDate(Fonction.makeDate(date)).equals(dataDate)){
                    realliste.add(liste.get(i));
                }
            }
            double toreturn = 0;
            if(realliste.size()!=0){
                toreturn = realliste.get(realliste.size() - 1).getConsommation();
            }
            return toreturn;
        }

        @GetMapping("/getConsommationPriseIdMoisIdModule/{idmois}/{idmodule}")
        public double getConsommationPriseIdMoisIdModule(@PathVariable("idmois") Long idmois, @PathVariable("idmodule") Long idmodule){
            int annee = 2024;
            List<LocalDate> listedates = Fonction.getAllDatesInMonth(annee, Math.toIntExact(idmois));
            RestTemplate restTemplate = new RestTemplate();
            double totalConsommation = 0;

            for (LocalDate date : listedates) {
                String formattedDate = date.toString();
                String url = "https://javaserver-production.up.railway.app/api/solarprise/getConsommationPriseByIdModuleAndDate/" + idmodule + "/" + formattedDate;

                double duration = restTemplate.getForObject(url, Double.class);
                totalConsommation += duration;
            }
            return totalConsommation;
        }

        @GetMapping("/getConsommationPriseAnnuelleIdModule/{idmodule}")
        public double[] getConsommationPriseAnnuelleIdModule(@PathVariable("idmodule") Long idmodule){
            RestTemplate restTemplate = new RestTemplate();
            double[] toreturn = new double[12];
            for(int i=0; i<toreturn.length; i++){
                String url = "https://javaserver-production.up.railway.app/api/solarprise/getConsommationPriseIdMoisIdModule/"+ (i+1) +"/"+ idmodule;
                toreturn[i] = restTemplate.getForObject(url, Double.class);
            }
            return toreturn;
        }

        @GetMapping("/listeConsommationPriseMensuelleByIdModuleAndMonth/{idmodule}/{idmois}")
        public ConsommationPriseMensuelle listeConsommationPriseMensuelleByIdModuleAndMonth(@PathVariable("idmodule") Long idmodule, @PathVariable("idmois") Long idmois){
            int annee = 2024;
            RestTemplate restTemplate = new RestTemplate();
            List<LocalDate> listedates = Fonction.getAllDatesInMonth(annee, Math.toIntExact(idmois));
            Double[][] toreturn = new Double[listedates.size()][2];
            for(int i=0; i<listedates.size(); i++){
                String url = "https://javaserver-production.up.railway.app/api/solarprise/getConsommationPriseByIdModuleAndDate/"+ idmodule +"/"+ listedates.get(i).toString();
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
                semaine1.add(listedates.get(i));
                semaine1.add(toreturn[i][1]);
            }
            for(int i=7; i<14; i++){
                semaine2.add(listedates.get(i));
                semaine2.add(toreturn[i][1]);
            }
            for(int i=14; i<21; i++){
                semaine3.add(listedates.get(i));
                semaine3.add(toreturn[i][1]);
            }
            for(int i=21; i<28; i++){
                semaine4.add(listedates.get(i));
                semaine4.add(toreturn[i][1]);
            }
            if(listedates.size() == 29){
                semaine5.add(listedates.get(28));
                semaine5.add(toreturn[28][1]);
            }
            if(listedates.size() == 30){
                semaine5.add(listedates.get(28));
                semaine5.add(toreturn[28][1]);
                semaine5.add(listedates.get(29));
                semaine5.add(toreturn[29][1]);
            }
            if(listedates.size() == 31){
                semaine5.add(listedates.get(28));
                semaine5.add(toreturn[28][1]);
                semaine5.add(listedates.get(29));
                semaine5.add(toreturn[29][1]);
                semaine5.add(listedates.get(30));
                semaine5.add(toreturn[30][1]);
            }
            ConsommationPriseMensuelle consommationPriseMensuelle = new ConsommationPriseMensuelle();
            consommationPriseMensuelle.setIdmois(Math.toIntExact(idmois));
            consommationPriseMensuelle.setIdmodule(Math.toIntExact(idmodule));
            consommationPriseMensuelle.setSemaine1(semaine1);
            consommationPriseMensuelle.setSemaine2(semaine2);
            consommationPriseMensuelle.setSemaine3(semaine3);
            consommationPriseMensuelle.setSemaine4(semaine4);
            consommationPriseMensuelle.setSemaine5(semaine5);

            return consommationPriseMensuelle;
        }
    }