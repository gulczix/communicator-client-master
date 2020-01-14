package application.layout.controllers;

import java.io.IOException;
import java.net.UnknownHostException;

import application.layout.alerts.CustomAlert;
import application.server.communication.ServerMessage;
import application.server.communication.messages.LoginRequest;
import application.server.communication.messages.RegistrationRequest;
import application.validation.data.LoginValidator;
import application.validation.data.PasswordValidator;
import application.validation.data.RepeatedPasswordValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Kontroler obslugujacy rejestracje i logowanie.
 */
public class MainController extends AbstractController {

    @FXML
    private TextField registrationLoginTextField;

    @FXML
    private PasswordField registrationPasswordTextField;

    @FXML
    private PasswordField registrationRepeatedPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField loginPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    void onRegisterButtonClick(ActionEvent event) {
        if (!isRegistrationDataValid()) {
            return;
        }

        try {
            sendRegistrationMessage();
        } catch (IOException e) {
            CustomAlert.error(e);
        }
    }

    @FXML
    void onLoginButtonClick(ActionEvent event) {
        if (!isLoginDataValid()) {
            return;
        }

        try {
            sendLoginMessage();
        } catch (IOException e) {
            CustomAlert.error(e);
        }
    }

    /**
     * Sprawdza, czy dane rejestracji sa poprawne.
     * @return True, jesli dane rejestracji sa poprawne, w przeciwnym razie false.
     */
    private boolean isRegistrationDataValid() {
        return validateRegistrationLogin() && validateRegistrationPasswords();
    }

    /**
     * Sprawdza, czy dane logowania sa poprawne.
     * @return True, jesli dane logowania sa poprawne, w przeciwnym razie false.
     */
    private boolean isLoginDataValid() {
        return validateLogin() && validateLoginPassword();
    }

    /**
     * Sprawdza, czy login podany przy rejestracji jest poprawny.
     * @return True, jesli login podany przy rejestracji jest poprawny, w przeciwnym razie false.
     */
    private boolean validateRegistrationLogin() {
        return new LoginValidator().isValid(registrationLoginTextField.getText(), true);
    }

    /**
     * Sprawdza, czy hasla rejestracji sa poprawne.
     * @return True, jesli hasla rejestracji sa poprawne, w przeciwnym razie false.
     */
    private boolean validateRegistrationPasswords() {
        Pair<String, String> passwordsForValidation = new Pair<>(registrationPasswordTextField.getText(),
                                                                 registrationRepeatedPasswordField.getText());
        return new RepeatedPasswordValidator().isValid(passwordsForValidation, true);
    }

    /**
     * Wysyla do serwera zadanie z danymi rejestracji.
     * @throws UnknownHostException
     * @throws IOException
     */
    private void sendRegistrationMessage() throws UnknownHostException, IOException {
        RegistrationRequest request = new RegistrationRequest(registrationLoginTextField.getText(),
                                                              registrationPasswordTextField.getText());

        ServerMessage response = request.send();

        if (!handleErrorResponse(response)) {
            CustomAlert.info(response);
        }

        request.close();
    }

    /**
     * Sprawdza, czy login podany przy logowaniu jest poprawny.
     * @return True, jesli login podany przy logowaniu jest poprawny, w przeciwnym razie false.
     */
    private boolean validateLogin() {
        return new LoginValidator().isValid(loginTextField.getText(), true);
    }

    /**
     * Sprawdza, czy haslo logowania jest poprawne.
     * @return True, jesli haslo logowania jest poprawne, w przeciwnym razie false.
     */
    private boolean validateLoginPassword() {
        return new PasswordValidator().isValid(loginPasswordField.getText(), true);
    }

    /**
     * Wysyla do serwera zadanie z danymi logowania.
     * @throws UnknownHostException
     * @throws IOException
     */
    private void sendLoginMessage() throws UnknownHostException, IOException {
        LoginRequest request = new LoginRequest(loginTextField.getText(),
                                                loginPasswordField.getText());

        ServerMessage response = request.send();

        if (!handleErrorResponse(response)) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ConversationView.fxml"));
            Stage stage = getStage();
            stage.setScene(new Scene(loader.load()));

            ConversationController controller = loader.<ConversationController>getController();
            controller.setConnection(request.getConnection());
            controller.setLogin(loginTextField.getText());
            controller.initEventHandlers();

            stage.show();
        } else {
            request.close();
        }
    }

    @Override
    protected Stage getStage() {
        return (Stage) registerButton.getScene().getWindow();
    }
}
