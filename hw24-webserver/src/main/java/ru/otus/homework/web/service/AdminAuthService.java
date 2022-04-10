package ru.otus.homework.web.service;

public interface AdminAuthService {
    boolean authenticate(String login, String password);
}
