package com.krnelx.persistence.repository;

import static java.lang.StringTemplate.STR;

import com.krnelx.persistence.entity.Entity;
import com.krnelx.persistence.exception.EntityDeleteException;
import com.krnelx.persistence.exception.EntityNotFoundException;
import com.krnelx.persistence.exception.EntityUpdateException;
import com.krnelx.persistence.repository.mapper.RowMapper;
import com.krnelx.persistence.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericJdbcRepository<T extends Entity> implements Repository<T> {

    private static final Logger logger = LoggerFactory.getLogger(GenericJdbcRepository.class);
    private final ConnectionManager connectionManager;
    private final RowMapper<T> rowMapper;
    private final String tableName;

    public GenericJdbcRepository(
        ConnectionManager connectionManager, RowMapper<T> rowMapper, String tableName) {
        this.connectionManager = connectionManager;
        this.rowMapper = rowMapper;
        this.tableName = tableName;
    }

    protected RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    // Переписати на аспекти spring Context
    @Override
    public Optional<T> findById(UUID id) {
        logger.info("Finding entity by id: {}", id);
        return findBy("id", id);
    }

    public Optional<T> findByLogin(String column, Object value) {
        logger.info("Finding entity by {}: {}", column, value);

        final String sql = STR. """
        SELECT *
          FROM users
         WHERE \{
            column } = ?
    """ ;

        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value.toString()); // Передача значення у вигляді рядка
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(rowMapper.mapRow(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error while finding entity by {}: {}", column, value, e);
            return Optional.empty();
        }
    }


    @Override
    public Optional<T> findBy(String column, Object value) {
        logger.info("Finding entity by {}: {}", column, value);
        final String sql =
            STR. """
            SELECT *
              FROM \{
                tableName }
             WHERE \{
                column } = ?
        """ ;

        UUID id = (UUID) value;
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id, Types.OTHER);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(rowMapper.mapRow(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error while finding entity by {}: {}", column, value, e);
            return Optional.empty();
        }
    }


    @Override
    public Set<T> findAllWhere(String sql) {
        logger.info(sql);
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters if necessary (not applicable for SELECT queries)

            ResultSet resultSet = statement.executeQuery();
            Set<T> entities = new LinkedHashSet<>();
            while (resultSet.next()) {
                // Map each row to an entity using the rowMapper
                entities.add(rowMapper.mapRow(resultSet));
            }
            return entities;
        } catch (SQLException throwables) {
            // If an exception occurs during database access, throw an EntityNotFoundException
            throw new EntityNotFoundException(
                "Error while retrieving entities from table: " + tableName + throwables);
        }
    }

    @Override
    public Set<T> findAll() {
        logger.info("Finding all entities");
        final String sql = String.format(
            "SELECT * FROM %s", tableName);

        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            Set<T> entities = new LinkedHashSet<>();
            while (resultSet.next()) {
                entities.add(rowMapper.mapRow(resultSet));
            }
            return entities;
        } catch (SQLException throwables) {
            logger.error("Error while finding all entities", throwables);
            throw new EntityNotFoundException(
                String.format("Error while finding all entities from table: %s", tableName));
        }
    }

    @Override
    public T save(final T entity) {
        List<Object> values = tableValues(entity);

        T newEntity;
        if (!Objects.isNull(entity.id())) {
            UUID newId = UUID.randomUUID();
            values.addFirst(entity.id());
            try {
                newEntity = insert(values);
                logger.info(newEntity.toString());
            } catch (EntityUpdateException e) {
                logger.error("Error occurred while saving entity: {}", e.getMessage());
                return null; // Повертаємо null, щоб продовжити виконання програми
            }
        } else {
            values.addLast(entity.id());
            newEntity = update(values);
        }

        return newEntity;
    }


    public Set<String> findAllLogins() {
        Set<String> logins = new HashSet<>();
        final String sql = "SELECT login FROM users";

        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                logins.add(resultSet.getString("login"));
            }
        } catch (SQLException throwables) {
            logger.error("Error while finding all logins", throwables);
            // Можливо, слід обробити помилку відповідним чином, наприклад, кинути виняток
        }
        return logins;
    }

    protected T insert(List<Object> values) {
        List<String> attributes = tableAttributes();
        String attributesString = "id, " + String.join(", ", attributes);
        String placeholders =
            Stream.generate(() -> "?")
                .limit(attributes.size() + 1)
                .collect(Collectors.joining(", "));
        String sql =
            STR. """
            INSERT INTO \{
                tableName } (\{
                attributesString })
                VALUES (\{
                placeholders })
        """ ;

        if (attributes.contains("login")) {
            String login = (String) values.get(attributes.indexOf("login") + 1);
            if (findAllLogins().contains(login)) {
                throw new EntityUpdateException("Такий логін вже існує");
            }
        }

        if (attributes.stream().anyMatch(a -> a.equals("created_at"))) {
            values.add(LocalDateTime.now()); // created_at
            values.add(LocalDateTime.now()); // updated_at
        }

        return updateExecute(values, sql, "Помилка при додаванні нового запису в таблицю");
    }

    protected T update(List<Object> values) {
        List<String> attributes = tableAttributes();
        String attributesString =
            attributes.stream()
                .filter(a -> !a.contains("created_at"))
                .map(a -> STR. "\{ a } = ?" )
                .collect(Collectors.joining(", "));
        String sql =
            STR. """
                      UPDATE \{ tableName }
                         SET \{ attributesString }
                       WHERE id = ?
                    """ ;

        if (attributes.stream().anyMatch(a -> a.equals("updated_at"))) {
            values.add(LocalDateTime.now()); // updated_at
        }

        return updateExecute(values, sql, "Помилка при оновленні існуючого запису в таблиці");
    }

    private T updateExecute(List<Object> values, String sql, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) instanceof Enum) {
                    statement.setString(i + 1, values.get(i).toString());
                    logger.info(statement.toString() + i + values.size() + values.get(i));
                } else {
                    statement.setObject(i + 1, values.get(i), Types.OTHER);
                    logger.info(statement.toString() + i + "\n" + values.size());
                }
            }

            logger.info(statement.toString());
            statement.executeUpdate();

            logger.info(statement.toString());
            UUID id = (UUID) values.stream()
                .filter(UUID.class::isInstance)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("UUID not found in values list");
                    return new NoSuchElementException("UUID not found");
                });

            return findById(id)
                .orElseThrow(() -> {
                    logger.error("Entity with ID {} not found after update", id);
                    return new EntityUpdateException(exceptionMessage);
                });
        } catch (SQLException | NoSuchElementException e) {
            logger.error("Error occurred while batch inserting records into the table", e);
            throw new EntityUpdateException(exceptionMessage);
        }
    }


    @Override
    public Set<T> save(Collection<T> entities) {
        Set<T> results;
        List<List<Object>> listOfValues = new ArrayList<>(new ArrayList<>());

        for (var entity : entities) {
            List<Object> values = tableValues(entity);
            if (Objects.isNull(entity.id())) {
                values.addFirst(UUID.randomUUID());
            } else {
                values.addLast(entity.id());
            }
            listOfValues.add(values);
        }

        if (entities.stream().allMatch(e -> Objects.isNull(e.id()))) {
            results = batchInsert(listOfValues);
        } else {
            results = batchUpdate(listOfValues);
        }
        return results;
    }


    protected Set<T> batchInsert(List<List<Object>> listOfValues) {
        List<String> attributes = tableAttributes();
        String attributesString = "id, " + String.join(", ", attributes);
        String placeholders =
            Stream.generate(() -> "?")
                .limit(attributes.size() + 1)
                .collect(Collectors.joining(", "));
        String sql =
            STR. """
            INSERT INTO "\{ tableName }" (\{
                attributesString })
            VALUES (\{
                placeholders })
    """ ;
        logger.info(sql);
        if (attributes.stream().anyMatch(a -> a.equals("created_at"))) {
            listOfValues.forEach(values -> {
                values.add(LocalDateTime.now()); // created_at
                values.add(LocalDateTime.now()); // updated_at
            });
        }

        return batchExecute(listOfValues, sql,
            "Помилка при додаванні нового запису в таблицю" + listOfValues);
    }

    protected Set<T> batchUpdate(List<List<Object>> listOfValues) {
        List<String> attributes = tableAttributes();
        List<String> nonKeyAttributes = attributes.stream()
            .filter(a -> !a.equals("id") && !a.contains("created_at"))
            .toList();

        String setClause =
            nonKeyAttributes.stream()
                .map(a -> STR. "\{ a } = ?" )
                .collect(Collectors.joining(", "));

        String sql =
            STR. """
        UPDATE "\{ tableName }"
           SET \{ setClause }
         WHERE id = ?
    """ ;

        if (attributes.contains("updated_at")) {
            LocalDateTime updatedAt = LocalDateTime.now();
            listOfValues.forEach(values -> {
                if (values.size() == nonKeyAttributes.size() + 1) { // Check if id is provided
                    values.add(updatedAt); // Add updated_at if id is provided
                }
            });
        }

        return batchExecute(listOfValues, sql,
            "Помилка при оновленні існуючого запису в таблиці" + listOfValues);
    }

    private Set<T> batchExecute(List<List<Object>> listOfValues, String sql,
        String exceptionMessage) {
        Set<T> results = new LinkedHashSet<>();
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            for (var values : listOfValues) {
                // Логуємо дані перед виконанням запиту
                System.out.println("Batch execute values: " + values.toString());

                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i), Types.OTHER);
                }
                statement.addBatch();
            }

            statement.executeBatch();

            // тут ми додаєм результат додавання
            results = getEntitiesAfterBatchExecute(listOfValues);
        } catch (SQLException throwables) {
            // Логуємо помилку, якщо вона виникла
            throwables.printStackTrace();
            throw new EntityUpdateException(exceptionMessage);
        }
        return results;
    }

    private Set<T> getEntitiesAfterBatchExecute(List<List<Object>> listOfValues) {
        Set<T> results;
        List<String> ids = listOfValues.stream().map(values -> {
            UUID id = (UUID) values.stream()
                .filter(UUID.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UUID not found"));

            return STR. "'\{ id.toString() }'" ;
        }).toList();

        // Construct the SQL query
        String sql = "SELECT * FROM " + tableName + " WHERE id IN (" + String.join(",", ids) + ")";

        results = findAllWhere(sql); // Use the constructed SQL query
        logger.info(results.toString());
        return results;
    }

    @Override
    public boolean delete(UUID id) {
        final String sql =
            STR. """
        DELETE FROM \{ tableName }
              WHERE id = ?
    """ ;

        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id, Types.OTHER);

            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(
                STR. "Помилка при видаленні запису з таблиці по id: \{ id.toString() }" );
        }
    }

    public boolean delete(Collection<UUID> ids) {
        final String sql =
            STR. """
            DELETE FROM \{ tableName }
                  WHERE id = ?
        """ ;

        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            for (var id : ids) {
                statement.setObject(1, id, Types.OTHER);
                statement.addBatch();
            }
            statement.executeBatch();

            // Змінено розміщення повернення результату та опрацьовано пакетні запити правильно
            return true;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(
                STR. "Помилка при видаленні записів з таблиці по ids: \{ ids.toString() }" );
        }
    }


    protected abstract List<String> tableAttributes();

    protected abstract List<Object> tableValues(T entity);
}
