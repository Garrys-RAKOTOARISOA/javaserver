package com.example.demo5.controllers;

import com.example.demo5.models.ClassSuccess;
import com.example.demo5.models.Client;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.TypeBatterie;
import com.example.demo5.repositories.ClientRepository;
import com.example.demo5.repositories.ModuleSolarRepository;
import com.example.demo5.repositories.TypeBatterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ClassSuccess changertypebatterie(@PathVariable("idbatterie") Long idbatterie, @PathVariable("idclient") Long idclient){
        ClassSuccess toreturn = new ClassSuccess();
        Client client = clientRepository.findById(idclient).get();
        ModuleSolar moduleSolar = moduleSolarRepository.findById(client.getModuleId()).get();
        TypeBatterie typeBatterie = typeBatterieRepository.findById(idbatterie).get();
        moduleSolar.setTypeBatterie(typeBatterie);
        moduleSolarRepository.save(moduleSolar);
        toreturn.setMessage("Type Batterie changee");
        return toreturn;
    }

    @GetMapping("/listebatterie")
    public List<TypeBatterie> listebatterie(){
        return typeBatterieRepository.findAll();
    }
}
