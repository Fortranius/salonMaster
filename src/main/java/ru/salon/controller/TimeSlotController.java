package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.TimeSlot;
import ru.salon.repository.TimeSlotRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class TimeSlotController {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TimeSlotRepository timeSlotRepository;

    public TimeSlotController(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @GetMapping("/timeSlots")
    public Page<TimeSlot> getTimeSlots(Pageable pageable) {
        return timeSlotRepository.findAll(pageable);
    }

    @PostMapping("/timeSlot")
    public TimeSlot createTimeSlot(@Valid @RequestBody TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    @GetMapping("/timeSlotsByDate")
    public Page<TimeSlot> getTimeSlotsByWeek(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end, Pageable pageable) {
        return timeSlotRepository.findByStartAndEndDate(
                LocalDateTime.parse(start, formatter).atZone(ZoneId.systemDefault()).toInstant(),
                LocalDateTime.parse(end, formatter).atZone(ZoneId.systemDefault()).toInstant(),
                pageable);
    }

}
