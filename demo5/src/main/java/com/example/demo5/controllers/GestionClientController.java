package com.example.demo5.controllers;

import com.example.demo5.models.Client;
import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.UsefulEntity;
import com.example.demo5.repositories.ClientRepository;
import com.example.demo5.repositories.ModuleSolarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solarclient")
@CrossOrigin("*")

public class GestionClientController {
    private final ClientRepository clientRepository;
    private final ModuleSolarRepository moduleSolarRepository;

    @Autowired
    public GestionClientController(ClientRepository clientRepository, ModuleSolarRepository moduleSolarRepository){
        this.clientRepository = clientRepository;
        this.moduleSolarRepository = moduleSolarRepository;
    }

    @GetMapping("/clientbyid/{id}")
    public Client getClientById(@PathVariable("id") Long id) {
        return clientRepository.findById(id).get();
    }

    @GetMapping("/listeclient")
    public List<Client> getClients(){
        return clientRepository.findAll();
    }

    @GetMapping("/insertclient/{nom}/{prenom}/{email}/{pass}/{codepostal}/{lienimage}/{idmodule}")
    public void insertClient(@PathVariable("nom") String nom,
                             @PathVariable("prenom") String prenom,
                             @PathVariable("email") String email,
                             @PathVariable("pass") String pass,
                             @PathVariable("codepostal") String codepostal,
                             @PathVariable("lienimage") String lienimage,
                             @PathVariable("idmodule") Long idmodule){
        ModuleSolar module = moduleSolarRepository.findById(idmodule).get();
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setPass(pass);
        client.setCodepostal(codepostal);
        client.setLienimage(lienimage);
        client.setModule(module);
        clientRepository.save(client);
    }

    @GetMapping("/loginclient/{email}/{password}")
    public UsefulEntity loginclient(@PathVariable("email")String email, @PathVariable("password")String password){
        UsefulEntity usefulEntity = new UsefulEntity();
        usefulEntity.setState(false);
        List<Client> liste = clientRepository.findAll();
        for(int i=0; i<liste.size(); i++){
            if(liste.get(i).getEmail().equals(email)&&liste.get(i).getPass().equals(password)){
                usefulEntity.setState(true);
                usefulEntity.setIdmodule(liste.get(i).getModuleId().intValue());
                usefulEntity.setIdclient(liste.get(i).getId());
            }
        }
        return usefulEntity;
    }
}