package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.RelaisPrise;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelaisPriseRepository extends CrudRepository<RelaisPrise, Long> {
    List<RelaisPrise> findAll();
    RelaisPrise findByModule(ModuleSolar moduleSolar);
}