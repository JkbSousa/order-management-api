package com.swee.ordermanagementspring.repositories;

import com.swee.ordermanagementspring.entities.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
