package com.swee.ordermanagementspring.controllers;

import com.swee.ordermanagementspring.dto.ClientRequestDTO;
import com.swee.ordermanagementspring.dto.ClientResponseDTO;
import com.swee.ordermanagementspring.entities.Address;
import com.swee.ordermanagementspring.entities.client.Client;
import com.swee.ordermanagementspring.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;
    public ClientController (ClientService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClientResponseDTO> findAll() {
        return service.findAll().stream()
                .map(ClientResponseDTO::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ClientResponseDTO findById(@PathVariable Long id) {
        return ClientResponseDTO.from(service.findById(id));
    }

    @PostMapping("/{id}/address")
    public ClientResponseDTO updateAddress(
            @PathVariable Long id,
            @RequestBody Address address) {

        return ClientResponseDTO.from(service.updateAddress(id, address));
    }

    @PostMapping
    public ClientResponseDTO insert(@Valid @RequestBody ClientRequestDTO dto) {
        return ClientResponseDTO.from(service.insert(dto));
    }

    @PutMapping("/{id}")
    public ClientResponseDTO update(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO dto) {
        return ClientResponseDTO.from(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}