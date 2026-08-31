package com.swee.ordermanagementspring.repositories;

import com.swee.ordermanagementspring.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
