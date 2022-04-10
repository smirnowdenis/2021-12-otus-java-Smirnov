package ru.otus.homework.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone() {
    }

    @Override
    public Phone clone() {
        return new Phone(id, number);
    }
}
