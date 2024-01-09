package com.example.demo5.repositories;

import com.example.demo5.models.BatterieData;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BatterieDataRepository extends CrudRepository<BatterieData, Long> {
//    List<BatterieData> findAll();
    List<BatterieData> findByModule(ModuleSolar module);
}
