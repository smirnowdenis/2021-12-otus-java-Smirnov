package ru.otus.homework.crm.service.api;

import ru.otus.homework.crm.model.Client;

import java.util.List;

public interface DBServiceClient {
    Client saveClient(Client client);

    Object getClient(long id);

    List<Client> findAll();
}
