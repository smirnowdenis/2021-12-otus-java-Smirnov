package ru.otus.homework;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.cache.HwListener;
import ru.otus.homework.cache.MyCache;
import ru.otus.homework.crm.datasource.DriverManagerDataSource;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.repository.executor.DbExecutorImpl;
import ru.otus.homework.crm.service.impl.DbServiceClientImpl;
import ru.otus.homework.crm.sessionmanager.TransactionRunnerJdbc;
import ru.otus.homework.jdbc.mapper.DataTemplateJdbc;
import ru.otus.homework.jdbc.mapper.api.EntityClassMetaData;
import ru.otus.homework.jdbc.mapper.impl.EntityClassMetaDataImpl;

import javax.sql.DataSource;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entityClassMetaDataClient); //реализация DataTemplate, универсальная

        MyCache<String, Client> myCache = new MyCache<>();
        HwListener<String, Client> listener =
                (key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action);
        myCache.addListener(listener);

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, myCache);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
