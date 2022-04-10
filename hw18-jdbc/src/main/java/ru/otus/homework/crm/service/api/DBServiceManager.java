package ru.otus.homework.crm.service.api;

import ru.otus.homework.crm.model.Manager;

import java.util.List;
import java.util.Optional;

public interface DBServiceManager {
    Manager saveManager(Manager client);

    Optional<Manager> getManager(long no);

    List<Manager> findAll();
}
