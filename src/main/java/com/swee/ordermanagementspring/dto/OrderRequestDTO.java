package com.swee.ordermanagementspring.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequestDTO {

    private Long clientId; //cliente já existente

    @Valid
    private ClientRequestDTO client; //cliente novo

    @NotEmpty(message = "The order must contain at least one item.")
    @Valid
    private List<OrderItemRequestDTO> items;

    @NotNull(message = "Payment is required.")
    @Valid
    private OrderPaymentRequestDTO payment;

    @NotNull(message = "Shipping address is required.")
    @Valid
    private AddressRequestDTO shippingAddress;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public ClientRequestDTO getClient() {
        return client;
    }

    public void setClient(ClientRequestDTO client) {
        this.client = client;
    }

    public List<OrderItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }

    public OrderPaymentRequestDTO getPayment() {
        return payment;
    }

    public void setPayment(OrderPaymentRequestDTO payment) {
        this.payment = payment;
    }

    public AddressRequestDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressRequestDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}