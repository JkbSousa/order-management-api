package com.swee.ordermanagementspring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swee.ordermanagementspring.entities.client.Client;
import com.swee.ordermanagementspring.entities.client.CorporateClient;
import com.swee.ordermanagementspring.entities.client.IndividualClient;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponseDTO {

    private Long id;
    private String type;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String cpf;
    private String cnpj;
    private String companyName;

    public ClientResponseDTO(Long id, String type, String name, String email, LocalDate birthDate,
                             String cpf, String cnpj, String companyName) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.companyName = companyName;
    }

    public static ClientResponseDTO from(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO(
                client.getId(),
                switch (client) {
                    case IndividualClient i -> "INDIVIDUAL";
                    case CorporateClient c -> "CORPORATE";
                    default -> "UNKNOWN";
                },
                client.getName(),
                client.getEmail(),
                client.getBirthDate(),
                null,
                null,
                null
        );

        if (client instanceof IndividualClient individual) {
            dto.cpf = individual.getCpf();
        }

        if (client instanceof CorporateClient corporate) {
            dto.cnpj = corporate.getCnpj();
            dto.companyName = corporate.getCompanyName();
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }
}