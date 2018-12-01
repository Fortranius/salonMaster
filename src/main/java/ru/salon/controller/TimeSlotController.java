package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.model.TimeSlot;
import ru.salon.repository.TimeSlotRepository;

@RestController
public class TimeSlotController {

    private TimeSlotRepository timeSlotRepository;

    public TimeSlotController(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @GetMapping("/timeSlots")
    public Page<TimeSlot> getTimeSlots(Pageable pageable) {
        return timeSlotRepository.findAll(pageable);
    }

}
