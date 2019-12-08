package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;
import ru.salon.repository.MasterRepository;
import ru.salon.repository.TimeSlotRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class MasterService {

    private MasterRepository masterRepository;

    private TimeSlotRepository timeSlotRepository;

    public List<Master> findAll() {
        return masterRepository.findAllByOrderByIdAsc();
    }

    public List<Master> getMastersByWorkDay(Instant day) {
        return findAll().stream().filter(master -> {
            List<TimeSlot> timeSlots = timeSlotRepository.findByStartSlotBetweenAndMaster(day, day.plus(1, ChronoUnit.DAYS), master);
            if (!CollectionUtils.isEmpty(timeSlots)) return true;
            List<LocalDate> workDays = master.getWorkDays().stream()
                    .map(date -> date.atZone(ZoneOffset.UTC).toLocalDate())
                    .collect(Collectors.toList());
            return workDays.contains(day.atZone(ZoneOffset.UTC).toLocalDate());
        }).collect(Collectors.toList());
    }

    public Master save(Master save) {
        if (!CollectionUtils.isEmpty(save.getWorkDays()))
            return masterRepository.save(save);

        LocalDate endDate = save.getStartDateWork()
                .atZone(ZoneOffset.UTC).toLocalDate()
                .plusMonths(7)
                .withDayOfMonth(1)
                .minusDays(1);

        List<LocalDate> dates = Stream.iterate(save.getStartDateWork().atZone(ZoneOffset.UTC).toLocalDate(),
                date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(save.getStartDateWork().atZone(ZoneOffset.UTC).toLocalDate(), endDate))
                .collect(Collectors.toList());

        List<Instant> workDays = dates.stream().filter(date -> {
            long current = (DAYS.between(save.getStartDateWork().atZone(ZoneOffset.UTC).toLocalDate(), date))
                    % (save.getWorkingDay().getWorkDay() + save.getWorkingDay().getDayOff());
            return current < save.getWorkingDay().getWorkDay();
        }).map(date -> date.atStartOfDay().atZone(ZoneOffset.UTC).toInstant())
                .collect(Collectors.toList());

        save.setWorkDays(workDays);
        return masterRepository.save(save);
    }
}
