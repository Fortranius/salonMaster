package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;
import ru.salon.repository.MasterRepository;
import ru.salon.repository.TimeSlotRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class MasterService {

    private MasterRepository masterRepository;

    private TimeSlotRepository timeSlotRepository;

    public List<Master> getMastersByWorkDay(Instant day) {
        return masterRepository.findAll().stream().filter(master -> {
            List<TimeSlot> timeSlots = timeSlotRepository.findByStartSlotBetweenAndMaster(day, day.plus(1, ChronoUnit.DAYS), master);
            if (!CollectionUtils.isEmpty(timeSlots)) return true;
            long diff = DAYS.between(master.getStartDateWork(), day) + 1;
            if (diff<=0) return false;
            long current = diff % (master.getWorkingDay().getWorkDay() + master.getWorkingDay().getDayOff());
            return current < master.getWorkingDay().getWorkDay() && current>=0;
        }).collect(Collectors.toList());
    }
}
