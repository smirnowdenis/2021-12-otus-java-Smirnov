package ru.otus.homework.dataprocessor;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final File file;

    private final Gson gson;

    public FileSerializer(String fileName) {
        file = new File(fileName);
        gson = new Gson();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(gson.toJson(data));
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
