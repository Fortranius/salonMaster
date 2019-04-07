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
        Master master = masterRepository.findAll().get(0);
        return generateArray(start, end, master);
    }

    private StaticData generateArray(Instant start, Instant end, Master master) {

        List<TimeSlot> timeSlots = timeSlotRepository
                .findByStartSlotBetweenAndMasterAndStatus(start, end, master, StatusOrder.DONE);
        List<AdditionalIncome> additionalIncomes = additionalIncomeRepository
                .findByDateBetweenAndMaster(start, end, master);
        List<Expense> expenses = expenseRepository.findByDateBetweenAndMaster(start, end, master);

        JSONArray data = new JSONArray();
        JSONArray columns = new JSONArray();
        addColumn(columns, "day", "Дата");

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
                    expenses,
                    columns));
            currentDateStart = currentDateStart.plus(1, ChronoUnit.DAYS);
            currentDateEnd = currentDateEnd.plus(1, ChronoUnit.DAYS);
            data.add(obj);
        }
        return StaticData.builder().columns(columns).data(data).build();
    }

    private JSONObject generateAllSum(Instant start, Instant end, Master master,
                                      List<TimeSlot> timeSlots,
                                      List<AdditionalIncome> additionalIncomes,
                                      List<Expense> expenses,
                                      JSONArray columns) {

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

        addColumn(columns, "master" + master.getId() + ".sumIncome" + master.getId(), "Услуги");
        addColumn(columns, "master" + master.getId() + ".sumIncomeMaster" + master.getId(), "З/п");

        Map<Product, Integer> sums = expenses.stream().filter(expense ->
                expense.getDate().isAfter(start) && expense.getDate().isBefore(end))
                .collect(Collectors.groupingBy(Expense::getProduct, Collectors.summingInt(Expense::getCountProduct)));

        sums.forEach((product, integer) -> {
            masterObject.put("product" + master.getId() + product.getId(), integer);
            addColumn(columns, "master" + master.getId() + ".product" + master.getId() + product.getId(), product.getDescription());
        });
        return masterObject;
    }

    private void addColumn(JSONArray columns, String dataField, String text) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dataField", dataField);
        jsonObject.put("text", text);
        columns.add(jsonObject);
    }
}
