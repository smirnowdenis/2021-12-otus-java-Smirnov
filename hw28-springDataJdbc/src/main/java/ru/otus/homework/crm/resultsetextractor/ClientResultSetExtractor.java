package ru.otus.homework.crm.resultsetextractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ClientResultSetExtractor implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clients = new ArrayList<Client>();
        Client client = null;
        while (rs.next()) {
            var clientId = rs.getLong("id");
            if (client == null || !client.getId().equals(clientId)) {
                client = new Client(clientId, rs.getString("name"), extractAddress(rs), extractFirstPhone(rs));
                clients.add(client);
            } else {
                client.getPhones().add(extractPhone(rs));
            }
        }
        return clients;
    }

    private Set<Phone> extractFirstPhone(ResultSet rs) throws SQLException {
        Set<Phone> phones = new LinkedHashSet<>();
        phones.add(extractPhone(rs));
        return phones;
    }

    private Address extractAddress(ResultSet rs) throws SQLException {
        return new Address(rs.getLong("address_id"),
                rs.getString("address_street"),
                rs.getString("address_clientId")
        );
    }

    private Phone extractPhone(ResultSet rs) throws SQLException {
        return new Phone(rs.getLong("phone_id"),
                rs.getString("phone_number"),
                rs.getString("phone_clientId")
        );
    }
}
