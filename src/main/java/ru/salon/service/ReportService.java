package ru.salon.service;

import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import ru.salon.model.*;
import ru.salon.model.enumiration.StatusOrder;
import ru.salon.repository.AdditionalIncomeRepository;
import ru.salon.repository.ExpenseRepository;
import ru.salon.repository.MasterRepository;
import ru.salon.repository.TimeSlotRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.salon.utils.Utils.getMonth;

@Service
@AllArgsConstructor
public class ReportService {

    private MasterRepository masterRepository;
    private ExpenseRepository expenseRepository;
    private TimeSlotRepository timeSlotRepository;
    private AdditionalIncomeRepository additionalIncomeRepository;

    public JSONArray getStatisticBetweenDate(Instant start, Instant end, Long masterId) {
        Master master = masterRepository.findAll().get(0);
        return generateArray(start, end, master);
    }

    private JSONArray generateArray(Instant start, Instant end, Master master) {

        List<TimeSlot> timeSlots = timeSlotRepository
                .findByStartSlotBetweenAndMasterAndStatus(start, end, master, StatusOrder.DONE);
        List<AdditionalIncome> additionalIncomes = additionalIncomeRepository
                .findByDateBetweenAndMaster(start, end, master);
        List<Expense> expenses = expenseRepository.findByDateBetweenAndMaster(start, end, master);

        JSONArray list = new JSONArray();
        Instant currentDateStart = start;
        Instant currentDateEnd = start.plus(1, ChronoUnit.DAYS);
        while (currentDateStart.isBefore(end)) {
            JSONObject obj = new JSONObject();
            LocalDateTime date = LocalDateTime.ofInstant(currentDateStart, ZoneId.systemDefault());
            obj.put("day", date.getDayOfMonth() + " " + getMonth(date.getMonthValue()));
            obj.put("master" + master.getId(), generateAllSum(currentDateStart,
                    currentDateEnd,
                    master,
                    timeSlots,
                    additionalIncomes,
                    expenses));
            currentDateStart = currentDateStart.plus(1, ChronoUnit.DAYS);
            currentDateEnd = currentDateEnd.plus(1, ChronoUnit.DAYS);
            list.add(obj);
        }
        return list;
    }

    private JSONObject generateAllSum(Instant start, Instant end, Master master,
                                      List<TimeSlot> timeSlots,
                                      List<AdditionalIncome> additionalIncomes,
                                      List<Expense> expenses) {

        BigDecimal sumIncome = timeSlots.stream().filter(timeSlot ->
                timeSlot.getStartSlot().isAfter(start) && timeSlot.getStartSlot().isBefore(end))
                .map(TimeSlot::getAllPrice).collect(Collectors.toList())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumIncomeMaster = timeSlots.stream().filter(timeSlot ->
                timeSlot.getStartSlot().isAfter(start) && timeSlot.getStartSlot().isBefore(end))
                .map(TimeSlot::getMasterWorkPrice).collect(Collectors.toList())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(2), 2);

        sumIncomeMaster = additionalIncomes.stream().filter(additionalIncome ->
                additionalIncome.getDate().isAfter(start) && additionalIncome.getDate().isBefore(end))
                .map(AdditionalIncome::getSum).collect(Collectors.toList())
                .stream().reduce(sumIncomeMaster, BigDecimal::add);
        JSONObject masterObject = new JSONObject();
        masterObject.put("sumIncome" + master.getId(), sumIncome);
        masterObject.put("sumIncomeMaster" + master.getId(), sumIncomeMaster);

        Map<Product, Integer> sums = expenses.stream().filter(expense ->
                expense.getDate().isAfter(start) && expense.getDate().isBefore(end))
                .collect(Collectors.groupingBy(Expense::getProduct, Collectors.summingInt(Expense::getCountProduct)));

        sums.forEach((product, integer) -> masterObject.put("product" + master.getId() + product.getId(), integer));
        return masterObject;
    }
}
