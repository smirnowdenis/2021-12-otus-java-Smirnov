package ru.otus.homework.crm.service.impl;

import com.sun.jdi.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.cache.HwCache;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.repository.DataTemplate;
import ru.otus.homework.crm.service.api.DBServiceClient;
import ru.otus.homework.crm.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> clientHwCache;

    public DbServiceClientImpl(TransactionRunner transactionRunner,
                               DataTemplate<Client> dataTemplate,
                               HwCache<String, Client> clientHwCache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.clientHwCache = clientHwCache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                putToCache(createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            putToCache(client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client clientFromCache = clientHwCache.get(String.valueOf(id));
        if (clientFromCache != null) {
            return Optional.of(clientFromCache);
        }

        Optional<Client> clientFromDb = transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });

        if (clientFromDb.isPresent()) {
            Client client = clientFromDb.get();
            putToCache(client);
        }

        return clientFromDb;
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    private void putToCache(Client client) {
        clientHwCache.put(String.valueOf(client.getId()), client);
    }
}
