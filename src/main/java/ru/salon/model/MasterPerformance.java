package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MasterPerformance {

    public MasterPerformance(Master master,
                             BigDecimal income,
                             BigDecimal cost,
                             BigDecimal countOrders) {
        this.master = master;
        this.income = income;
        this.cost = cost;
        this.countOrders = countOrders;
    }

    private Master master;
    private BigDecimal income;
    private BigDecimal cost;
    private BigDecimal countOrders;
}
