package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MasterPerformance {

    public MasterPerformance(Master master, BigDecimal income, BigDecimal cost) {
        this.master = master;
        this.income = income;
        this.cost = cost;
    }

    private Master master;
    private BigDecimal income;
    private BigDecimal cost;
}
