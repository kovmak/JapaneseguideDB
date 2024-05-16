package com.krnelx.presentation.controller;

import com.krnelx.domain.dto.UserGudesDto;
import com.krnelx.domain.service.impl.UserService;
import com.krnelx.persistence.entity.Users.UsersRole;
import com.krnelx.presentation.viewmodel.UserViewModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class SignUpController {
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);
    @Autowired
    private UserService userService;
    @FXML
    private Label idField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<UsersRole> roleComboBox;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private TextField addressField;
    @FXML
    private Label addressLabel;
    private UserViewModel userViewModel;

    public SignUpController() {
        // init
    }

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(UsersRole.values());
        roleComboBox.setValue(UsersRole.CLIENT);

        userViewModel = new UserViewModel(
            UUID.randomUUID(),
            "JohnDoe",
            "password123",
            UsersRole.CLIENT,
            "John Doe"
        );
        // Зв'язування властивостей ViewModel з View
        bindFieldsToViewModel();
    }

    private void bindFieldsToViewModel() {
        idField.setText(userViewModel.getId().toString());
        loginField.textProperty().bindBidirectional(userViewModel.loginProperty());
        passwordField.textProperty().bindBidirectional(userViewModel.passwordProperty());
        roleComboBox.valueProperty().bindBidirectional(userViewModel.roleProperty());
        nameField.textProperty().bindBidirectional(userViewModel.nameProperty());
        phoneNumberField.visibleProperty().bind(
            roleComboBox.valueProperty().isEqualTo(UsersRole.CLIENT)
                .or(roleComboBox.valueProperty().isEqualTo(UsersRole.MODER))
        );
        phoneNumberLabel.visibleProperty().bind(
            roleComboBox.valueProperty().isEqualTo(UsersRole.CLIENT)
                .or(roleComboBox.valueProperty().isEqualTo(UsersRole.MODER))
        );
        addressField.visibleProperty().bind(
            roleComboBox.valueProperty().isEqualTo(UsersRole.CLIENT)
                .or(roleComboBox.valueProperty().isEqualTo(UsersRole.MODER))
        );
        addressLabel.visibleProperty().bind(
            roleComboBox.valueProperty().isEqualTo(UsersRole.CLIENT)
                .or(roleComboBox.valueProperty().isEqualTo(UsersRole.MODER))
        );
    }

    @FXML
    private void onSaveButtonClicked() {
        logger.info("Saving User Data: {}", userViewModel);

        UserGudesDto userStoreDto = new UserGudesDto(
            userViewModel.getId(),
            userViewModel.getlogin(),
            userViewModel.getPassword(),
            userViewModel.getRole(),
            userViewModel.getname()
        );

        userService.create(userStoreDto);


        UUID id = userViewModel.getId();
        String name = userViewModel.getname();
        String phone = phoneNumberField.getText();
        String address = addressField.getText();

        saveClient(id, name, phone, address);

        idField.setText(UUID.randomUUID().toString());
    }
    protected void saveClient(UUID id, String name, String phone, String address) {
        // Параметри для підключення до бази даних
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres";

        // SQL-запит для вставки нового клієнта у таблицю clients
        String sql = "INSERT INTO client (id, name, phone, address) VALUES (?, ?, ?, ?)";

        try (
            // Встановлення з'єднання з базою даних
            Connection connection = DriverManager.getConnection(url, user, password);
            // Підготовка SQL-запиту
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            // Встановлюємо значення параметрів для запиту
            statement.setObject(1, id);
            statement.setString(2, name);
            statement.setString(3, phone);
            statement.setString(4, address);

            // Виконуємо запит на вставку даних
            statement.executeUpdate();
            logger.info("Клієнт успішно збережений у базі даних.");
        } catch (SQLException e) {
            logger.error("Помилка при збереженні клієнта у базі даних: {}", e.getMessage());
        }
    }

    @FXML
    private void onCancelButtonClicked() {
        idField.setText(UUID.randomUUID().toString());
        loginField.clear();
        passwordField.clear();
        roleComboBox.setValue(UsersRole.CLIENT);
        nameField.clear();
        phoneNumberField.clear();
        addressField.clear();
    }
}
