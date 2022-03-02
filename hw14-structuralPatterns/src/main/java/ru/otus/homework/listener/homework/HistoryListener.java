package ru.otus.homework.listener.homework;

import ru.otus.homework.listener.Listener;
import ru.otus.homework.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    Map<Long, Message> messageHistoryByIdMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messageHistoryByIdMap.put(msg.getId(), msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageHistoryByIdMap.get(id));
    }
}
