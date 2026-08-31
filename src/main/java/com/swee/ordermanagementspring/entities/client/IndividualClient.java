package com.swee.ordermanagementspring.entities.client;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("INDIVIDUAL")
public class IndividualClient extends Client{

    @Column(unique = true)
    private String cpf;

    public IndividualClient() {

    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public IndividualClient(String name, String email, LocalDate birthDate, String cpf) {
        super(name, email, birthDate);
        this.cpf = cpf;


    }
}
