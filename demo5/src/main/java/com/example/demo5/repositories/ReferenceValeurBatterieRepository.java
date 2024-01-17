package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.ReferenceValeurBatterie;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReferenceValeurBatterieRepository extends CrudRepository<ReferenceValeurBatterie, Long> {
    List<ReferenceValeurBatterie> findByDateAndModule(Date date, ModuleSolar moduleSolar);
}
