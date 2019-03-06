package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.salon.model.Sale;
import ru.salon.service.SaleService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SaleController {

    private SaleService saleService;

    @GetMapping("/sales")
    public Page<Sale> getExpenses(Pageable pageable) {
        return saleService.findAll(pageable);
    }

    @PostMapping("/sale")
    public ResponseEntity<?> createSale(@Valid @RequestBody Sale sale) {
        Sale newSale = saleService.save(sale);
        if (newSale == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(newSale, HttpStatus.OK);
    }

    @PutMapping("/sale")
    public ResponseEntity<?> updateSale(@RequestBody Sale sale) {
        Sale newSale = saleService.update(sale);
        if (newSale == null) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(newSale, HttpStatus.OK);
    }
}
