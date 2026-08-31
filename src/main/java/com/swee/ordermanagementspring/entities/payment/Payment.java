package com.swee.ordermanagementspring.entities.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.swee.ordermanagementspring.entities.Order;
import com.swee.ordermanagementspring.entities.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardPayment.class, name = "CARD"),
        @JsonSubTypes.Type(value = PixPayment.class, name = "PIX"),
        @JsonSubTypes.Type(value = BoletoPayment.class, name = "BOLETO")
})
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected Double amount;
    @Enumerated(EnumType.STRING)
    protected PaymentStatus status;
    protected LocalDateTime paymentDate;
    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    protected Order order;

    public Payment(){

    }

    public Payment(Double amount, Order order) {
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public abstract boolean processPayment();

}
