package ru.otus.homework.crm.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client")
    private List<ru.otus.homework.crm.model.Phone> phones;


    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<ru.otus.homework.crm.model.Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        if (this.phones != null && !this.phones.isEmpty()) {
            this.phones.forEach(phone -> phone.setClient(this));
        }
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name,
                Optional.ofNullable(this.address).map(Address::clone).orElse(null),
                Optional.ofNullable(this.phones).map(Collection::stream)
                        .map(phoneStream -> phoneStream.map(ru.otus.homework.crm.model.Phone::clone)
                                .collect(Collectors.toList()))
                        .orElse(null)
        );
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

    public List<ru.otus.homework.crm.model.Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<ru.otus.homework.crm.model.Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
