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
            BigDecimal cost = expenseRepository.findByDateBetweenAndMaster(start, end, master).stream().map(expense ->
                expense.getProduct().getPurchasePrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))
            ).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal sumIncome = timeSlotRepository.findByStartSlotBetweenAndMaster(start, end, master)
                    .stream().map(TimeSlot::getAllPrice).collect(Collectors.toList())
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            int countOrders = timeSlotRepository.countByStartSlotBetweenAndMaster(start, end, master);
            return new MasterPerformance(master, sumIncome, cost, BigDecimal.valueOf(countOrders));
        }).collect(Collectors.toList());
    }

    public MasterPerformance getAllIncomesAndCosts(Instant start, Instant end) {
        BigDecimal cost = expenseRepository.findByDateBetween(start, end).stream().map(expense ->
                expense.getProduct().getPurchasePrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))
        ).collect(Collectors.toList()).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sumIncome = timeSlotRepository.findByStartSlotBetween(start, end)
                .stream().map(TimeSlot::getAllPrice).collect(Collectors.toList())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        long countOrders = timeSlotRepository.countByStartSlotBetween(start, end);
        return new MasterPerformance(null, sumIncome, cost, BigDecimal.valueOf(countOrders));
    }
}
