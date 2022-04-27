package ru.otus.homework.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("phone")
public class Phone implements Cloneable {
    @Id
    private Long id;

    @Nonnull
    private String number;

    @Nonnull
    private String clientId;

    public Phone() {
    }

    public Phone(Long id, @Nonnull String number, @Nonnull String clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nonnull
    public String getNumber() {
        return number;
    }

    public void setNumber(@Nonnull String number) {
        this.number = number;
    }

    @Nonnull
    public String getClientId() {
        return clientId;
    }

    public void setClientId(@Nonnull String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Phone clone() {
        return new Phone(id, number, clientId);
    }
}
