package ru.otus.homework.web.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.service.api.DBServiceClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ClientsApiServlet extends HttpServlet {
    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();

        String jsonResponse;
        Long clientId = extractIdFromRequest(request);
        if (clientId == null) {
            List<Client> clients = dbServiceClient.findAll();
            jsonResponse = gson.toJson(clients);
        } else {
            Client client = dbServiceClient.getClient(clientId).orElse(null);
            jsonResponse = gson.toJson(client);
        }

        out.print(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            Client client = gson.fromJson(reader, Client.class);

            Client savedClient = dbServiceClient.saveClient(client);
            resp.getOutputStream().print(gson.toJson(savedClient));
        }
    }

    private Long extractIdFromRequest(HttpServletRequest request) {
        if (request.getPathInfo() == null) {
            return null;
        }
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

}
