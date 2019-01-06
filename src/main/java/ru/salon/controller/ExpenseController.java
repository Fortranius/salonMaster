package ru.salon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.Expense;
import ru.salon.repository.ExpenseRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    private ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/expenses")
    public Page<Expense> getExpenses(Pageable pageable) {
        return expenseRepository.findAll(pageable);
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
