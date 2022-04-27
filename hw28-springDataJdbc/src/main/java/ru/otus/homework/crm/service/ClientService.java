package ru.otus.homework.crm.service;

import ru.otus.homework.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client saveClient(Client client);

    Optional<Client> findById(Long id);

    List<Client> findAll();
}
