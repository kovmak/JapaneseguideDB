package com.krnelx.presentation.controller;

import com.krnelx.persistence.entity.Description;
import com.krnelx.persistence.entity.Events;
import com.krnelx.persistence.entity.Person;
import com.krnelx.persistence.util.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MainController {

    @FXML
    private Label userLabel;

    @FXML
    private Label accessLevelLabel;

    @FXML
    private ListView<String> sectionListView;

    @FXML
    private ListView<String> categoryListView;

    @FXML
    private ListView<String> descriptionListView;

    @FXML
    private Label InfoLabel;

    @FXML
    private Button addButton;

    private String login;
    private String userId;
    protected UUID idUser;
    private String role;

    private Connection connection;

    private final ConnectionManager connectionManager;
    private final ApplicationContext springContext;

    @Autowired
    public MainController(ConnectionManager connectionManager, ApplicationContext springContext) {
        this.connectionManager = connectionManager;
        this.springContext = springContext;
    }

    @FXML
    public void initialize() {
        setConnection(connection);
        retrieveUserData();
        retrieveSections();
        retrieveCategories();
        retrieveDescriptions();
    }

    public void setCurrentUser(String login) {
        this.login = login;
        retrieveUserData();
    }

    @FXML
    private void showAddButtons(String role) {
        if (role != null && (role.equals("moder") || role.equals("admin"))) {
            addButton.setVisible(true);
        } else {
            addButton.setVisible(false);
        }
    }

    // Метод для отримання даних користувача
    private void retrieveUserData() {
        try {
            if (login != null) {
                Statement statement = connection.createStatement();
                ResultSet userResultSet = statement.executeQuery(
                        "SELECT * FROM users WHERE login = '" + login + "'");
                if (userResultSet.next()) {
                    String loginCurrentUser = userResultSet.getString("login");
                    String roleCurrenUser = userResultSet.getString("role");

                    UUID idUserForCart = (UUID) userResultSet.getObject("id");
                    this.idUser = idUserForCart;

                    userLabel.setText("User: " + loginCurrentUser);
                    accessLevelLabel.setText("Access: " + roleCurrenUser);
                    showAddButtons(roleCurrenUser);
                    this.role = roleCurrenUser;
                    userId = userResultSet.getString("id"); // Збереження userId
                } else {
                    // Якщо користувача не знайдено, можна встановити відповідні повідомлення у лейбли
                    userLabel.setText("User not found");
                    accessLevelLabel.setText("");
                }
                statement.close();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    // Метод для отримання секцій
    private void retrieveSections() {
        try {
            sectionListView.getItems().clear();
            Statement statement = connection.createStatement();
            ResultSet sectionResultSet = statement.executeQuery("SELECT * FROM events");

            List<Events> sections = new ArrayList<>();

            while (sectionResultSet.next()) {
                String idStr = sectionResultSet.getString("id");
                UUID id = UUID.fromString(idStr);
                String sectionName = sectionResultSet.getString("name");
                String sectionDescription = sectionResultSet.getString("description");
                sections.add(new Events(id, sectionName, sectionDescription));
            }

            statement.close();
            sectionResultSet.close();

            for (Events section : sections) {
                sectionListView.getItems().add(section.getName() + " - " + section.getDescription());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving sections", e);
        }
    }

    // Метод для отримання категорій
    private void retrieveCategories() {
        try {
            categoryListView.getItems().clear();
            Statement statement = connection.createStatement();
            ResultSet categoryResultSet = statement.executeQuery("SELECT * FROM person");

            List<Person> persons = new ArrayList<>();

            while (categoryResultSet.next()) {
                String idStr = categoryResultSet.getString("id");
                UUID id = UUID.fromString(idStr);
                String personName = categoryResultSet.getString("name");
                String personAddress = categoryResultSet.getString("address");
                persons.add(new Person(id, personName, personAddress));
            }

            statement.close();
            categoryResultSet.close();

            for (Person person : persons) {
                categoryListView.getItems().add(person.name() + " - " + person.address());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving categories", e);
        }
    }

    // Метод для отримання описів
    private void retrieveDescriptions() {
        try {
            descriptionListView.getItems().clear();
            Statement statement = connection.createStatement();
            ResultSet descriptionResultSet = statement.executeQuery("SELECT * FROM description");

            List<Description> descriptions = new ArrayList<>();

            while (descriptionResultSet.next()) {
                String idStr = descriptionResultSet.getString("id");
                UUID id = UUID.fromString(idStr);
                String descriptionName = descriptionResultSet.getString("name");
                String descriptionText = descriptionResultSet.getString("description");
                descriptions.add(new Description(id, descriptionName, descriptionText));
            }

            statement.close();
            descriptionResultSet.close();

            for (Description description : descriptions) {
                descriptionListView.getItems().add(description.name() + " - " + description.description());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving descriptions", e);
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connectionManager.get();
        retrieveUserData();
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    private void addElementsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/krnelx/presentation/view/addNewElements.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass()
                    .getResourceAsStream("/com/krnelx/presentation/icon.png")));
            stage.setTitle("Додавання елементів");

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            retrieveSections();
            retrieveCategories();
            retrieveDescriptions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void DeleteElementsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/krnelx/presentation/view/deleteElements.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass()
                    .getResourceAsStream("/com/krnelx/presentation/icon.png")));
            stage.setTitle("Видалення елементів");

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            retrieveSections();
            retrieveCategories();
            retrieveDescriptions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addWindow() {
        addElementsWindow();
    }

    @FXML
    private void deleteWindow() {
        DeleteElementsWindow();
    }
}
