package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.RelaisPanneau;
import org.springframework.data.repository.CrudRepository;

public interface RelaisPanneauRepository extends CrudRepository<RelaisPanneau, Long> {
    RelaisPanneau findByModule(ModuleSolar moduleSolar);
}
