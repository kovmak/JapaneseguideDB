package com.krnelx.presentation.controller;

import static com.krnelx.presentation.Runner.springContext;

import com.krnelx.domain.exception.AuthenticationException;
import com.krnelx.domain.exception.UserAlreadyAuthenticatedException;
import com.krnelx.domain.service.impl.AuthenticationService;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SignInController {

    private final AuthenticationService authenticationService;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button registrationButton;

    @FXML
    private Button exitButton;


    public SignInController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @FXML
    public void initialize() {

    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void signIn() {
        String login = usernameField.getText();
        String password = passwordField.getText();

        try {
            boolean authenticated = authenticationService.authenticate(login, password);

            if (authenticated) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Вхід виконано успішно!");

                // Завантаження відображення вікна Main
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/krnelx/presentation/view/main.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Parent root = loader.load();
                MainController mainController = loader.getController();

                mainController.setCurrentUser(login);
                // Показ вікна Main
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/krnelx/presentation/icon.png")));
                stage.setTitle("Головне вікно");
                stage.show();

                // Закриття поточного вікна авторизації
                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Невірний логін або пароль.");
            }
        } catch (UserAlreadyAuthenticatedException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", e.getMessage());
        } catch (AuthenticationException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Невірний логін або пароль.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void goToRegistration(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/krnelx/presentation/view/signUp.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            SignUpController signUpController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass()
                .getResourceAsStream("/com/krnelx/presentation/icon.png")));
            stage.setTitle("Реєстрація");

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.initOwner(((Node) event.getSource()).getScene().getWindow());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
