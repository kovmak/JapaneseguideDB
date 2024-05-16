package com.krnelx.persistence.repository.impl.jdbc;

import com.krnelx.persistence.entity.Person;
import com.krnelx.persistence.repository.GenericJdbcRepository;
import com.krnelx.persistence.repository.contract.PersonRepository;
import com.krnelx.persistence.repository.contract.TableNames;
import com.krnelx.persistence.repository.mapper.impl.PersonRowMapper;
import com.krnelx.persistence.util.ConnectionManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl extends GenericJdbcRepository<Person> implements
        PersonRepository {
    private final PersonRowMapper categoryRowMapper;
    private final JdbcManyToMany<Person> jdbcManyToMany;

    public PersonRepositoryImpl(
        ConnectionManager connectionManager,
        PersonRowMapper rowMapper,
        PersonRowMapper categoryRowMapper,
        JdbcManyToMany<Person> jdbcManyToMany) {
        super(connectionManager, rowMapper, TableNames.PERSON.getName());
        this.jdbcManyToMany = jdbcManyToMany;
        this.categoryRowMapper = categoryRowMapper;
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("name", "description");
    }

    @Override
    protected List<Object> tableValues(Person category) {
        return List.of(category.name(), category.address());
    }

    @Override
    public Set<Person> findAllByToyId(UUID personId) {
        final String sql = """
        SELECT c.id,
               c.name,
               c.description
          FROM category AS c
               JOIN Description_category AS tc
                 ON c.id = tc.category_id
         WHERE tc.Description_id = ?;
        """;

        // Assuming jdbcManyToMany is a JdbcManyToMany<Person>
        return jdbcManyToMany.getByPivot(
            personId,
            sql,
            categoryRowMapper, // Use PersonRowMapper here
            "Error while getting all categories for toy id: " + personId);
    }

    @Override
    public boolean attach(UUID personId, UUID categoryId) {
        final String sql =
            """
            UPDATE person
            SET category_id = ?
            WHERE id = ?;
            """;
        return jdbcManyToMany.executeUpdate(
            categoryId, personId, sql, "Помилка при додаванні категорії");
    }

    @Override
    public boolean detach(UUID parentId, UUID childId) {
        return false;
    }
}
