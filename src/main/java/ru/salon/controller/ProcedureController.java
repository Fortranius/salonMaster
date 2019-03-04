package ru.salon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Procedure;
import ru.salon.repository.ProcedureRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProcedureController {

    private ProcedureRepository procedureRepository;

    public ProcedureController(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @GetMapping("/procedures")
    public List<Procedure> getProcedures() {
        return procedureRepository.findAll();
    }

    @GetMapping("/procedures/description/{description}")
    public List<Procedure> getProceduresByDescription(@PathVariable String description) {
        return procedureRepository.findByDescription(description);
    }

    @PostMapping("/procedure")
    public Procedure createService(@Valid @RequestBody Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    @PutMapping("/procedure")
    public Procedure updateService(@Valid @RequestBody Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    @DeleteMapping("/procedure/{procedureId}")
    public ResponseEntity<?> deleteServiceId(@PathVariable Long procedureId) {
        return procedureRepository.findById(procedureId)
                .map(master -> {
                    procedureRepository.delete(master);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Procedure not found with id " + procedureId));
    }
}
