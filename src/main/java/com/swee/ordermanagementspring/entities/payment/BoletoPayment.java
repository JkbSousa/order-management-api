package com.swee.ordermanagementspring.entities.payment;

import com.swee.ordermanagementspring.entities.Order;
import com.swee.ordermanagementspring.entities.enums.OrderStatus;
import com.swee.ordermanagementspring.entities.enums.PaymentStatus;
import com.swee.ordermanagementspring.exceptions.PaymentException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("BOLETO")
public class BoletoPayment extends Payment{

    private String barCode;
    private LocalDate dueDate;

    public BoletoPayment(Double amount, Order order, String barCode, LocalDate dueDate) {
        super(amount, order);
        this.barCode = barCode;
        this.dueDate = dueDate;
        this.status = PaymentStatus.PENDING;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public boolean processPayment() {
        if (barCode == null || barCode.isEmpty()) {
            throw new PaymentException("Invalid barcode");
        }

        if (dueDate.isBefore(LocalDate.now())) {
            this.status = PaymentStatus.EXPIRED;
            return false;
        }
    this.status = PaymentStatus.APPROVED;
        this.paymentDate = LocalDateTime.now();
        return true;
    }
}
