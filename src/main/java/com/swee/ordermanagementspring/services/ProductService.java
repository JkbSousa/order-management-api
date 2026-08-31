package com.swee.ordermanagementspring.services;

import com.swee.ordermanagementspring.dto.ProductRequestDTO;
import com.swee.ordermanagementspring.entities.product.DigitalProduct;
import com.swee.ordermanagementspring.entities.product.PhysicalProduct;
import com.swee.ordermanagementspring.entities.product.Product;
import com.swee.ordermanagementspring.exceptions.ProductException;
import com.swee.ordermanagementspring.exceptions.ResourceNotFoundException;
import com.swee.ordermanagementspring.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    public ProductService (ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product insert(ProductRequestDTO dto) {
        Product product = buildProduct(dto);
        return repository.save(product);
    }

    public Product update(Long id, ProductRequestDTO dto) {
        Product existing = findById(id); //já lança ResourceNotFoundException se não existir

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setDescription(dto.getDescription());

        if (existing instanceof PhysicalProduct physical && dto.getWeight() != null) {
            physical.setWeight(dto.getWeight());
        }

        if (existing instanceof DigitalProduct digital && dto.getDownloadLink() != null) {
            digital.setDownloadLink(dto.getDownloadLink());
        }

        return repository.save(existing);
    }

    private Product buildProduct(ProductRequestDTO dto) {
        return switch (dto.getType().toUpperCase()) {
            case "PHYSICAL" -> {
                if (dto.getWeight() == null) {
                    throw new ProductException("weight is required for PHYSICAL products");
                }
                yield new PhysicalProduct(dto.getPrice(), dto.getName(), dto.getDescription(), dto.getWeight());
            }
            case "DIGITAL" -> {
                if (dto.getDownloadLink() == null || dto.getDownloadLink().isBlank()) {
                    throw new ProductException("downloadLink is required for DIGITAL products");
                }
                yield new DigitalProduct(dto.getPrice(), dto.getName(), dto.getDescription(), dto.getDownloadLink());
            }
            default -> throw new ProductException("Invalid product type: " + dto.getType());
        };
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}