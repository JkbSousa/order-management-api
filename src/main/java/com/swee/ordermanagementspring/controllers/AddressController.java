package com.swee.ordermanagementspring.controllers;

import com.swee.ordermanagementspring.dto.AddressRequestDTO;
import com.swee.ordermanagementspring.dto.AddressResponseDTO;
import com.swee.ordermanagementspring.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> findAll() {
        List<AddressResponseDTO> list = addressService.findAll().stream()
                .map(AddressResponseDTO::from)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(AddressResponseDTO.from(addressService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> insert(@Valid @RequestBody AddressRequestDTO dto) {
        return ResponseEntity.ok(AddressResponseDTO.from(addressService.insert(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AddressRequestDTO dto) {
        return ResponseEntity.ok(AddressResponseDTO.from(addressService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}