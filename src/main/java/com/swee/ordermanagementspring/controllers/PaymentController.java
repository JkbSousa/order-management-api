package com.swee.ordermanagementspring.controllers;

import com.swee.ordermanagementspring.dto.PaymentRequestDTO;
import com.swee.ordermanagementspring.dto.PaymentResponseDTO;
import com.swee.ordermanagementspring.entities.payment.Payment;
import com.swee.ordermanagementspring.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        List<PaymentResponseDTO> list = paymentService.findAll().stream()
                .map(PaymentResponseDTO::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        return ResponseEntity.ok(PaymentResponseDTO.from(payment));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> insert(@Valid @RequestBody PaymentRequestDTO dto){
        Payment savedPayment = paymentService.insert(dto);
        return ResponseEntity.ok(PaymentResponseDTO.from(savedPayment));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponseDTO> process(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        Payment processedPayment = paymentService.processAndSave(payment);
        return ResponseEntity.ok(PaymentResponseDTO.from(processedPayment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PaymentRequestDTO dto) {
        Payment updated = paymentService.update(id, dto);
        return ResponseEntity.ok(PaymentResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}