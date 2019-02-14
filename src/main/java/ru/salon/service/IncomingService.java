package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.ProductBalance;
import ru.salon.repository.ExpenseRepository;
import ru.salon.repository.IncomingRepository;
import ru.salon.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IncomingService {

    private ProductRepository productRepository;
    private IncomingRepository incomingRepository;
    private ExpenseRepository expenseRepository;

    public List<ProductBalance> getAllProductsBalance() {
        return productRepository.findAll().stream().map(product -> {
            Long countExpense = expenseRepository.sumCountProduct(product.getId()).orElse(0L);
            Long countIncoming = incomingRepository.sumCountProduct(product.getId()).orElse(0L);

            int balance = countIncoming.intValue() - countExpense.intValue();

            return ProductBalance.builder().product(product)
                    .count(balance).build();
        }).collect(Collectors.toList());
    }
}
