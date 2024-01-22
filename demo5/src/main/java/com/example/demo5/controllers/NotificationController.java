package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ClassSuccess;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.NotificationModule;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.NotificationModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/solarnotification")
@CrossOrigin("*")
public class NotificationController {
    private final NotificationModuleRepository notificationModuleRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public NotificationController(NotificationModuleRepository notificationModuleRepository, ModuleSolarRepository moduleSolarRepository){
        this.notificationModuleRepository = notificationModuleRepository;
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/insertionnotification/{idmodule}/{texte}")
    public String nouvelnotification(@PathVariable("idmodule")Long idmodule, @PathVariable("texte")String texte){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        NotificationModule notification = new NotificationModule();
        notification.setModule(module);
        notification.setTexte(texte);
        notificationModuleRepository.save(notification);
        return "Notification inseree";
    }

    @GetMapping("/getnotification/{id}")
    public NotificationModule getnotif(@PathVariable("id")Long id){
        return notificationModuleRepository.findById(id).get();
    }

    @GetMapping("/traitementnotification/{id}")
    public ClassSuccess traitementnotif(@PathVariable("id") Long id){
        ClassSuccess toreturn = new ClassSuccess();
        NotificationModule notification = notificationModuleRepository.findById(id).get();
        notification.setSeen(true);
        notificationModuleRepository.save(notification);
        toreturn.setMessage("Notification traitee");
        return toreturn;
    }

    @GetMapping("/listenotificationbyidmodule/{idmodule}")
    public List<NotificationModule> getlistenotif(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        return notificationModuleRepository.findByModule(module);
    }

    @GetMapping("/listenotificationnontraiteebyidmodule/{idmodule}")
    public List<NotificationModule> listenotificationtraiteebyidmodule(@PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        List<NotificationModule> liste = notificationModuleRepository.findByModule(module);
        List<NotificationModule> toreturn = new ArrayList<>();
        for(int i=0; i<liste.size(); i++){
            if(!liste.get(i).getSeen()){
                toreturn.add(liste.get(i));
            }
        }
        return toreturn;
    }
}
