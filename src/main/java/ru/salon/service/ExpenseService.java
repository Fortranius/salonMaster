package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.Expense;
import ru.salon.model.ProductBalance;
import ru.salon.repository.ExpenseRepository;

@Service
@AllArgsConstructor
public class ExpenseService {

    private IncomingService incomingService;
    private ExpenseRepository expenseRepository;

    public Expense save(Expense expense) {
        ProductBalance productBalance = incomingService.getProductBalance(expense.getProduct());
        if (productBalance.getCount() >= expense.getCountProduct()) return expenseRepository.save(expense);
        else return null;
    }

    public Expense update(Expense expenseToUpdate) {
        ProductBalance productBalance = incomingService.getProductBalance(expenseToUpdate.getProduct());
        Expense expense = expenseRepository.findById(expenseToUpdate.getId()).orElse(expenseToUpdate);

        int count = productBalance.getCount() + expense.getCountProduct();
        if (count >= expenseToUpdate.getCountProduct()) return expenseRepository.save(expenseToUpdate);
        else return null;
    }
}
