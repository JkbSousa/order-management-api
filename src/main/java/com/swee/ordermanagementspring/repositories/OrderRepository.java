package com.swee.ordermanagementspring.repositories;

import com.swee.ordermanagementspring.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
