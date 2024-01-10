package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.NotificationModule;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.NotificationModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        notification.setTemps(Fonction.getCurrentTimestamp());
        notificationModuleRepository.save(notification);
        return "Notification inseree";
    }

    @GetMapping("/getnotification/{id}")
    public NotificationModule getnotif(@PathVariable("id")Long id){
        return notificationModuleRepository.findById(id).get();
    }

    @GetMapping("/traitementnotification/{id}")
    public String traitementnotif(@PathVariable("id") Long id){
        NotificationModule notification = notificationModuleRepository.findById(id).get();
        notification.setSeen(true);
        notificationModuleRepository.save(notification);
        return "Notification traitee";
    }
}
