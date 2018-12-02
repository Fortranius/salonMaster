package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Client;
import ru.salon.repository.ClientRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClientController {

    private ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public Page<Client> getMasters(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @GetMapping("/clients/{clientId}")
    public Client getClient(@PathVariable Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id " + clientId));
    }
}
