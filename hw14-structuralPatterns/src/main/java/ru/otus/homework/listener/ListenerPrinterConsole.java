package ru.otus.homework.listener;


import ru.otus.homework.model.Message;

public class ListenerPrinterConsole implements Listener {
    @Override
    public void onUpdated(Message msg) {
        var logString = String.format("oldMsg:%s", msg);
        System.out.println(logString);
    }
}
