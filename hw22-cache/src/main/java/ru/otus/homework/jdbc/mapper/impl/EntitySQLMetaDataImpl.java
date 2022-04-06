package ru.otus.homework.jdbc.mapper.impl;

import ru.otus.homework.jdbc.mapper.api.EntityClassMetaData;
import ru.otus.homework.jdbc.mapper.api.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    public static final String SELECT_QUERY_TEMPLATE = "select %s from %s where %s  = ?";
    public static final String INSERT_QUERY_TEMPLATE = "insert into %s(%s) values (%s)";
    public static final String UPDATE_QUERY_TEMPLATE = "update %s set %s where %s = ?";
    public static final String SELECT_ALL_QUERY_TEMPLATE = "select * from %s";

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String entityClassName = entityClassMetaData.getName();
        String idFieldName = entityClassMetaData.getIdField().getName();

        selectAllSql = String.format(EntitySQLMetaDataImpl.SELECT_ALL_QUERY_TEMPLATE, entityClassName);
        selectByIdSql = String.format(SELECT_QUERY_TEMPLATE,
                entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(",")),
                entityClassName,
                idFieldName
        );

        insertSql = String.format(INSERT_QUERY_TEMPLATE, entityClassName,
                fieldsWithoutId.stream().map(Field::getName).collect(Collectors.joining(",")),
                "?,".repeat(fieldsWithoutId.size() - 1) + "?"
        );

        updateSql = String.format(UPDATE_QUERY_TEMPLATE, entityClassName,
                fieldsWithoutId.stream().map(Field::getName).collect(Collectors.joining(" = ?, ")) + " = ?",
                idFieldName
        );
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
