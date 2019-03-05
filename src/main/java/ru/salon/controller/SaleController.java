package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.Sale;
import ru.salon.repository.SaleRepository;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SaleController {

    private SaleRepository saleRepository;

    @GetMapping("/sales")
    public Page<Sale> getExpenses(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    @PostMapping("/sale")
    public Sale createSale(@Valid @RequestBody Sale sale) {
        return saleRepository.save(sale);
    }

    @PutMapping("/sale")
    public Sale updateSale(@RequestBody Sale sale) {
        return saleRepository.save(sale);
    }
}
