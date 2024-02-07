package com.example.demo5.repositories;

import com.example.demo5.models.Client;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAll();
    List<Client> findByModule(ModuleSolar moduleSolar);

    @Query("select c from Client c where c.module.Id = ?1")
    Optional<Client> findByModuleId(Long Id);

//    Optional<Client> findByModule(ModuleSolar moduleId);
}
