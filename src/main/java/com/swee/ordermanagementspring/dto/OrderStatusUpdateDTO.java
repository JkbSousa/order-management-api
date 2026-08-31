package com.swee.ordermanagementspring.dto;

import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateDTO {

    @NotNull(message = "Status required.")
    private String status; //PENDING_PAYMENT, PROCESSING, PAID, SHIPPED, DELIVERED

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}