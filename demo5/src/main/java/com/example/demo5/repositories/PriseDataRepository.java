package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PriseData;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriseDataRepository extends CrudRepository<PriseData, Long> {
    List<PriseData> findAll();

    List<PriseData> findByModule(ModuleSolar module);
}
