package ru.otus.homework.crm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.sessionmanager.TransactionManager;
import ru.otus.homework.crm.model.User;
import ru.otus.homework.crm.service.api.DBServiceUser;

import java.util.Optional;

public class DBServiceUserImpl implements DBServiceUser {
    private static final Logger log = LoggerFactory.getLogger(DBServiceUserImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public DBServiceUserImpl(DataTemplate<User> userDataTemplate, TransactionManager transactionManager) {
        this.userDataTemplate = userDataTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<User> getAdmin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var admin = userDataTemplate.findAdminByLogin(session, login);
            Optional<User> adminOptional = !admin.isEmpty() ? Optional.of(admin.get(0)) : Optional.empty();
            log.info("admin: {}", adminOptional);
            return adminOptional;
        });
    }
}
