package javafx.controllers;

import console.dao.UserDao;
import console.dao.UserDaoImpl;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.Authentication;
import javafx.assets.Hash;
import javafx.assets.LanguagePack;
import javafx.eventlisteners.EventListeners;
import javafx.MainPanel;
import javafx.panes.LoginPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;

public class LoginController extends CustomController {
    public final LoginPane pane;
    private final UserDao userHandler;

    public LoginController(Stage stage,LoginPane pane, EventListeners eventListeners) {
        super(stage, eventListeners);
        this.pane = pane;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userHandler = new UserDaoImpl(entityManager);
    }

    public void handleLoginAction() {
        String username = pane.getUsernameField().getText();
        String hashedPassword = Hash.getPasswordHashed(pane.getPasswordField().getText());
        User user = userHandler.getByPassword(username, hashedPassword);
        if (user == null) {
            setStatusText("login.status.user.not_found", true, "text-danger");
            return;
        }
        switchScene(user);
    }

    private void switchScene(User user) {
        MainPanel mainPanel = new MainPanel(stage, user);
        stage.getScene().setRoot(mainPanel);
    }

    private boolean areInputFieldsEmpty() {
        return pane.getUsernameField().getText().isEmpty() || pane.getPasswordField().getText().isEmpty();
    }

    public void handlePasswordResetButtonAction() {
        String username = pane.getUsernameField().getText();
        String hashedPassword = Hash.getPasswordHashed(pane.getPasswordField().getText());
        String newPassword = pane.getNewPasswordField().getText();
        String repeatPassword = pane.getRepeatPasswordField().getText();
        User user = userHandler.getByPassword(username, hashedPassword);
        String errorKey;
        errorKey = getErrorKey(newPassword, repeatPassword, user, hashedPassword);
        if (errorKey.isEmpty()) {
            loginSucceeded(user, newPassword);
            return;
        }
        setStatusText(errorKey, true, "text-danger");
        System.out.println(pane.getUsernameField().getWidth());
    }

    private String getErrorKey(String newPassword, String repeatPassword, User user, String hashedPassword) {
        if (areInputFieldsEmpty()) {
            return "login.status.empty_fields";
        } else if (!newPassword.equals(repeatPassword)) {
            return "login.status.password.mismatch";
        } else if (!Authentication.doesPasswordComplieToPasswordRules(newPassword)) {
            return "login.status.password.not_rule_conform";
        } else {
            return getLoginResponse(user, hashedPassword);
        }
    }

    private void loginSucceeded(User user, String newPassword) {
        user.setPassword(Hash.getPasswordHashed(newPassword));
        userHandler.update(user);
        setStatusText("login.reset.succeed", true, "text-success");
    }

    private String getLoginResponse(User user, String hashedPassword) {
        if (user == null) {
            return "login.status.user.not_found";
        }
        if (!doesLoginMatch(user, hashedPassword)) {
            return "login.status.failed";
        }
        return "";
    }

    private void setStatusText(String key, boolean visible, String color) {
        Text statusText = pane.getStatusText();
        statusText.textProperty().bind(pane.getValue(key));
        statusText.setVisible(visible);
        statusText.getStyleClass().add(color);
    }

    private boolean doesLoginMatch(User user, String hashedPassword) {
        return user.getPassword().equals(hashedPassword);
    }

    public void handleOpenResetBox() {
        pane.getChangePasswordBox().setVisible(!pane.getChangePasswordBox().isVisible());
    }

    public void handleOnEnter() {
        pane.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (pane.getChangePasswordBox().isVisible()) {
                    handlePasswordResetButtonAction();
                    return;
                }
                handleLoginAction();
            }
            event.consume();
        });
    }

    public void handleLanguageChange(String newLanguage) {
        LanguagePack.setLocale(Locale.forLanguageTag(newLanguage));
    }


    private static String getBindingValue(String key) {
        return LanguagePack.createStringBinding(key).getValue();
    }
}
