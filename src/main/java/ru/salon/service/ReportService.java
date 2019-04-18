package ru.salon.service;

import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import ru.salon.dto.StaticData;
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

    public StaticData getStatisticBetweenDate(Instant start, Instant end, Long masterId) {
        return generateArray(start, end, masterRepository.findAll());
    }

    private StaticData generateArray(Instant start, Instant end, List<Master> masters) {
        JSONArray data = new JSONArray();
        JSONArray columns = new JSONArray();
        addColumn(columns, "day", "Дата", null, null);

        masters.forEach(master -> {
            List<Expense> expenses = expenseRepository.findByDateBetweenAndMaster(start, end, master);
            Map<Product, Integer> sums = expenses.stream().filter(expense ->
                    expense.getDate().isAfter(start) && expense.getDate().isBefore(end))
                    .collect(Collectors.groupingBy(Expense::getProduct, Collectors.summingInt(Expense::getCountProduct)));

            addColumn(columns, master.getId().toString(), master.getPerson().getName(), "3", "0");

            addColumn(columns, "master" + master.getId() + ".sumIncome" + master.getId(), "Услуги", null, "1");
            addColumn(columns, "master" + master.getId() + ".sumIncomeMaster" + master.getId(), "З/п", null, "1");

            sums.forEach((product, integer) -> {
                addColumn(columns, "master" + master.getId() + ".product" + master.getId() + product.getId(),
                        product.getDescription(), null, "1");
            });
        });

        Instant currentDateStart = start;
        Instant currentDateEnd = start.plus(1, ChronoUnit.DAYS);
        while (currentDateStart.isBefore(end)) {
            JSONObject obj = new JSONObject();
            LocalDateTime date = LocalDateTime.ofInstant(currentDateStart, ZoneId.systemDefault());
            obj.put("day", date.getDayOfMonth() + " " + getMonth(date.getMonthValue()));

            for (Master master:masters) {
                List<TimeSlot> timeSlots = timeSlotRepository
                        .findByStartSlotBetweenAndMasterAndStatus(start, end, master, StatusOrder.DONE);
                List<AdditionalIncome> additionalIncomes = additionalIncomeRepository
                        .findByDateBetweenAndMaster(start, end, master);
                List<Expense> expenses = expenseRepository.findByDateBetweenAndMaster(start, end, master);

                obj.put("master" + master.getId(), generateAllSum(currentDateStart,
                        currentDateEnd,
                        master,
                        timeSlots,
                        additionalIncomes,
                        expenses));
            }
            currentDateStart = currentDateStart.plus(1, ChronoUnit.DAYS);
            currentDateEnd = currentDateEnd.plus(1, ChronoUnit.DAYS);
            data.add(obj);
        }

        return StaticData.builder().columns(columns).data(data).build();
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

        sums.forEach((product, integer) -> {
            masterObject.put("product" + master.getId() + product.getId(), integer);
        });
        return masterObject;
    }

    private void addColumn(JSONArray columns, String dataField, String text, String colSpan, String row) {
        JSONObject jsonObject = new JSONObject();
        if (colSpan == null) jsonObject.put("dataField", dataField);
        jsonObject.put("text", text);
        if (colSpan != null) jsonObject.put("colSpan", colSpan);
        if (row != null) jsonObject.put("row", row);
        columns.add(jsonObject);
    }
}
