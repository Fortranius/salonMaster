package ru.salon.controller;

import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/masters")
    public Master createMaster(@Valid @RequestBody Master master) {
        return masterRepository.save(master);
    }

    @DeleteMapping("/masters/{questionId}")
    public ResponseEntity<?> deleteMAster(@PathVariable Long questionId) {
        return masterRepository.findById(questionId)
                .map(master -> {
                    masterRepository.delete(master);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Master not found with id " + questionId));
    }
}
