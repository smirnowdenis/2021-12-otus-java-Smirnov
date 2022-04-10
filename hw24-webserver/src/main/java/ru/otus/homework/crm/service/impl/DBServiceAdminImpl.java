package ru.otus.homework.crm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.core.repository.DataTemplate;
import ru.otus.homework.core.sessionmanager.TransactionManager;
import ru.otus.homework.crm.model.Admin;
import ru.otus.homework.crm.service.api.DBServiceAdmin;

import java.util.Optional;

public class DBServiceAdminImpl implements DBServiceAdmin {
    private static final Logger log = LoggerFactory.getLogger(DBServiceAdminImpl.class);

    private final DataTemplate<Admin> adminDataTemplate;
    private final TransactionManager transactionManager;

    public DBServiceAdminImpl(DataTemplate<Admin> adminDataTemplate, TransactionManager transactionManager) {
        this.adminDataTemplate = adminDataTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<Admin> getAdmin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var admins = adminDataTemplate.findByEntityField(session, "login", login);
            Optional<Admin> adminOptional = !admins.isEmpty() ? Optional.of(admins.get(0)) : Optional.empty();
            log.info("admin: {}", adminOptional);
            return adminOptional;
        });
    }
}
