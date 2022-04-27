package ru.otus.homework.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.ClientService;

import java.util.List;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client")
    public List<Client> getAllClients() {
        return clientService.findAll();
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return clientService.findById(id).orElse(null);
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }
}
