package com.swee.ordermanagementspring.entities.payment;

import com.swee.ordermanagementspring.entities.Order;
import com.swee.ordermanagementspring.entities.enums.OrderStatus;
import com.swee.ordermanagementspring.entities.enums.PaymentStatus;
import com.swee.ordermanagementspring.exceptions.PaymentException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {

    private String cardNumber;
    private String cardHolder;
    private Integer installments;

    public CardPayment(){

    }

    public CardPayment(Double amount, Order order, String cardNumber, String cardHolder, Integer installments) {
        super(amount, order);
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.installments = installments;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    @Override
    public boolean processPayment() {
        if (cardNumber == null || cardNumber.isEmpty()){
            throw new PaymentException("Invalid number");
        }
        this.status = PaymentStatus.APPROVED;
        this.paymentDate = LocalDateTime.now();
        return true;
    }
}
