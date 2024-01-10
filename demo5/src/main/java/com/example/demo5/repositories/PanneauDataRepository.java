package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PanneauData;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PanneauDataRepository extends CrudRepository<PanneauData, Long> {
    List<PanneauData> findAll();
    List<PanneauData> findByModule(ModuleSolar module);
}
