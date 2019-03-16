package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class MasterService {

    private MasterRepository masterRepository;

    public List<Master> getMastersByWorkDay(Instant day) {
        return masterRepository.findAll().stream().filter(master -> {
            long diff = DAYS.between(master.getStartDateWork(), day) + 1;
            if (diff % 7 == 0) return true;
            if (diff<=0) return false;
            long current = diff % (master.getWorkingDay().getWorkDay() + master.getWorkingDay().getDayOff());
            return current < master.getWorkingDay().getWorkDay() && current>0;
        }).collect(Collectors.toList());
    }
}
