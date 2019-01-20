package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Client;
import ru.salon.repository.ClientRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public Page<Client> getClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @GetMapping("/clients/name/{name}")
    public List<Client> getClientsByFIO(@PathVariable String name) {
        return clientRepository.findByNameContaining(name);
    }

    @GetMapping("/clients/phone/{phone}")
    public List<Client> getClientsByPhone(@PathVariable String phone) {
        return clientRepository.findByPhoneContaining(phone);
    }

    @GetMapping("/client/{clientId}")
    public Client getClient(@PathVariable Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id " + clientId));
    }

    @PostMapping("/client")
    public Client createClient(@Valid @RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("/client")
    public Client updateClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @DeleteMapping("/client/{clientId}")
    public ResponseEntity<?> deleteClint(@PathVariable Long clientId) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    clientRepository.delete(client);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + clientId));
    }
}
