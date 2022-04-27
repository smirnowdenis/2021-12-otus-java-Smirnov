package ru.otus.homework.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("address")
public class Address implements Cloneable {
    @Id
    private Long id;

    @Nonnull
    private String street;

    @Nonnull
    private String clientId;

    public Address() {
    }

    public Address(Long id, @Nonnull String street, @Nonnull String clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nonnull
    public String getStreet() {
        return street;
    }

    public void setStreet(@Nonnull String street) {
        this.street = street;
    }

    @Nonnull
    public String getClientId() {
        return clientId;
    }

    public void setClientId(@Nonnull String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Address clone() {
        return new Address(id, street, clientId);
    }
}
