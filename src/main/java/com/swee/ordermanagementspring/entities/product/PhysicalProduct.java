package com.swee.ordermanagementspring.entities.product;

import com.swee.ordermanagementspring.exceptions.ProductException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PHYSICAL")
public class PhysicalProduct extends Product{
    private Double weight;

    public PhysicalProduct() {
    }

    public PhysicalProduct(Double price, String name, String description, Double weight) {
        super(price, name, description);
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public Double calculateShippingValue() {
        double shippingValue = 20.0;
        if (weight < 0.0) {
            throw new ProductException("Invalid weight.");
        }
        if (weight > 2.0) {
            return shippingValue += (weight - 2.0) * 8.0;
        }
        return shippingValue;
    }
}
