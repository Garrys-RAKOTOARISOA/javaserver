package com.example.demo5.repositories;

import com.example.demo5.models.Client;
import com.example.demo5.models.ModuleSolar;
import org.springframework.data.repository.CrudRepository;

public interface ModuleSolarRepository extends CrudRepository<ModuleSolar, Long> {
    ModuleSolar findByClient(Client client);
}
