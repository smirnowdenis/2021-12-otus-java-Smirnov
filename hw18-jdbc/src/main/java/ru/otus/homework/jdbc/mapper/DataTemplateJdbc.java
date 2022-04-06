package ru.otus.homework.jdbc.mapper;

import ru.otus.homework.crm.repository.DataTemplate;
import ru.otus.homework.crm.repository.DataTemplateException;
import ru.otus.homework.crm.repository.executor.DbExecutor;
import ru.otus.homework.jdbc.mapper.api.EntityClassMetaData;
import ru.otus.homework.jdbc.mapper.api.EntitySQLMetaData;
import ru.otus.homework.jdbc.mapper.impl.EntitySQLMetaDataImpl;
import ru.otus.homework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private final DbExecutor dbExecutor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return getEntityFromResultSet(rs);
                }
                return null;
            } catch (SQLException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                List<T> entities = new ArrayList<>();
                while (rs.next()) {
                    entities.add(getEntityFromResultSet(rs));
                }
                return entities;
            } catch (SQLException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            List<Object> entityFieldValues = ReflectionUtils.getEntityFieldValues(entityClassMetaData.getFieldsWithoutId(), entity);

            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), entityFieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            List<Object> entityFieldValues = ReflectionUtils.getEntityFieldValues(entityClassMetaData.getFieldsWithoutId(), entity);
            entityFieldValues.add(ReflectionUtils.getEntityFieldValue(entityClassMetaData.getIdField(), entity));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), entityFieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T getEntityFromResultSet(ResultSet rs) throws SQLException, IllegalAccessException {
        T entity = ReflectionUtils.createInstance(entityClassMetaData);
        setObjectFieldsFromResultSet(entity, rs);

        return entity;
    }

    private void setObjectFieldsFromResultSet(T entity, ResultSet rs) throws SQLException, IllegalAccessException {
        List<Field> allClassFields = entityClassMetaData.getAllFields();

        for (Field field : allClassFields) {
            ReflectionUtils.setFieldAccessibleIfRequired(field);
            field.set(entity, rs.getObject(field.getName()));
        }
    }
}
