package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.dto.ExpenseCriteria;
import ru.salon.model.Expense;
import ru.salon.service.ExpenseQueryService;
import ru.salon.service.ExpenseService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ExpenseController {

    private ExpenseService expenseService;
    private ExpenseQueryService expenseQueryService;

    @GetMapping("/expenses")
    public Page<Expense> getExpenses(@RequestParam(name = "masterId", required = false) Long masterId,
                                     @RequestParam(name = "productId", required = false) Long productId,
                                     Pageable pageable) {
        return expenseQueryService.findEntityByCriteria(ExpenseCriteria.builder().masterId(masterId).productId(productId).build(),
                pageable);
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
