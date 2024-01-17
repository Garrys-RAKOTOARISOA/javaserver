package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.ReferenceDureePrise;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface ReferenceDureePriseRepository extends CrudRepository<ReferenceDureePrise, Long> {
    List<ReferenceDureePrise> findByDateAndModule(Date date, ModuleSolar moduleSolar);
}
