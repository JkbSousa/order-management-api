package com.swee.ordermanagementspring.controllers;

import com.swee.ordermanagementspring.dto.ProductRequestDTO;
import com.swee.ordermanagementspring.dto.ProductResponseDTO;
import com.swee.ordermanagementspring.entities.product.Product;
import com.swee.ordermanagementspring.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController (ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDTO> findAll() {
        return service.findAll().stream()
                .map(ProductResponseDTO::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(@PathVariable Long id) {
        return ProductResponseDTO.from(service.findById(id));
    }

    @PostMapping
    public ProductResponseDTO insert(@Valid @RequestBody ProductRequestDTO dto) {
        return ProductResponseDTO.from(service.insert(dto));
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return ProductResponseDTO.from(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}