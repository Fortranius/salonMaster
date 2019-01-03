package ru.salon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.exception.ResourceNotFoundException;
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

    @GetMapping("/masters/description/{description}")
    public List<Service> getClientsByFIO(@PathVariable String description) {
        return serviceRepository.findByDescription(description);
    }

    @PostMapping("/service")
    public Service createService(@Valid @RequestBody Service service) {
        return serviceRepository.save(service);
    }

    @PutMapping("/service")
    public Service updateService(@Valid @RequestBody Service service) {
        return serviceRepository.save(service);
    }

    @DeleteMapping("/service/{serviceId}")
    public ResponseEntity<?> deleteServiceId(@PathVariable Long serviceId) {
        return serviceRepository.findById(serviceId)
                .map(master -> {
                    serviceRepository.delete(master);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + serviceId));
    }
}
