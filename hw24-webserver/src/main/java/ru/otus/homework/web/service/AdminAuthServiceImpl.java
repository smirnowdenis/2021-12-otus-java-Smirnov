package ru.otus.homework.web.service;

import ru.otus.homework.crm.service.api.DBServiceAdmin;

public class AdminAuthServiceImpl implements AdminAuthService {
    private final DBServiceAdmin dbServiceAdmin;

    public AdminAuthServiceImpl(DBServiceAdmin dbServiceAdmin) {
        this.dbServiceAdmin = dbServiceAdmin;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceAdmin.getAdmin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
