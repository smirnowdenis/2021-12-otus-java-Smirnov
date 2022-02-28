package ru.otus.homework.listener;

import ru.otus.homework.model.Message;

public interface Listener {
    void onUpdated(Message msg);
}
