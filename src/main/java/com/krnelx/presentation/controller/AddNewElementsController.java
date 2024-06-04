package com.krnelx.presentation.controller;

import com.krnelx.persistence.util.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private CheckBox descriptionCheckBox;

    @FXML
    private CheckBox eventCheckBox;

    @FXML
    private CheckBox personCheckBox;

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
    public void onCheckBoxAction() {
        if (personCheckBox.isSelected()) {
            textField2.setDisable(true);
            textField3.setDisable(false);
        } else {
            textField2.setDisable(false);
            textField3.setDisable(true);
        }
    }

    @FXML
    public void onSave() {
        if (descriptionCheckBox.isSelected()) {
            saveDescription();
        } else if (eventCheckBox.isSelected()) {
            saveEvent();
        } else if (personCheckBox.isSelected()) {
            savePerson();
        } else {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Виберіть тип елементу для збереження");
        }
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

            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Період успішно збережено!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при збереженні періоду: " + e.getMessage());
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

            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Подія успішно збережена!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при збереженні події: " + e.getMessage());
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

            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Персона успішно збережена!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при збереженні персони: " + e.getMessage());
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
