package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.salon.dto.ExpenseCriteria;
import ru.salon.model.Expense;
import ru.salon.repository.ExpenseRepository;
import ru.salon.service.ExpenseQueryService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ExpenseController {

    private ExpenseRepository expenseRepository;
    private ExpenseQueryService expenseQueryService;

    @GetMapping("/expenses")
    public Page<Expense> getExpenses(@RequestParam(name = "masterId", required = false) Long masterId,
                                     @RequestParam(name = "productId", required = false) Long productId,
                                     Pageable pageable) {
        return expenseQueryService.findEntityByCriteria(ExpenseCriteria.builder().masterId(masterId).productId(productId).build(),
                pageable);
    }

    @PostMapping("/expense")
    public Expense createExpense(@Valid @RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @PutMapping("/expense")
    public Expense updateExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }
}
