package com.example.demo5.repositories;

import com.example.demo5.models.ModuleSolar;
import com.example.demo5.models.NotificationModule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationModuleRepository extends CrudRepository<NotificationModule, Long> {
    List<NotificationModule> findByModule(ModuleSolar module);
}