package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.model.Incoming;
import ru.salon.repository.IncomingRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class IncomingController {

    private IncomingRepository incomingRepository;

    @PostMapping("/incoming")
    public Incoming createIncoming(@Valid @RequestBody Incoming incoming) {
        return incomingRepository.save(incoming);
    }
}
