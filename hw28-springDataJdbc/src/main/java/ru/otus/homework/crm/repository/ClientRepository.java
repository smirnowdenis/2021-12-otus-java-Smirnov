package ru.otus.homework.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.resultsetextractor.ClientResultSetExtractor;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    @Query(value = """
            select c.id        as id,
                   c.name      as name,
                   a.id        as address_id,
                   a.street    as address_street,
                   a.client_id as address_clientId,
                   p.id        as phone_id,
                   p.number    as phone_number,
                   p.client_id as phone_clientId
            from client c
                left outer join address a
                                on a.client_id = c.id
                left outer join phone p
                                on p.client_id = c.id
            """,
            resultSetExtractorClass = ClientResultSetExtractor.class)
    List<Client> findAll();
}
