package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.TimeSlot;
import ru.salon.repository.TimeSlotRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TimeSlotController {

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
    public Page<TimeSlot> getTimeSlotsByWeek(@RequestParam("start") String start, @RequestParam("end") String end, Pageable pageable) {
        System.out.println("111111111");
        System.out.println(start);
        return timeSlotRepository.findAll(pageable);
    }

}
