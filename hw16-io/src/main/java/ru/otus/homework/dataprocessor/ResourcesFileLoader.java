package ru.otus.homework.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.homework.model.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    private final Gson gson;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        gson = new Gson();
    }

    @Override
    public List<Measurement> load() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream(fileName))))) {
            return gson.fromJson(reader, new TypeToken<List<Measurement>>() {
            }.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
