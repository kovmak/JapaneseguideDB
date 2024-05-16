package com.krnelx.presentation.controller;

import com.krnelx.persistence.util.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class AddNewElementsController {

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    private final ConnectionManager connectionManager;

    @Autowired
    public AddNewElementsController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @FXML
    public void onSaveDescription() {
        String elementType = getElementTypeDescription(); // Отримайте тип елементу
        switch (elementType) {
            case "description":
                saveDescription();
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Помилка", "Невідомий тип елементу");
                break;
        }
    }

    @FXML
    public void onSaveEvent() {
        String elementType = getElementTypeEvent(); // Отримайте тип елементу
        switch (elementType) {
            case "events":
                saveEvent();
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Помилка", "Невідомий тип елементу");
                break;
        }
    }

    @FXML
    public void onSavePerson() {
        String elementType = getElementTypePerson(); // Отримайте тип елементу
        switch (elementType) {
            case "person":
                savePerson();
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Помилка", "Невідомий тип елементу");
                break;
        }
    }

    private String getElementTypeDescription() {
        return "description";
    }

    private String getElementTypeEvent() {
        return "events";
    }

    private String getElementTypePerson() {
        return "person";
    }

    private void saveDescription() {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO description (id, name, description) VALUES (?, ?, ?)")) {
            UUID id = UUID.randomUUID();
            String name = textField1.getText();
            String description = textField2.getText();

            statement.setObject(1, id);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Опис успішно збережено!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при збереженні опису: " + e.getMessage());
        }
    }

    private void saveEvent() {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO events (id, name, description) VALUES (?, ?, ?)")) {
            UUID id = UUID.randomUUID();
            String name = textField1.getText();
            String description = textField2.getText();

            statement.setObject(1, id);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Опис успішно збережено!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при збереженні опису: " + e.getMessage());
        }
    }

    private void savePerson() {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO person (id, name, address) VALUES (?, ?, ?)")) {
            UUID id = UUID.randomUUID();
            String name = textField1.getText();
            String address = textField3.getText();

            statement.setObject(1, id);
            statement.setString(2, name);
            statement.setString(3, address);
            statement.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Особу успішно збережено!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при збереженні особи: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onCancel() {
        Stage currentStage = (Stage) textField1.getScene().getWindow();
        currentStage.close();
    }
}
