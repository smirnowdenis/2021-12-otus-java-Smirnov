package ru.otus.homework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.homework.core.repository.DataTemplateHibernate;
import ru.otus.homework.core.repository.HibernateUtils;
import ru.otus.homework.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.homework.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Admin;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.Phone;
import ru.otus.homework.crm.service.impl.DBServiceAdminImpl;
import ru.otus.homework.crm.service.impl.DbServiceClientImpl;
import ru.otus.homework.web.server.ClientsWebServer;
import ru.otus.homework.web.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.homework.web.service.AdminAuthService;
import ru.otus.homework.web.service.AdminAuthServiceImpl;
import ru.otus.homework.web.service.TemplateProcessor;
import ru.otus.homework.web.service.TemplateProcessorImpl;

public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration,
                Client.class, Address.class, Phone.class, Admin.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var dbServiceAdmin = new DBServiceAdminImpl(new DataTemplateHibernate<>(Admin.class), transactionManager);
        var dbServiceClient = new DbServiceClientImpl(new DataTemplateHibernate<>(Client.class), transactionManager);


        Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        AdminAuthService authService = new AdminAuthServiceImpl(dbServiceAdmin);

        ClientsWebServer clientsWebServer = new ClientsWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
