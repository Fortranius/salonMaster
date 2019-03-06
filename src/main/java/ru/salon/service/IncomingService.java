package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.Product;
import ru.salon.model.ProductBalance;
import ru.salon.repository.ExpenseRepository;
import ru.salon.repository.IncomingRepository;
import ru.salon.repository.ProductRepository;
import ru.salon.repository.SaleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IncomingService {

    private ProductRepository productRepository;
    private IncomingRepository incomingRepository;
    private ExpenseRepository expenseRepository;
    private SaleRepository saleRepository;

    public List<ProductBalance> getAllProductsBalance() {
        return productRepository.findAll().stream().map(product ->
            generateProductBalanceFromProduct(product)
        ).collect(Collectors.toList());
    }

    public ProductBalance getProductBalance(Product product) {
        return generateProductBalanceFromProduct(product);
    }

    private ProductBalance generateProductBalanceFromProduct(Product product) {
        Long countExpense = expenseRepository.sumCountProduct(product.getId()).orElse(0L);
        Long countSale = saleRepository.sumCountProduct(product.getId()).orElse(0L);
        Long countIncoming = incomingRepository.sumCountProduct(product.getId()).orElse(0L);

        int balance = countIncoming.intValue() - countExpense.intValue() - countSale.intValue();

        return ProductBalance.builder().product(product)
                .count(balance).build();
    }

}
