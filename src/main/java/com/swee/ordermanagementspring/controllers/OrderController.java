package com.swee.ordermanagementspring.controllers;

import com.swee.ordermanagementspring.dto.AddressRequestDTO;
import com.swee.ordermanagementspring.dto.OrderRequestDTO;
import com.swee.ordermanagementspring.dto.OrderResponseDTO;
import com.swee.ordermanagementspring.dto.OrderStatusUpdateDTO;
import com.swee.ordermanagementspring.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderResponseDTO> findAll() {
        return service.findAll().stream()
                .map(OrderResponseDTO::from)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO findById(@PathVariable Long id) {
        return OrderResponseDTO.from(service.findById(id));
    }

    @PostMapping
    public OrderResponseDTO insert(@Valid @RequestBody OrderRequestDTO dto) {
        return OrderResponseDTO.from(service.insert(dto));
    }

    @PutMapping("/{id}/status")
    public OrderResponseDTO updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateDTO dto) {
        return OrderResponseDTO.from(service.updateStatus(id, dto));
    }

    @PutMapping("/{id}/address")
    public OrderResponseDTO updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO dto) {
        return OrderResponseDTO.from(service.updateAddress(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}