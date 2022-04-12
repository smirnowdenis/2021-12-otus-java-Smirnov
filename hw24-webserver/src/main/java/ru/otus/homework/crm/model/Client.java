package ru.otus.homework.crm.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "name")
    private String name;

    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Expose
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client")
    private List<Phone> phones;

    public Client() {
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        if (this.phones != null && !this.phones.isEmpty()) {
            this.phones.forEach(phone -> phone.setClient(this));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name,
                Optional.ofNullable(this.address).map(Address::clone).orElse(null),
                Optional.ofNullable(this.phones).map(Collection::stream)
                        .map(phoneStream -> phoneStream.map(Phone::clone)
                                .collect(Collectors.toList()))
                        .orElse(null)
        );
    }
}
