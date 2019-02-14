package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.model.ProductBalance;
import ru.salon.service.IncomingService;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProductBalanceController {

    private IncomingService incomingService;

    @GetMapping("/getAllProductsBalance")
    public List<ProductBalance> getAllProductsBalance() {
        return incomingService.getAllProductsBalance();
    }
}
