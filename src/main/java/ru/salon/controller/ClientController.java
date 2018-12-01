package ru.salon.controller;

import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Client;
import ru.salon.repository.ClientRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    private ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients/{clientId}")
    public Client getAnswersByQuestionId(@PathVariable Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id " + clientId));
    }
}
