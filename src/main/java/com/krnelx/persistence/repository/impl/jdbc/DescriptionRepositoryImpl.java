package com.krnelx.persistence.repository.impl.jdbc;

import com.krnelx.persistence.entity.Description;
import com.krnelx.persistence.repository.GenericJdbcRepository;
import com.krnelx.persistence.repository.contract.TableNames;
import com.krnelx.persistence.repository.contract.DescriptionRepository;
import com.krnelx.persistence.repository.mapper.impl.DescriptionRowMapper;
import com.krnelx.persistence.util.ConnectionManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class DescriptionRepositoryImpl extends GenericJdbcRepository<Description> implements DescriptionRepository {

    private final JdbcManyToMany<Description> jdbcManyToMany;
    private final DescriptionRowMapper descriptionRowMapper;

    public DescriptionRepositoryImpl(
        ConnectionManager connectionManager,
        DescriptionRowMapper descriptionRowMapper,
        JdbcManyToMany<Description> jdbcManyToMany) {
        super(connectionManager, descriptionRowMapper, TableNames.DESCRIPTION.getName());
        this.jdbcManyToMany = jdbcManyToMany;
        this.descriptionRowMapper = descriptionRowMapper;
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of(
            "name",
            "description",
            "price",
            "category_id"
        );
    }

    @Override
    protected List<Object> tableValues(Description description) {
        return List.of(
                description.name(),
                description.description());
    }

    @Override
    public Set<Description> findAllDescriptionsCategory(UUID categoryId) {
        final String sql = """
        SELECT d.id, d.name, d.description, d.price, d.category_id, d.manufacture_id
        FROM toy d
        WHERE d.category_id = ?;
        """;

        return jdbcManyToMany.getByPivot(
            categoryId,
            sql,
            descriptionRowMapper,
            "Error while getting all description for category id: " + categoryId);
    }


    @Override
    public boolean attach(UUID descriptionId, UUID categoryId) {
        final String sql =
            """
            UPDATE description
            SET category_id = ?
            WHERE id = ?;
            """;
        return jdbcManyToMany.executeUpdate(
            categoryId, descriptionId, sql, "Помилка при додаванні категорії");
    }

    @Override
    public boolean detach(UUID descriptionId, UUID categoryId) {
        final String sql =
            """
            UPDATE description
            SET category_id = NULL
            WHERE id = ? AND category_id = ?;
            """;
        return jdbcManyToMany.executeUpdate(
                descriptionId, categoryId, sql, "Помилка при видаленні категорії");
    }
}
