package ru.otus.homework.jdbc.mapper.impl;

import ru.otus.homework.jdbc.mapper.annotation.Id;
import ru.otus.homework.jdbc.mapper.api.EntityClassMetaData;
import ru.otus.homework.jdbc.mapper.exception.JdbcMapperException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;
    private final Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        Field[] declaredFields = entityClass.getDeclaredFields();
        List<Field> fieldsWithIdAnnotation = Arrays
                .stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());

        if (fieldsWithIdAnnotation.isEmpty()) {
            throw new JdbcMapperException(String.format("Cannot find field with mandatory @Id annotation in %s class", getName()));
        }

        if (fieldsWithIdAnnotation.size() != 1) {
            throw new JdbcMapperException(String.format("@Id annotation must be single in %s class", getName()));
        }

        try {
            constructor = entityClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new JdbcMapperException(String.format("Class %s doesn't have constructor without parameters", getName()));
        }

        idField = fieldsWithIdAnnotation.get(0);
        fieldsWithoutId = Arrays.stream(declaredFields)
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
        name = entityClass.getSimpleName().toLowerCase();
        allFields = new ArrayList<>(fieldsWithoutId);
        allFields.add(idField);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

}
