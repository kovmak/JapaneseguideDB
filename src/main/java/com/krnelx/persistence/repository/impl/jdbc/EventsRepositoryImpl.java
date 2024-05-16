package com.krnelx.persistence.repository.impl.jdbc;

import com.krnelx.persistence.entity.Events;
import com.krnelx.persistence.repository.GenericJdbcRepository;
import com.krnelx.persistence.repository.contract.EventsRepository;
import com.krnelx.persistence.repository.contract.TableNames;
import com.krnelx.persistence.repository.mapper.impl.EventsRowMapper;
import com.krnelx.persistence.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class EventsRepositoryImpl extends GenericJdbcRepository<Events> implements
        EventsRepository {
    private final ConnectionManager connectionManager;

    public EventsRepositoryImpl(
        ConnectionManager connectionManager,
        EventsRowMapper sectionRowMapper) {
        super(connectionManager, sectionRowMapper, TableNames.EVENTS.getName());
        this.connectionManager = connectionManager;
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of(
            "name",
            "description"
        );
    }

    @Override
    protected List<Object> tableValues(Events sections) {
        return List.of(
            sections.name()
        );
    }

    @Override
    public Optional<Events> findByName(String name) {
        return findBy("name", name);
    }

    @Override
    public Optional<Events> findByDescription(String description) {
        return super.findBy("description", description);
    }

    @Override
    public boolean attach(UUID parentId, UUID childId) {
        String sql = "UPDATE Toy SET section_id = ? WHERE id = ?";
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, parentId);
            statement.setObject(2, childId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            new SQLException(e);
            return false;
        }
    }

    @Override
    public boolean detach(UUID parentId, UUID childId) {
        String sql = "UPDATE Toy SET section_id = NULL WHERE id = ?";
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, childId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            new SQLException(e);
            return false;
        }
    }
}

