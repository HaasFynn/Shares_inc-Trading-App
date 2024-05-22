package javafx.login;

import backend.dao.UserDao;
import backend.dao.UserDaoImpl;
import backend.entities.User;
import jakarta.persistence.EntityManager;
import javafx.assets.Hash;
import javafx.assets.LanguagePack;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Locale;

import backend.functional.EntityManagement;
import javafx.share_creation.ShareCreatorPane;

public class LoginController {
    public final LoginPane pane;
    private final UserDao userHandler;

    public LoginController(LoginPane pane) {
        this.pane = pane;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userHandler = new UserDaoImpl(entityManager);
    }

    public void handleLoginAction() {
        String username = pane.usernameField.getText();
        String hashedPassword = Hash.getPasswordHashed(pane.passwordField.getText());
        User user = userHandler.getByPassword(username, hashedPassword);
        if (user == null) {
            return;
        }
        pane.getScene().setRoot(new ShareCreatorPane(pane.stage, pane.font));
    }

    public void enableAllInputs() {
        changeInputFieldAvailability(false);
        changeButtonAvailability(false);
    }

    private void disableAllInputs() {
        changeInputFieldAvailability(true);
        changeButtonAvailability(true);
    }

    private void changeInputFieldAvailability(boolean disableValue) {
        pane.usernameField.setDisable(disableValue);
        pane.passwordField.setDisable(disableValue);
        //pane.dbSelectionBox.setDisable(disableValue);
        //pane.getLanguageBox.setDisable(disableValue);
    }

    private void changeButtonAvailability(boolean disableValue) {
        //pane.loginButton.setDisable(disableValue);
        //pane.passwordResetButton.setDisable(disableValue);
        //pane.exitButton.setDisable(disableValue);
    }

    private boolean areInputFieldsEmpty() {
        return pane.usernameField.getText().isEmpty() || pane.passwordField.getText().isEmpty();
    }

    public void handlePasswordResetButtonAction() {
        String username = pane.usernameField.getText();
        String hashedPassword = Hash.getPasswordHashed(pane.passwordField.getText());
        String errorMessage;
        if (areInputFieldsEmpty()) {
            errorMessage = getValue("login.status.empty.fields");
        } else {
            User user = userHandler.getByPassword(username, hashedPassword);
            errorMessage = getLoginResponse(user, hashedPassword);
        }
        if (errorMessage.isEmpty()) {
            return;
        }
        setStatusText(errorMessage, true, "text-danger");
    }

    private String getLoginResponse(User user, String hashedPassword) {
        if (user == null) {
            return getValue("login.status.user.not.found");
        }
        if (!doesLoginMatch(user, hashedPassword)) {
            return getValue("login.status.failed");
        }
        return "";
    }

    private void setStatusText(String key, boolean visible, String color) {
        pane.statusText.textProperty().bind(pane.getValue(key));
        pane.statusText.setVisible(visible);
        pane.statusText.getStyleClass().add(color);
    }

    private boolean doesLoginMatch(User user, String password) {
        return user.getPassword().equals(Hash.getPasswordHashed(password));
    }

    public void handleOpenResetBox() {
        pane.changePasswordBox.setVisible(!pane.changePasswordBox.isVisible());
    }

    public void handleExitButtonAction() {
        //Stage stage = (Stage) pane.getWindow();
        //stage.close();
    }

    public void handleOnEnter() {
        pane.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLoginAction();
            }
            event.consume();
        });
    }

    public void handleLanguageChange(String newLanguage) {
        LanguagePack.setLocale(Locale.forLanguageTag(newLanguage));
    }


    private static String getValue(String key) {
        return LanguagePack.createStringBinding(key).getValue();
    }
}
