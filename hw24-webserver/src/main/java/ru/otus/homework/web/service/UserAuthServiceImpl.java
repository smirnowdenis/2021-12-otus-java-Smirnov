package ru.otus.homework.web.service;

import ru.otus.homework.crm.service.api.DBServiceUser;

public class UserAuthServiceImpl implements UserAuthService {
    private final DBServiceUser dbServiceUser;

    public UserAuthServiceImpl(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceUser.getAdmin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
