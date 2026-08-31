package com.swee.ordermanagementspring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swee.ordermanagementspring.entities.enums.PaymentStatus;
import com.swee.ordermanagementspring.entities.payment.BoletoPayment;
import com.swee.ordermanagementspring.entities.payment.CardPayment;
import com.swee.ordermanagementspring.entities.payment.Payment;
import com.swee.ordermanagementspring.entities.payment.PixPayment;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDTO {

    private Long id;
    private String type;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private Long orderId;
    private String cardNumberMasked;
    private String cardHolder;
    private Integer installments;

    private String pixKey;
    private String pixHolderName;
    private String pixKeyType;
    private String transactionId;

    private String barCode;
    private LocalDate dueDate;

    public PaymentResponseDTO(Long id, String type, Double amount, PaymentStatus status,
                              LocalDateTime paymentDate, Long orderId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.orderId = orderId;
    }

    public static PaymentResponseDTO from(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO(
                payment.getId(),
                switch (payment) {
                    case CardPayment c -> "CARD";
                    case PixPayment p -> "PIX";
                    case BoletoPayment b -> "BOLETO";
                    default -> "UNKNOWN";
                },
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentDate(),
                payment.getOrder() != null ? payment.getOrder().getId() : null
        );

        if (payment instanceof CardPayment card) {
            dto.cardNumberMasked = maskCardNumber(card.getCardNumber());
            dto.cardHolder = card.getCardHolder();
            dto.installments = card.getInstallments();
        }

        if (payment instanceof PixPayment pix) {
            dto.pixKey = pix.getPixKey();
            dto.pixHolderName = pix.getPixHolderName();
            dto.pixKeyType = pix.getPixKeyType() != null ? pix.getPixKeyType().name() : null;
            dto.transactionId = pix.getTransactionId();
        }

        if (payment instanceof BoletoPayment boleto) {
            dto.barCode = boleto.getBarCode();
            dto.dueDate = boleto.getDueDate();
        }

        return dto;
    }

    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return null;
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCardNumberMasked() {
        return cardNumberMasked;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public Integer getInstallments() {
        return installments;
    }

    public String getPixKey() {
        return pixKey;
    }

    public String getPixHolderName() {
        return pixHolderName;
    }

    public String getPixKeyType() {
        return pixKeyType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getBarCode() {
        return barCode;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}