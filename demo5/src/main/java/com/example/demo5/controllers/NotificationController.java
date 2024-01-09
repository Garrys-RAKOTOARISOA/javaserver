package com.example.demo5.controllers;

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

    @GetMapping("/traitementnotification/{id}")
    public String traitementnotif(@PathVariable("id") Long id){
        NotificationModule notification = notificationModuleRepository.findById(id).get();
        notification.setSeen(true);
        notificationModuleRepository.save(notification);
        return "Notification traitee";
    }
}
