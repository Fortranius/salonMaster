package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.salon.model.*;
import ru.salon.repository.ClientRepository;
import ru.salon.repository.ExpenseRepository;
import ru.salon.repository.ProductRepository;
import ru.salon.repository.TimeSlotRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeSlotService {

    private TimeSlotRepository timeSlotRepository;
    private ClientRepository clientRepository;
    private IncomingService incomingService;
    private ProductRepository productRepository;
    private ExpenseRepository expenseRepository;

    public Page<TimeSlot> findAll(Pageable pageable) {
        return timeSlotRepository.findAll(pageable);
    }

    public List<TimeSlot> findByStartSlotBetween(Instant start, Instant end) {
        return timeSlotRepository.findByStartSlotBetween(start, end);
    }

    public List<TimeSlot> findByClientId(Long clientId) {
        return timeSlotRepository.findByClientId(clientId);
    }

    public void delete(Long id) {
        expenseRepository.delete(expenseRepository.findByTimeSlotId(id));
        timeSlotRepository.deleteById(id);
    }

    public TimeSlot save(TimeSlot timeSlot) {
        if (timeSlot.getHair() == null) {
            return timeSlotRepository.save(timeSlot);
        }
        Product product = productRepository.findByHair(timeSlot.getHair());
        Expense expense = Expense.builder()
                .countProduct(timeSlot.getHairWeight().intValue())
                .date(timeSlot.getStartSlot())
                .master(timeSlot.getMaster())
                .product(product).build();
        ProductBalance productBalance = incomingService.getProductBalance(product);
        if (timeSlot.getId() != null) {
            TimeSlot old = timeSlotRepository.findById(timeSlot.getId()).orElse(timeSlot);
            timeSlot.setChanges(old.getChanges());
            Expense oldExpense = expenseRepository.findByTimeSlotId(timeSlot.getId());

            int count = productBalance.getCount() + oldExpense.getCountProduct();
            if (count < expense.getCountProduct()) return null;

            expense.setId(oldExpense.getId());
            checkChanges(timeSlot, old);
        } else {
            if (productBalance.getCount() < timeSlot.getHairWeight().intValue()) return null;
            timeSlot.setChanges(Collections.singletonList(TimeSlotChange.builder().date(Instant.now()).change("Создана новая заявка").build()));
        }
        timeSlot.setClient(clientRepository.save(timeSlot.getClient()));
        TimeSlot saveTimeSlot = timeSlotRepository.save(timeSlot);
        expense.setTimeSlot(saveTimeSlot);
        expenseRepository.save(expense);
        return saveTimeSlot;
    }

    private void checkChanges(TimeSlot timeSlot, TimeSlot old) {
        if (!old.getStartSlot().equals(timeSlot.getStartSlot()))
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Изменено время начала процедуры с " + generateDiffDate(timeSlot.getStartSlot(), old.getStartSlot())).build());
        if (!old.getEndSlot().equals(timeSlot.getEndSlot()))
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Изменено время завершения процедуры с " + generateDiffDate(timeSlot.getEndSlot(), old.getEndSlot())).build());
        if (!old.getMaster().getId().equals(timeSlot.getMaster().getId()))
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Изменен мастер с " + old.getMaster().getPerson().getName() + " на " + timeSlot.getMaster().getPerson().getName()).build());
        if (old.getAllPrice().compareTo(timeSlot.getAllPrice()) != 0)
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Изменена общая сумма процедуры с " + old.getAllPrice().toString() + " на " + timeSlot.getAllPrice().toString()).build());
        if (old.getAllPrice().compareTo(timeSlot.getAllPrice()) != 0)
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Изменена сумма услуги мастера с " + old.getMasterWorkPrice().toString() + " на " + timeSlot.getMasterWorkPrice().toString()).build());

        checkProcedures(timeSlot, old);
    }

    private String generateDiffDate(Instant slot, Instant old) {
        LocalDateTime dateOld = LocalDateTime.ofInstant(old, ZoneId.systemDefault());
        LocalDateTime dateNew = LocalDateTime.ofInstant(slot, ZoneId.systemDefault());
        return patternTime(dateOld.getHour()) + ":" + patternTime(dateOld.getMinute()) + " на " + patternTime(dateNew.getHour()) + ":" + patternTime(dateNew.getMinute());
    }

    private String patternTime(int time) {
        if (time<10) return "0" + time;
        else return "" + time;
    }

    private void checkProcedures(TimeSlot timeSlot, TimeSlot old) {
        timeSlot.getProcedures().stream()
                .filter(procedure -> !old.getProcedures().contains(procedure))
                .collect(Collectors.toList()).forEach(procedure -> {
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Добавлена новая услуга: " + procedure.getDescription()).build());
        });

        old.getProcedures().stream()
                .filter(procedure -> !timeSlot.getProcedures().contains(procedure))
                .collect(Collectors.toList()).forEach(procedure -> {
            timeSlot.getChanges().add(TimeSlotChange.builder().date(Instant.now())
                    .change("Удалена услуга: " + procedure.getDescription()).build());
        });
    }
}
