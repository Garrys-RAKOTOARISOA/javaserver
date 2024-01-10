package com.example.demo5.controllers;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.repositories.ModuleSolarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modulesolar")
@CrossOrigin("*")
public class ModuleSolarController {
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public ModuleSolarController(ModuleSolarRepository moduleSolarRepository){
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/GetModuleById/{idmodule}")
    public ModuleSolar getmodule(@PathVariable("idmodule") Long idmodule){
        return moduleSolarRepository.findById(idmodule).get();
    }

    @GetMapping("/RedirigerWifi/{idmodule}/{ssid}/{pass}")
    public String redirectionwifi(@PathVariable("idmodule") Long idmodule, @PathVariable("ssid") String ssid, @PathVariable("pass") String pass){
        ModuleSolar moduleSolar = moduleSolarRepository.findById(idmodule).get();
        moduleSolar.setSsid(ssid);
        moduleSolar.setPass(pass);
        moduleSolarRepository.save(moduleSolar);
        return "Le wifi a ete redirigee vers "+ssid;
    }
}