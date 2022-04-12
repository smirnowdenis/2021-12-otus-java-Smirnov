package ru.otus.homework.web.service;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
