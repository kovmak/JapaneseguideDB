package com.krnelx.persistence.repository.impl.jdbc;

import com.krnelx.persistence.exception.EntityDeleteException;
import com.krnelx.persistence.exception.EntityNotFoundException;
import com.krnelx.persistence.repository.mapper.RowMapper;
import com.krnelx.persistence.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JdbcManyToMany<T> {
    private final ConnectionManager connectionManager;

    public JdbcManyToMany(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected Set<T> getByPivot(
        UUID entityId, String sql, RowMapper<T> rowMapper, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, entityId, Types.OTHER);
            ResultSet resultSet = statement.executeQuery();

            Set<T> entities = new LinkedHashSet<>();
            while (resultSet.next()) {
                entities.add(rowMapper.mapRow(resultSet));
            }
            return entities;
        } catch (SQLException throwables) {
            throw new EntityNotFoundException(exceptionMessage);
        }
    }

    protected boolean executeUpdate(
        UUID firstId, UUID secondId, String sql, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, firstId, Types.OTHER);
            statement.setObject(2, secondId, Types.OTHER);
            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(exceptionMessage);
        }
    }
}
