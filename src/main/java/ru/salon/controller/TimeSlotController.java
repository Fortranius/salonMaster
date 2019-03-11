package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.TimeSlot;
import ru.salon.service.TimeSlotService;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static ru.salon.utils.Utils.FORMATTER;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TimeSlotController {

    private TimeSlotService timeSlotService;

    @GetMapping("/timeSlots")
    public Page<TimeSlot> getTimeSlots(Pageable pageable) {
        return timeSlotService.findAll(pageable);
    }

    @PostMapping("/timeSlot")
    public TimeSlot createTimeSlot(@Valid @RequestBody TimeSlot timeSlot) {
        return timeSlotService.save(timeSlot);
    }

    @GetMapping("/timeSlotsByDate")
    public List<TimeSlot> getTimeSlotsByWeek(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end) {

        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        return timeSlotService.findByStartSlotBetween(startSlot, endSlot);
    }

    @GetMapping("/timeSlotsByClientId")
    public List<TimeSlot> getTimeSlotsByClientId(@RequestParam("clientId") Long clientId) {
        return timeSlotService.findByClientId(clientId);
    }
}
