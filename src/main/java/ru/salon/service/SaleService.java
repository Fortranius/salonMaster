package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.salon.model.ProductBalance;
import ru.salon.model.Sale;
import ru.salon.repository.SaleRepository;

@Service
@AllArgsConstructor
public class SaleService {

    private IncomingService incomingService;
    private SaleRepository saleRepository;

    public Sale save(Sale sale) {
        ProductBalance productBalance = incomingService.getProductBalance(sale.getProduct());
        if (productBalance.getCount() >= sale.getCountProduct()) return saleRepository.save(sale);
        else return null;
    }

    public Sale update(Sale saleToUpdate) {
        ProductBalance productBalance = incomingService.getProductBalance(saleToUpdate.getProduct());
        Sale sale = saleRepository.findById(saleToUpdate.getId()).orElse(saleToUpdate);

        int count = productBalance.getCount() + sale.getCountProduct();
        if (count >= saleToUpdate.getCountProduct()) return saleRepository.save(saleToUpdate);
        else return null;
    }

    public Page<Sale> findAll(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }
}
