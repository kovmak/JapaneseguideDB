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
public class JdbcOneToMany<T> {
    private final ConnectionManager connectionManager;

    public JdbcOneToMany(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected Set<T> getByForeignKey(
        UUID parentId, String sql, RowMapper<T> rowMapper, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, parentId, Types.OTHER);
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
        UUID parentId, Set<UUID> childIds, String sql, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            for (UUID childId : childIds) {
                statement.setObject(1, parentId, Types.OTHER);
                statement.setObject(2, childId, Types.OTHER);
                statement.addBatch();
            }
            int[] updateCounts = statement.executeBatch();
            int totalUpdates = 0;
            for (int count : updateCounts) {
                totalUpdates += count;
            }
            return totalUpdates > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(exceptionMessage);
        }
    }
}
