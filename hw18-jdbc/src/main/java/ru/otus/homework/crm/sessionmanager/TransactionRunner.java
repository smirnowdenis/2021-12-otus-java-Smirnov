package ru.otus.homework.crm.sessionmanager;

public interface TransactionRunner {
    <T> T doInTransaction(TransactionAction<T> action);
}
