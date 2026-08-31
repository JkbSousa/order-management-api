package com.swee.ordermanagementspring.services;

import com.swee.ordermanagementspring.dto.*;
import com.swee.ordermanagementspring.entities.Address;
import com.swee.ordermanagementspring.entities.Order;
import com.swee.ordermanagementspring.entities.OrderItem;
import com.swee.ordermanagementspring.entities.client.Client;
import com.swee.ordermanagementspring.entities.client.CorporateClient;
import com.swee.ordermanagementspring.entities.client.IndividualClient;
import com.swee.ordermanagementspring.entities.enums.OrderStatus;
import com.swee.ordermanagementspring.entities.payment.BoletoPayment;
import com.swee.ordermanagementspring.entities.payment.CardPayment;
import com.swee.ordermanagementspring.entities.payment.Payment;
import com.swee.ordermanagementspring.entities.payment.PixPayment;
import com.swee.ordermanagementspring.entities.product.Product;
import com.swee.ordermanagementspring.exceptions.OrderException;
import com.swee.ordermanagementspring.exceptions.PaymentException;
import com.swee.ordermanagementspring.exceptions.ProductException;
import com.swee.ordermanagementspring.exceptions.ResourceNotFoundException;
import com.swee.ordermanagementspring.repositories.ClientRepository;
import com.swee.ordermanagementspring.repositories.OrderRepository;
import com.swee.ordermanagementspring.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public OrderService(OrderRepository repository, ProductRepository productRepository, ClientRepository clientRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Transactional
    public Order insert(OrderRequestDTO dto) {
        Client client = resolveClient(dto);

        Order order = new Order(OrderStatus.PENDING_PAYMENT, client);
        order.setShippingAddress(buildAddress(dto.getShippingAddress()));

        List<OrderItem> items = buildItems(order, dto.getItems());
        order.setItems(items);

        Payment payment = buildPayment(dto.getPayment(), order);
        order.setPayment(payment);

        return repository.save(order);
    }

    private Client resolveClient(OrderRequestDTO dto) {
        if (dto.getClientId() != null) {
            return clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client not found, id: " + dto.getClientId()));
        }

        if (dto.getClient() != null) {
            return buildNewClient(dto.getClient());
        }

        throw new OrderException("It is necessary to provide either clientId or client data.");
    }

    private Client buildNewClient(ClientRequestDTO dto) {
        return switch (dto.getType().toUpperCase()) {
            case "INDIVIDUAL" -> {
                if (dto.getCpf() == null || dto.getCpf().isBlank()) {
                    throw new OrderException("CPF is required for INDIVIDUAL clients.");
                }
                yield new IndividualClient(dto.getName(), dto.getEmail(), dto.getBirthDate(), dto.getCpf());
            }
            case "CORPORATE" -> {
                if (dto.getCnpj() == null || dto.getCnpj().isBlank()) {
                    throw new OrderException("CNPJ is required for CORPORATE clients.");
                }
                yield new CorporateClient(dto.getName(), dto.getEmail(), dto.getBirthDate(), dto.getCnpj(), dto.getCompanyName());
            }
            default -> throw new OrderException("Invalid client type: " + dto.getType());
        };
    }

    private Address buildAddress(AddressRequestDTO dto) {
        return new Address(dto.getZipCode(), dto.getState(), dto.getCity(),
                dto.getNeighborhood(), dto.getComplement(), dto.getNumber(), dto.getStreet());
    }

    private List<OrderItem> buildItems(Order order, List<OrderItemRequestDTO> itemsDto) {
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequestDTO itemDto : itemsDto) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found, id: " + itemDto.getProductId()));

            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice() * itemDto.getQuantity());
            items.add(item);
        }

        return items;
    }

    private Payment buildPayment(OrderPaymentRequestDTO dto, Order order) {
        return switch (dto.getType().toUpperCase()) {
            case "CARD" -> {
                if (dto.getCardNumber() == null || dto.getCardNumber().isBlank()) {
                    throw new PaymentException("cardNumber is required for CARD payments.");
                }
                yield new CardPayment(dto.getAmount(), order, dto.getCardNumber(), dto.getCardHolder(), dto.getInstallments());
            }
            case "PIX" -> {
                if (dto.getPixKey() == null || dto.getPixKey().isBlank()) {
                    throw new PaymentException("pixKey is required for PIX payments.");
                }
                yield new PixPayment(dto.getAmount(), order, dto.getPixKey(), dto.getPixHolderName(), null, null);
            }
            case "BOLETO" -> {
                if (dto.getBarCode() == null || dto.getBarCode().isBlank()) {
                    throw new PaymentException("barCode is required for BOLETO payments.");
                }
                yield new BoletoPayment(dto.getAmount(), order, dto.getBarCode(), dto.getDueDate());
            }
            default -> throw new PaymentException("Invalid payment type: " + dto.getType());
        };
    }

    public Order updateStatus(Long id, OrderStatusUpdateDTO dto) {
        Order order = findById(id);

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OrderException("Invalid status: " + dto.getStatus());
        }

        order.setStatus(newStatus);
        return repository.save(order);
    }

    public Order updateAddress(Long id, AddressRequestDTO dto) {
        Order order = findById(id);

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderException("The shipping address cannot be changed for an order that has already been shipped or delivered.");
        }

        Address address = order.getShippingAddress();
        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setComplement(dto.getComplement());
        address.setNeighborhood(dto.getNeighborhood());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());

        return repository.save(order);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}