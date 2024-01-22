package com.example.demo5.controllers;

import com.example.demo5.models.ClassSuccess;
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
    public ClassSuccess redirectionwifi(@PathVariable("idmodule") Long idmodule, @PathVariable("ssid") String ssid, @PathVariable("pass") String pass){
        ClassSuccess toreturn = new ClassSuccess();
        ModuleSolar moduleSolar = moduleSolarRepository.findById(idmodule).get();
        moduleSolar.setSsid(ssid);
        moduleSolar.setPass(pass);
        moduleSolarRepository.save(moduleSolar);
        toreturn.setMessage("\"Le wifi a ete redirigee vers \"+ssid");
        return toreturn;
    }
}