package com.example.demo5.repositories;

import com.example.demo5.models.Client;
import com.example.demo5.models.TypeBatterie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TypeBatterieRepository extends CrudRepository<TypeBatterie, Long> {
    List<TypeBatterie> findAll();
}
