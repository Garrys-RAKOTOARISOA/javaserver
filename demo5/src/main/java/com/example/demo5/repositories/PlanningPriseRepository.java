package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.PlanningPrise;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PlanningPriseRepository extends CrudRepository<PlanningPrise, Long> {
    List<PlanningPrise> findAll();

    boolean existsByModuleAndDatedebutLessThanEqualAndDatefinGreaterThanEqual(
            ModuleSolar module, Timestamp datefin, Timestamp datedebut);

    List<PlanningPrise> findByModule(ModuleSolar moduleSolar);

    List<PlanningPrise> findAllByOrderByDatedebutAsc();
}
