package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class MasterController {

    private MasterRepository masterRepository;

    public MasterController(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    @GetMapping("/masters")
    public Page<Master> getMasters(Pageable pageable) {
        return masterRepository.findAll(pageable);
    }

    @PostMapping("/master")
    public Master createMaster(@Valid @RequestBody Master master) {
        return masterRepository.save(master);
    }

    @PutMapping(value = "/master", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Master updateMaster(@RequestBody Master master) {
        return masterRepository.save(master);
    }

    @DeleteMapping("/master/{masterId}")
    public ResponseEntity<?> deleteMAster(@PathVariable Long masterId) {
        return masterRepository.findById(masterId)
                .map(master -> {
                    masterRepository.delete(master);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Master not found with id " + masterId));
    }
}
