package com.example.demo5.repositories;

import com.example.demo5.models.CouleurBoutonBatterie;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

public interface CouleurBoutonBatterieRepository extends CrudRepository<CouleurBoutonBatterie, Long> {
    CouleurBoutonBatterie findByModule(ModuleSolar moduleSolar);
}
