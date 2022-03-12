package ru.otus.homework.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.homework.model.Measurement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {
    private final File file;

    private final Gson gson;

    public ResourcesFileLoader(String fileName) {
        try {
            URI uri = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI();
            file = new File(uri);
            gson = new Gson();
        } catch (URISyntaxException e) {
            throw new FileProcessException(e);
        }
    }

    @Override
    public List<Measurement> load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return gson.fromJson(reader, new TypeToken<List<Measurement>>(){}.getType());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
