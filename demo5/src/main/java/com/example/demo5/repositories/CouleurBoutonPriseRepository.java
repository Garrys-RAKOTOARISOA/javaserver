package com.example.demo5.repositories;

import com.example.demo5.models.CouleurBoutonPrise;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

public interface CouleurBoutonPriseRepository extends CrudRepository<CouleurBoutonPrise, Long> {
    CouleurBoutonPrise findByModule(ModuleSolar moduleSolar);
}