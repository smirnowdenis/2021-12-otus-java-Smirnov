package ru.otus.homework.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage clone() {
        ObjectForMessage clone = new ObjectForMessage();
        clone.setData(new ArrayList<>(data));
        return clone;
    }
}
