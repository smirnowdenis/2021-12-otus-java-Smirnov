package ru.otus.homework.crm.service.impl;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.cache.HwCache;
import ru.otus.homework.cache.MyCache;
import ru.otus.homework.crm.datasource.DriverManagerDataSource;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.repository.executor.DbExecutorImpl;
import ru.otus.homework.crm.service.api.DBServiceClient;
import ru.otus.homework.crm.sessionmanager.TransactionRunner;
import ru.otus.homework.crm.sessionmanager.TransactionRunnerJdbc;
import ru.otus.homework.jdbc.mapper.DataTemplateJdbc;
import ru.otus.homework.jdbc.mapper.api.EntityClassMetaData;
import ru.otus.homework.jdbc.mapper.impl.EntityClassMetaDataImpl;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

class DbServiceClientImplTest {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImplTest.class);

    private static DataSource dataSource;
    private DBServiceClient dbServiceClient;
    private HwCache<Long, Client> clientHwCache;

    @BeforeAll
    static void setUp() {
        dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
    }

    @Test
    @DisplayName("Должен почиститься кеш после работы GC")
    void shouldClearCacheAfterGC() {
        setUpDbService(new TransactionRunnerJdbc(dataSource));

        dbServiceClient.saveClient(new Client("cachedClient1"));
        dbServiceClient.saveClient(new Client("cachedClient2"));
        dbServiceClient.saveClient(new Client("cachedClient3"));
        assertThat(clientHwCache.size()).isEqualTo(3);

        System.gc();

        await().atMost(5, TimeUnit.SECONDS).until(() -> clientHwCache.size() == 0);
    }

    @Test
    @DisplayName("Должен взять из кеша вместо запроса в БД")
    void shouldGetFromCacheInsteadOfDb() {
        setUpDbService(null);

        Client secondClient = new Client("second");
        clientHwCache.put(1L, new Client("first"));
        clientHwCache.put(2L, secondClient);
        clientHwCache.put(3L, new Client("third"));

        assertThat(clientHwCache.size()).isEqualTo(3);
        assertThat(dbServiceClient.getClient(2)).hasValue(secondClient);
    }

    private void setUpDbService(TransactionRunner transactionRunner) {
        var dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entityClassMetaDataClient); //реализация DataTemplate, универсальная
        clientHwCache = new MyCache<>();
        dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, clientHwCache);
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