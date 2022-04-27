package ru.otus.homework.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.repository.ClientRepository;
import ru.otus.homework.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public ClientServiceImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> findById(Long id) {
        return transactionManager.doInTransaction(() -> {
            var clientOptional = clientRepository.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>();
        clientRepository.findAll().forEach(clientList::add);
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
