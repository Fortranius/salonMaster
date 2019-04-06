package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.dto.StatisticMaster;
import ru.salon.repository.MasterRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {

    private MasterRepository masterRepository;

    public List<StatisticMaster> getStatisticBetweenDate(Instant start, Instant end, Long masterId) {
        return Collections.singletonList(StatisticMaster.builder()
                .day("1 апреля")
                .allSum(BigDecimal.valueOf(200).toString())
                .masterSum("400")
                .master(masterId !=null ? masterRepository.findById(masterId).orElse(null): null).build());
    }
}
