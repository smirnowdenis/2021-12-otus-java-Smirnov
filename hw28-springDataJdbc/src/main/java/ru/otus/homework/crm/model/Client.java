package ru.otus.homework.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@Table("client")
public class Client implements Cloneable {
    @Id
    private Long id;

    @Nonnull
    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones = new LinkedHashSet<>();

    public Client() {
    }

    public Client(Long id, @Nonnull String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name,
                Optional.ofNullable(this.address).map(Address::clone).orElse(null),
                Optional.ofNullable(this.phones).map(Collection::stream)
                        .map(phoneStream -> phoneStream.map(Phone::clone)
                                .collect(Collectors.toSet()))
                        .orElse(null)
        );
    }

    public String getPhoneValues() {
        return phones.stream().map(Phone::getNumber).collect(Collectors.joining(","));
    }
}
