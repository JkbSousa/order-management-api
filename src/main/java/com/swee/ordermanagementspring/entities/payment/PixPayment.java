package com.swee.ordermanagementspring.entities.payment;

import com.swee.ordermanagementspring.entities.Order;
import com.swee.ordermanagementspring.entities.enums.OrderStatus;
import com.swee.ordermanagementspring.entities.enums.PaymentStatus;
import com.swee.ordermanagementspring.entities.enums.PixKeyType;
import com.swee.ordermanagementspring.exceptions.PaymentException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("PIX")
public class PixPayment extends Payment{

    private String pixKey;
    private String pixHolderName;
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private PixKeyType pixKeyType;

    public PixPayment(){

    }

    public PixPayment(Double amount, Order order, String pixKey, String pixHolderName, String transactionId, PixKeyType pixKeyType) {
        super(amount, order);
        this.pixKey = pixKey;
        this.pixHolderName = pixHolderName;
        this.transactionId = transactionId;
        this.pixKeyType = pixKeyType;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PixKeyType getPixKeyType() {
        return pixKeyType;
    }

    public void setPixKeyType(PixKeyType pixKeyType) {
        this.pixKeyType = pixKeyType;
    }

    @Override
    public boolean processPayment() {
        if (pixKey == null || pixKey.isEmpty()) {
            throw new PaymentException("Invalid pix key.");
        }

        if (pixKeyType == null) {
            if (pixKey.contains("@")) {
                pixKeyType = PixKeyType.EMAIL;
            } else if (pixKey.matches("\\d{11}")) {
                pixKeyType = PixKeyType.CPF;
            } else if (pixKey.matches("\\d{10,11}")) {
                pixKeyType = PixKeyType.PHONE;
            } else {
                pixKeyType = PixKeyType.RANDOM;
            }
        }

        this.transactionId = "PIX-" + System.currentTimeMillis();
        this.status = PaymentStatus.APPROVED;
        this.paymentDate = LocalDateTime.now();
        return true;

    }
}
