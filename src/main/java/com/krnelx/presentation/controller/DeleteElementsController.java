package com.krnelx.presentation.controller;

import com.krnelx.persistence.util.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class DeleteElementsController {

    @FXML
    private CheckBox eventCheckBox;

    @FXML
    private CheckBox personCheckBox;

    @FXML
    private CheckBox descriptionCheckBox;

    @FXML
    private ListView<String> dataListView;

    @FXML
    private Button deleteButton;

    private final ConnectionManager connectionManager;

    @Autowired
    public DeleteElementsController(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @FXML
    private void loadData() {
        dataListView.getItems().clear();

        if (eventCheckBox.isSelected()) {
            loadEvents();
        } else if (personCheckBox.isSelected()) {
            loadPersons();
        } else if (descriptionCheckBox.isSelected()) {
            loadDescriptions();
        } else {
            showAlert(Alert.AlertType.WARNING, "Попередження", "Будь ласка, виберіть тип елементу для завантаження даних.");
        }
    }

    private void loadEvents() {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM events")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                dataListView.getItems().add(name);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при завантаженні подій: " + e.getMessage());
        }
    }

    private void loadPersons() {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM person")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                dataListView.getItems().add(name);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при завантаженні персон: " + e.getMessage());
        }
    }

    private void loadDescriptions() {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM description")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                dataListView.getItems().add(name);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при завантаженні описів: " + e.getMessage());
        }
    }

    @FXML
    private void deleteSelected() {
        String selectedItem = dataListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Попередження", "Будь ласка, виберіть елемент для видалення.");
            return;
        }

        if (eventCheckBox.isSelected()) {
            deleteEvent(selectedItem);
        } else if (personCheckBox.isSelected()) {
            deletePerson(selectedItem);
        } else if (descriptionCheckBox.isSelected()) {
            deleteDescription(selectedItem);
        } else {
            showAlert(Alert.AlertType.WARNING, "Попередження", "Будь ласка, виберіть тип елементу для видалення.");
        }
    }

    private void deleteEvent(String name) {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM events WHERE name = ?")) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Подію успішно видалено.");
                loadData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при видаленні події.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при видаленні події: " + e.getMessage());
        }
    }

    private void deletePerson(String name) {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM person WHERE name = ?")) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Персону успішно видалено.");
                loadData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при видаленні персони.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при видаленні персони: " + e.getMessage());
        }
    }

    private void deleteDescription(String name) {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM description WHERE name = ?")) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Період успішно видалено.");
                loadData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при видаленні періоду.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка при видаленні періоду: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
