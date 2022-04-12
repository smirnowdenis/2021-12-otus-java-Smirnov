package ru.otus.homework.crm.service.api;

import ru.otus.homework.crm.model.User;

import java.util.Optional;

public interface DBServiceUser {
    Optional<User> getAdmin(String login);
}
