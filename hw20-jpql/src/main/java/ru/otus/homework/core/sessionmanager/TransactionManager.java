package ru.otus.homework.core.sessionmanager;

import ru.otus.homework.core.sessionmanager.TransactionAction;

public interface TransactionManager {
    <T> T doInTransaction(TransactionAction<T> action);
    <T> T doInReadOnlyTransaction(TransactionAction<T> action);
}
