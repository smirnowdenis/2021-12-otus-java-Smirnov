package ru.otus.homework.crm.service.api;

import ru.otus.homework.crm.model.Admin;

import java.util.Optional;

public interface DBServiceAdmin {
    Optional<Admin> getAdmin(String login);
}
