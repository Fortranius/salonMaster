package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/allMasters")
    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }

    @GetMapping("/masters/name/{name}")
    public List<Master> getClientsByFIO(@PathVariable String name) {
        return masterRepository.findByNameContaining(name);
    }

    @PostMapping("/master")
    public Master createMaster(@Valid @RequestBody Master master) {
        return masterRepository.save(master);
    }

    @PutMapping("/master")
    public Master updateMaster(@RequestBody Master master) {
        return masterRepository.save(master);
    }

    @DeleteMapping("/master/{masterId}")
    public ResponseEntity<?> deleteMaster(@PathVariable Long masterId) {
        return masterRepository.findById(masterId)
                .map(master -> {
                    masterRepository.delete(master);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Master not found with id " + masterId));
    }
}
