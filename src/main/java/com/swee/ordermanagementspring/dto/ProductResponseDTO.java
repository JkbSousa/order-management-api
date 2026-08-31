package com.swee.ordermanagementspring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swee.ordermanagementspring.entities.product.DigitalProduct;
import com.swee.ordermanagementspring.entities.product.PhysicalProduct;
import com.swee.ordermanagementspring.entities.product.Product;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private Long id;
    private String type;
    private String name;
    private Double price;
    private String description;
    private Double weight;
    private String downloadLink;

    public ProductResponseDTO(Long id, String type, String name, Double price, String description,
                              Double weight, String downloadLink) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.description = description;
        this.weight = weight;
        this.downloadLink = downloadLink;
    }

    public static ProductResponseDTO from(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO(
                product.getId(),
                switch (product) {
                    case PhysicalProduct p -> "PHYSICAL";
                    case DigitalProduct d -> "DIGITAL";
                    default -> "UNKNOWN";
                },
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                null,
                null
        );

        if (product instanceof PhysicalProduct physical) {
            dto.weight = physical.getWeight();
        }

        if (product instanceof DigitalProduct digital) {
            dto.downloadLink = digital.getDownloadLink();
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public Double getWeight() {
        return weight;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}