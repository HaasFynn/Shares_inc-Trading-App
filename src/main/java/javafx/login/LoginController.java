package javafx.login;

import backend.dao.UserDao;
import backend.dao.UserDaoImpl;
import backend.entities.User;
import backend.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.Controller;
import javafx.assets.Hash;
import javafx.assets.LanguagePack;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.share_creation.ShareCreatorPane;

import java.util.Locale;

public class LoginController extends Controller {
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
            setStatusText("login.status.user.not.found", true, "text-danger");
            return;
        }
        pane.getScene().setRoot(new ShareCreatorPane(pane.stage, pane.font)); //Changes Pane TODO Change to MainPanel
    }

    private boolean areInputFieldsEmpty() {
        return pane.usernameField.getText().isEmpty() || pane.passwordField.getText().isEmpty();
    }

    public void handlePasswordResetButtonAction() {
        String username = pane.usernameField.getText();
        String hashedPassword = Hash.getPasswordHashed(pane.passwordField.getText());
        String newPassword = pane.newPasswordField.getText();
        String repeatPassword = pane.repeatPasswordField.getText();
        User user = userHandler.getByPassword(username, hashedPassword);
        String errorKey;
        errorKey = getErrorKey(newPassword, repeatPassword, user, hashedPassword);
        if (errorKey.isEmpty()) {
            loginSucceeded(user, newPassword);
            return;
        }
        setStatusText(errorKey, true, "text-danger");
        System.out.println(pane.usernameField.getWidth());
    }

    private String getErrorKey(String newPassword, String repeatPassword, User user, String hashedPassword) {
        if (areInputFieldsEmpty()) {
            return "login.status.empty.fields";
        } else if (!newPassword.equals(repeatPassword)) {
            return "login.status.password.mismatch";
        } else if (!doesPasswordComplieToPasswordRules(newPassword)) {
            return "login.status.password.notRuleConform";
        } else {
            return getLoginResponse(user, hashedPassword);
        }
    }

    private void loginSucceeded(User user, String newPassword) {
        user.setPassword(Hash.getPasswordHashed(newPassword));
        userHandler.update(user);
        setStatusText("login.reset.succeed", true, "text-success");
    }

    private boolean doesPasswordComplieToPasswordRules(String newPassword) {
        return isPW8DigitsLong(newPassword) && doesPWContainRightLetters(newPassword) && doesPWContainSpecialCharacters(newPassword);
    }

    private static boolean doesPWContainSpecialCharacters(String password) {
        char[] passwordChar = password.toCharArray();

        for (char letter : passwordChar) {
            if (!Character.isAlphabetic(letter) && !Character.isDigit(letter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean doesPWContainRightLetters(String password) {
        return (!password.equals(password.toLowerCase()) && !password.equals(password.toUpperCase()));
    }

    private static boolean isPW8DigitsLong(String password) {
        char[] passwordLength = password.toCharArray();
        return passwordLength.length >= 8;
    }

    private String getLoginResponse(User user, String hashedPassword) {
        if (user == null) {
            return "login.status.user.not.found";
        }
        if (!doesLoginMatch(user, hashedPassword)) {
            return "login.status.failed";
        }
        return "";
    }

    private void setStatusText(String key, boolean visible, String color) {
        pane.statusText.textProperty().bind(pane.getValue(key));
        pane.statusText.setVisible(visible);
        pane.statusText.getStyleClass().add(color);
    }

    private boolean doesLoginMatch(User user, String hashedPassword) {
        return user.getPassword().equals(hashedPassword);
    }

    public void handleOpenResetBox() {
        pane.changePasswordBox.setVisible(!pane.changePasswordBox.isVisible());
    }

    public void handleOnEnter() {
        pane.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (pane.changePasswordBox.isVisible()) {
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
