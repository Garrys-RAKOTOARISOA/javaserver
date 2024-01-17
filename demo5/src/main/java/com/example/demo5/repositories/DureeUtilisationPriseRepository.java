package com.example.demo5.repositories;

import com.example.demo5.models.DureeUtilisationPrise;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DureeUtilisationPriseRepository extends CrudRepository<DureeUtilisationPrise, Long> {
    List<DureeUtilisationPrise> findByDateAndModule(Date date, ModuleSolar moduleSolar);
}
