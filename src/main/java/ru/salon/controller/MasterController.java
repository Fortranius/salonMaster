package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.exception.ResourceNotFoundException;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;
import ru.salon.service.MasterService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static ru.salon.utils.Utils.FORMATTER;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MasterController {

    private MasterRepository masterRepository;
    private MasterService masterService;

    @GetMapping("/masters")
    public Page<Master> getMasters(Pageable pageable) {
        return masterRepository.findAll(pageable);
    }

    @GetMapping("/allMasters")
    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }

    @GetMapping("/allMastersByWorkDay")
    public List<Master> getAllMastersByWorkDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String date) {
        return masterService.getMastersByWorkDay(LocalDateTime.parse(date, FORMATTER).atZone(ZoneId.of("+0")).toInstant());
    }

    @GetMapping("/allMastersByDayOff")
    public List<Master> getAllMastersByDayOff(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String date) {
        List<Master> mastersWorkDay = masterService.getMastersByWorkDay(LocalDateTime.parse(date, FORMATTER).atZone(ZoneId.of("+0")).toInstant());
        return masterRepository.findAll().stream()
                .filter(master -> !mastersWorkDay.contains(master))
                .collect(Collectors.toList());
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
