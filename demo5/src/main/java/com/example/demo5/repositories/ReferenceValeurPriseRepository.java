package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.ReferenceValeurPrise;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReferenceValeurPriseRepository extends CrudRepository<ReferenceValeurPrise, Long> {
    List<ReferenceValeurPrise> findByDateAndModule(Date date, ModuleSolar moduleSolar);
}