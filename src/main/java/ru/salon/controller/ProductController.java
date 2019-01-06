package ru.salon.controller;

import org.springframework.web.bind.annotation.*;
import ru.salon.model.Product;
import ru.salon.repository.ProductRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/description/{description}")
    public List<Product> getClientsByFIO(@PathVariable String description) {
        return productRepository.findByDescription(description);
    }

    @PostMapping("/product")
    public Product createProduct(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/product")
    public Product updateProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}
