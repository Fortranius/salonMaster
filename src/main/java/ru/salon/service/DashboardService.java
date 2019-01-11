package ru.salon.service;

import org.springframework.stereotype.Service;
import ru.salon.model.Master;
import ru.salon.model.MasterPerformance;
import ru.salon.model.TimeSlot;
import ru.salon.repository.ExpenseRepository;
import ru.salon.repository.MasterRepository;
import ru.salon.repository.TimeSlotRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private MasterRepository masterRepository;
    private TimeSlotRepository timeSlotRepository;
    private ExpenseRepository expenseRepository;

    public DashboardService(MasterRepository masterRepository,
                            TimeSlotRepository timeSlotRepository,
                            ExpenseRepository expenseRepository) {
        this.masterRepository = masterRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<MasterPerformance> getMastersIncomesAndCosts(Instant start, Instant end) {
        List<Master> masters = masterRepository.findAll();
        return masters.stream().map(master -> {
            BigDecimal cost = expenseRepository.findByMaster(master).stream().map(expense ->
                expense.getProduct().getPrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))
            ).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            List<TimeSlot> slots = timeSlotRepository.findBetweenStartSlotAndMaster(master, start, end);
            System.out.println(slots);
            BigDecimal sumIncome = timeSlotRepository.findBetweenStartSlotAndMaster(master, start, end)
                    .stream().map(TimeSlot::getPrice).collect(Collectors.toList())
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return new MasterPerformance(master, sumIncome, cost);
        }).collect(Collectors.toList());
    }

    public MasterPerformance getAllIncomesAndCosts() {
        BigDecimal cost = expenseRepository.findAll().stream().map(expense ->
                expense.getProduct().getPrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))
        ).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumIncome = timeSlotRepository.findAll().stream().map(TimeSlot::getPrice)
                .collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return new MasterPerformance(null, sumIncome, cost);
    }
}
