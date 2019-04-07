package ru.salon.dto;

import lombok.Builder;
import lombok.Data;
import ru.salon.model.Expense;
import ru.salon.model.Master;

import java.util.Map;

@Data
@Builder
public class StatisticMaster {
    private String day;
    private String allSum;
    private String masterSum;
    private Master master;
    private Map<Expense, Integer> expenses;
}
