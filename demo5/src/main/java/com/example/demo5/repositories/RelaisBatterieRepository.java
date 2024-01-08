package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.RelaisBatterie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelaisBatterieRepository extends CrudRepository<RelaisBatterie, Long> {
    List<RelaisBatterie> findAll();

    RelaisBatterie findByModule(ModuleSolar moduleSolar);
}
