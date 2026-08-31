package com.swee.ordermanagementspring.services;

import com.swee.ordermanagementspring.dto.AddressRequestDTO;
import com.swee.ordermanagementspring.entities.Address;
import com.swee.ordermanagementspring.exceptions.ResourceNotFoundException;
import com.swee.ordermanagementspring.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(Long id){
        return addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found, id: " + id));
    }

    public Address insert(AddressRequestDTO dto) {
        Address address = new Address(dto.getZipCode(), dto.getState(), dto.getCity(),
                dto.getNeighborhood(), dto.getComplement(), dto.getNumber(), dto.getStreet());
        return addressRepository.save(address);
    }

    public Address update(Long id, AddressRequestDTO dto) {
        Address existing = findById(id);

        existing.setStreet(dto.getStreet());
        existing.setNumber(dto.getNumber());
        existing.setComplement(dto.getComplement());
        existing.setNeighborhood(dto.getNeighborhood());
        existing.setCity(dto.getCity());
        existing.setState(dto.getState());
        existing.setZipCode(dto.getZipCode());

        return addressRepository.save(existing);
    }

    public void delete (Long id) {
        addressRepository.deleteById(id);
    }
}