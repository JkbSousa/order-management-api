package com.swee.ordermanagementspring.dto;

import com.swee.ordermanagementspring.entities.Order;
import com.swee.ordermanagementspring.entities.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private LocalDateTime moment;
    private OrderStatus status;
    private ClientResponseDTO client;
    private List<OrderItemResponseDTO> items;
    private PaymentResponseDTO payment;
    private AddressResponseDTO shippingAddress;
    private Double total;

    public OrderResponseDTO(Long id, LocalDateTime moment, OrderStatus status, ClientResponseDTO client,
                            List<OrderItemResponseDTO> items, PaymentResponseDTO payment,
                            AddressResponseDTO shippingAddress, Double total) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.items = items;
        this.payment = payment;
        this.shippingAddress = shippingAddress;
        this.total = total;
    }

    public static OrderResponseDTO from(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getMoment(),
                order.getStatus(),
                ClientResponseDTO.from(order.getClient()),
                order.getItems().stream().map(OrderItemResponseDTO::from).toList(),
                order.getPayment() != null ? PaymentResponseDTO.from(order.getPayment()) : null,
                AddressResponseDTO.from(order.getShippingAddress()),
                order.total()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public ClientResponseDTO getClient() {
        return client;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public PaymentResponseDTO getPayment() {
        return payment;
    }

    public AddressResponseDTO getShippingAddress() {
        return shippingAddress;
    }

    public Double getTotal() {
        return total;
    }
}