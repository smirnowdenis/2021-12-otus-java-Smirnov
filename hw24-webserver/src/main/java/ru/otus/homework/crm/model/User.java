package ru.otus.homework.crm.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User implements Cloneable {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "login")
    private String login;

    @Expose
    @Column(name = "password")
    private String password;

    @Expose
    @Column(name = "role")
    private String role;

    public User() {
    }

    public User(Long id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public User clone() {
        return new User(this.id, this.login, this.password, this.role);
    }
}
