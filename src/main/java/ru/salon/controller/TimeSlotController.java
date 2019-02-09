package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.TimeSlot;
import ru.salon.repository.ClientRepository;
import ru.salon.repository.TimeSlotRepository;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TimeSlotController {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TimeSlotRepository timeSlotRepository;
    private ClientRepository clientRepository;

    public TimeSlotController(TimeSlotRepository timeSlotRepository,
                              ClientRepository clientRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/timeSlots")
    public Page<TimeSlot> getTimeSlots(Pageable pageable) {
        return timeSlotRepository.findAll(pageable);
    }

    @PostMapping("/timeSlot")
    public TimeSlot createTimeSlot(@Valid @RequestBody TimeSlot timeSlot) {
        timeSlot.setClient(clientRepository.save(timeSlot.getClient()));
        return timeSlotRepository.save(timeSlot);
    }

    @GetMapping("/timeSlotsByDate")
    public List<TimeSlot> getTimeSlotsByWeek(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end) {

        Instant startSlot = LocalDateTime.parse(start, formatter).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, formatter).atZone(ZoneId.of("+0")).toInstant();
        return timeSlotRepository.findByStartSlotBetween(startSlot, endSlot);
    }

    @GetMapping("/timeSlotsByClientId")
    public List<TimeSlot> getTimeSlotsByClientId(@RequestParam("clientId") Long clientId) {
        return timeSlotRepository.findByClientId(clientId);
    }
}
