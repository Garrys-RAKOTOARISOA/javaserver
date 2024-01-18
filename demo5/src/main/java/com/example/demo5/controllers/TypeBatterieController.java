package com.example.demo5.controllers;

import com.example.demo5.models.Client;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.TypeBatterie;
import com.example.demo5.repositories.ClientRepository;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.TypeBatterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solartypebatterie")
@CrossOrigin("*")
public class TypeBatterieController {
    private final ClientRepository clientRepository;
    private final ModuleSolarRepository moduleSolarRepository;
    private final TypeBatterieRepository typeBatterieRepository;
    @Autowired
    public TypeBatterieController(ClientRepository clientRepository, ModuleSolarRepository moduleSolarRepository, TypeBatterieRepository typeBatterieRepository){
        this.clientRepository = clientRepository;
        this.moduleSolarRepository = moduleSolarRepository;
        this.typeBatterieRepository = typeBatterieRepository;
    }

    @GetMapping("/changertypebatterie/{idbatterie}/{idclient}")
    public String changertypebatterie(@PathVariable("idbatterie") Long idbatterie, @PathVariable("idclient") Long idclient){
        Client client = clientRepository.findById(idclient).get();
        ModuleSolar moduleSolar = moduleSolarRepository.findByClient(client);
        TypeBatterie typeBatterie = typeBatterieRepository.findById(idbatterie).get();
        moduleSolar.setTypeBatterie(typeBatterie);
        moduleSolarRepository.save(moduleSolar);
        return "Type Batterie changee";
    }
}
