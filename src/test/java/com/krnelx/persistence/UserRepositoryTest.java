package com.krnelx.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import com.krnelx.persistence.entity.Users;
import com.krnelx.persistence.entity.Users.UsersRole;
import com.krnelx.persistence.exception.EntityUpdateException;
import com.krnelx.persistence.repository.mapper.impl.UserRowMapper;
import com.krnelx.persistence.util.ConnectionManager;
import com.krnelx.persistence.util.PropertyManager;
import com.krnelx.persistence.repository.contract.UserRepository;
import com.krnelx.persistence.repository.impl.jdbc.UserRepositoryImpl;
import com.krnelx.persistence.init.FakeDatabaseInitializer;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

// TODO: removes, batch tests
class UserRepositoryTest {
    private static ConnectionManager connectionManager;
    private static UserRepository userRepository;

    @BeforeAll
    static void setup() {
        PropertyManager propertyManager = new PropertyManager(
            UserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("application.properties")
        );
        connectionManager = new ConnectionManager(propertyManager);
        userRepository = new UserRepositoryImpl(connectionManager, new UserRowMapper());
    }

    @BeforeEach
    void init() throws SQLException {
        FakeDatabaseInitializer.run(connectionManager.get());
    }

    @Test
    void findOneById_whenUserExists_thenReturnsUser() {
        // Given
        UUID userId = UUID.fromString("018f39f8-6850-75d3-b6b1-be865de4d061");
        Users expectedUser = new Users(userId,
            "john_doe",
            "john.doe@example.com",
            UsersRole.ADMIN,
            "password1");

        // When
        Optional<Users> actualOptionalUser = userRepository.findById(userId);

        // Then
        assertTrue(actualOptionalUser.isPresent(), "The found User object is not null");
        assertEquals(expectedUser, actualOptionalUser.get(), "The searched object is equal to the found one");
    }

    @Test
    void findOneById_whenUserDoesNotExist_thenReturnsEmptyOptional() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        Optional<Users> actualOptionalUser = userRepository.findById(id);

        // Then
        assertTrue(actualOptionalUser.isEmpty(), "Empty optional if the address with this id does not exist");
    }

    @Test
    @Tag("slow")
    void findAll_thenReturnsSetOfUser() {
        // Given
        int usersSize = 5;

        // When
        Set<Users> users = userRepository.findAll();

        // Then
        assertNotNull(users);
        assertEquals(usersSize, users.size());
    }

    @Test
    void save_whenInsertNewUser_thenReturnsAddressEntityWithGeneratedId() {
        // Given
        Users expectedUser = new Users(
            null,
            "emma_jones",
            "emma.jones@example.com",
            UsersRole.CLIENT,
            "emma1234"
        );

        // When
        Users actualUser = userRepository.save(expectedUser);
        UUID id = actualUser.id();
        Optional<Users> optionalFoundedUser = userRepository.findById(id);

        // Then
        assertNotNull(id);
        assertTrue(optionalFoundedUser.isPresent());
        assertEquals(actualUser, optionalFoundedUser.orElse(null));
    }

    @Test
    void save_whenUpdateExistUser_thenReturnsUser() {
        // Given
        UUID userId = UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5");
        Users expectedUser = new Users(
            userId,
            "jane_brown",
            "jane.brown@example.com",
            UsersRole.MODER,
            "P@ssword2"
        );

        // When
        userRepository.save(expectedUser);
        var optionalUser = userRepository.findById(userId);

        // Then
        assertEquals(expectedUser, optionalUser.orElse(null));
    }

    @Test
    void save_whenUpdateNotExistUser_thenThrowEntityUpdateException() {
        // Given
        UUID userId = UUID.randomUUID();
        Users expectedUser = new Users(
            userId,
            "jane_brown",
            "jane.brown@example.com",
            UsersRole.CLIENT,
            "P@ssword2"
        );

        // When
        Executable executable = () -> {
            userRepository.save(expectedUser);
            var optionalUser = userRepository.findById(userId);
        };

        // Then
        assertThrows(EntityUpdateException.class, executable);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connectionManager.closePool();
    }
}
