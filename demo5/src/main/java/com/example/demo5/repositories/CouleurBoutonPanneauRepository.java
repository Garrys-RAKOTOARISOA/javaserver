package com.example.demo5.repositories;

import com.example.demo5.models.CouleurBoutonPanneau;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

public interface CouleurBoutonPanneauRepository extends CrudRepository<CouleurBoutonPanneau, Long> {
    CouleurBoutonPanneau findByModule(ModuleSolar moduleSolar);
}