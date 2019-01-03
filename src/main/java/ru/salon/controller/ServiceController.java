package ru.salon.controller;

import org.springframework.web.bind.annotation.*;
import ru.salon.model.Service;
import ru.salon.repository.ServiceRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceController {

    private ServiceRepository serviceRepository;

    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/services")
    public List<Service> getServices() {
        return serviceRepository.findAll();
    }

    @PostMapping("/service")
    public Service createService(@Valid @RequestBody Service service) {
        return serviceRepository.save(service);
    }

}
