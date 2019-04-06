package ru.salon.dto;

import lombok.Builder;
import lombok.Data;
import ru.salon.model.Master;

@Data
@Builder
public class StatisticMaster {
    private String day;
    private String allSum;
    private String masterSum;
    private Master master;
}
