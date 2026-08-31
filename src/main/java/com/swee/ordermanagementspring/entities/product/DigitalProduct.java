package com.swee.ordermanagementspring.entities.product;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DIGITAL")
public class DigitalProduct extends Product{
    private String downloadLink;

    public DigitalProduct() {

    }

    public DigitalProduct(Double price, String name, String description, String downloadLink) {
        super(price, name, description);
        this.downloadLink = downloadLink;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public Double calculateShippingValue() {
        return 0.0;
    }
}
