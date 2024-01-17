package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.ReferenceDureeBatterie;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReferenceDureeBatterieRepository extends CrudRepository<ReferenceDureeBatterie, Long> {
    List<ReferenceDureeBatterie> findByDateAndModule(Date date, ModuleSolar moduleSolar);
}
