package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PlanningBatterie;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PlanningBatterieRepository extends CrudRepository<PlanningBatterie, Long> {
    List<PlanningBatterie> findAll();

    boolean existsByModuleAndDatedebutLessThanEqualAndDatefinGreaterThanEqual(
            ModuleSolar module, Timestamp datefin, Timestamp datedebut);
    List<PlanningBatterie> findByModule(ModuleSolar moduleSolar);
}
