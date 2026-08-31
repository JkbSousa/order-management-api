package com.swee.ordermanagementspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class PaymentRequestDTO {

    @NotNull(message = "Payment type required.")
    private String type; //CARD, PIX ou BOLETO

    @NotNull(message = "Value required.")
    @Positive(message = "Value must be positive.")
    private Double amount;

    @NotNull(message = "Order id required.")
    private Long orderId;

    // Campos do CardPayment
    private String cardNumber;
    private String cardHolder;
    private Integer installments;

    // Campos do PixPayment
    private String pixKey;
    private String pixHolderName;

    // Campos do BoletoPayment
    private String barCode;
    private LocalDate dueDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getPixKey() {
        return pixKey;
    }

    public void setPixKey(String pixKey) {
        this.pixKey = pixKey;
    }

    public String getPixHolderName() {
        return pixHolderName;
    }

    public void setPixHolderName(String pixHolderName) {
        this.pixHolderName = pixHolderName;
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

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}