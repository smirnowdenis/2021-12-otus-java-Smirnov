package ru.otus.homework.util;

import ru.otus.homework.jdbc.mapper.api.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T createInstance(EntityClassMetaData<T> entityClassMetaData) {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        return wrapException(constructor::newInstance);
    }

    public static <T> List<Object> getEntityFieldValues(List<Field> fields, T entity) {
        return wrapException(() -> {
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : fields) {
                fieldValues.add(getEntityFieldValue(field, entity));
            }
            return fieldValues;
        });
    }

    public static <T> Object getEntityFieldValue(Field field, T entity) {
        return wrapException(() -> {
            setFieldAccessibleIfRequired(field);
            return field.get(entity);
        });
    }

    public static void setFieldAccessibleIfRequired(Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers)) {
            field.setAccessible(true);
        }
    }

    private static <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
