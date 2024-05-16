package com.krnelx.persistence.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public final class DatabaseInitializer {
    private final ConnectionManager connectionManager;

    public DatabaseInitializer(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }
    public void init(){
        try (Connection connection = connectionManager.get();
            Statement statementForDDL = connection.createStatement();
            Statement statementForDML = connection.createStatement()){
            statementForDDL.execute(getSQL("ddl.sql"));
            statementForDML.execute(getSQL("dml.sql"));
        } catch (SQLException throwables){
            throw new RuntimeException(throwables);
        }
    }

    private String getSQL(final String resourceName){
        return new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    ConnectionManager.class.getClassLoader().getResourceAsStream(resourceName)))
        ).lines().collect(Collectors.joining("\n"));
    }
}
