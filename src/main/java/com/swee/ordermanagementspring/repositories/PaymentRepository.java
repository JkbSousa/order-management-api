package com.swee.ordermanagementspring.repositories;

import com.swee.ordermanagementspring.entities.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
