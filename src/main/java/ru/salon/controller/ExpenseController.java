package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.dto.ExpenseCriteria;
import ru.salon.model.Expense;
import ru.salon.service.ExpenseQueryService;
import ru.salon.service.ExpenseService;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ExpenseService expenseService;
    private ExpenseQueryService expenseQueryService;

    public ExpenseController(ExpenseService expenseService, ExpenseQueryService expenseQueryService) {
        this.expenseService = expenseService;
        this.expenseQueryService = expenseQueryService;
    }

    @GetMapping("/expenses")
    public Page<Expense> getExpensesBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                            @RequestParam(name = "masterId", required = false) Long masterId,
                                            @RequestParam(name = "productId", required = false) Long productId,
                                            Pageable pageable) {
        Instant startSlot = LocalDateTime.parse(start, formatter).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, formatter).atZone(ZoneId.of("+0")).toInstant();
        return expenseQueryService.findEntityByCriteria(ExpenseCriteria.builder()
                        .masterId(masterId)
                        .productId(productId)
                        .start(startSlot)
                        .end(endSlot).build(), pageable);
    }

    @PostMapping("/expense")
    public ResponseEntity<?> createExpense(@Valid @RequestBody Expense expense) {
        Expense newExpense = expenseService.save(expense);
        if (newExpense == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(newExpense, HttpStatus.OK);
    }

    @PutMapping("/expense")
    public ResponseEntity<?> updateExpense(@RequestBody Expense expense) {
        Expense newExpense = expenseService.update(expense);
        if (newExpense == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(newExpense, HttpStatus.OK);
    }
}
