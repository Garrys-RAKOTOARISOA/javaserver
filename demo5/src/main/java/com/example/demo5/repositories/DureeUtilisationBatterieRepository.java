package com.example.demo5.repositories;

import com.example.demo5.models.DureeUtilisationBatterie;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DureeUtilisationBatterieRepository extends CrudRepository<DureeUtilisationBatterie, Long> {
    List<DureeUtilisationBatterie> findByDateAndModule(Date date, ModuleSolar moduleSolar);
}