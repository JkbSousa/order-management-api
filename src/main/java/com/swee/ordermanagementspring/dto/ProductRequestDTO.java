package com.swee.ordermanagementspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequestDTO {

    @NotNull(message = "Product type required.")
    private String type; // PHYSICAL ou DIGITAL

    @NotBlank(message = "Name required.")
    private String name;

    @NotNull(message = "Price required.")
    @Positive(message = "Price must be positive.")
    private Double price;

    private String description;

    // Campo do PhysicalProduct
    @PositiveOrZero(message = "Weight cannot be negative.")
    private Double weight;

    // Campo do DigitalProduct
    private String downloadLink;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}