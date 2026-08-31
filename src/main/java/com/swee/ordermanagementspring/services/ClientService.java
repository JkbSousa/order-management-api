package com.swee.ordermanagementspring.services;

import com.swee.ordermanagementspring.dto.ClientRequestDTO;
import com.swee.ordermanagementspring.entities.Address;
import com.swee.ordermanagementspring.entities.client.Client;
import com.swee.ordermanagementspring.entities.client.CorporateClient;
import com.swee.ordermanagementspring.entities.client.IndividualClient;
import com.swee.ordermanagementspring.exceptions.ProductException;
import com.swee.ordermanagementspring.exceptions.ResourceNotFoundException;
import com.swee.ordermanagementspring.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;
    public ClientService (ClientRepository repository){
        this.repository = repository;
    }

    public List<Client> findAll() {
        return repository.findAll();
    }

    public Client findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    public Client updateAddress(Long id, Address address) {
        Client client = findById(id);

        client.setAddress(address);

        return repository.save(client);
    }

    public Client update(Long id, ClientRequestDTO dto) {
        Client existing = findById(id);

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setBirthDate(dto.getBirthDate());

        if (existing instanceof IndividualClient individual && dto.getCpf() != null) {
            individual.setCpf(dto.getCpf());
        }

        if (existing instanceof CorporateClient corporate) {
            if (dto.getCnpj() != null) {
                corporate.setCnpj(dto.getCnpj());
            }
            if (dto.getCompanyName() != null) {
                corporate.setCompanyName(dto.getCompanyName());
            }
        }

        return repository.save(existing);
    }

    public Client insert(ClientRequestDTO dto) {
        Client client = buildClient(dto);
        return repository.save(client);
    }

    private Client buildClient(ClientRequestDTO dto) {
        return switch (dto.getType().toUpperCase()) {
            case "INDIVIDUAL" -> {
                if (dto.getCpf() == null || dto.getCpf().isBlank()) {
                    throw new ProductException("cpf é obrigatório para cliente INDIVIDUAL");
                }
                yield new IndividualClient(dto.getName(), dto.getEmail(), dto.getBirthDate(), dto.getCpf());
            }
            case "CORPORATE" -> {
                if (dto.getCnpj() == null || dto.getCnpj().isBlank()) {
                    throw new ProductException("cnpj é obrigatório para cliente CORPORATE");
                }
                yield new CorporateClient(dto.getName(), dto.getEmail(), dto.getBirthDate(), dto.getCnpj(), dto.getCompanyName());
            }
            default -> throw new ProductException("Tipo de cliente inválido: " + dto.getType());
        };
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}