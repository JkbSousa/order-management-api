package com.swee.ordermanagementspring.repositories;

import com.swee.ordermanagementspring.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
