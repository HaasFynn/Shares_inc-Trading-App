package javafx.login;

import backend.dao.UserDao;
import backend.dao.UserDaoImpl;
import backend.entities.User;
import jakarta.persistence.EntityManager;
import javafx.assets.Hash;
import javafx.assets.LanguagePack;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Locale;

import backend.functional.EntityManagement;

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
        // TODO: change Scene & Add Pane
    }

    private void openResetWindow() {
        try {
            //resetMain = new ResetMain();
            //resetMain.start(this);
            //resetController = new ResetController(resetMain.scene, employeeHandler);
            disableAllInputs();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        //String username = pane.usernameField.getText();
        //String hashedPassword = Hash.getPasswordHashed(pane.passwordField.getText());
        //String errorMessage = "";
        //if (areInputFieldsEmpty()) {
        //    errorMessage = getValue("empty.fields");
        //} else {
        //    Employee employee = employeeHandler.getByUsername(username);
        //    if (employee == null) {
        //        errorMessage = getValue("employee.not.found");
        //    } else if (!doesLoginMatch(employee, hashedPassword)) {
        //        errorMessage = getValue("login.failed");
        //    }
        //}
        //if (!errorMessage.isEmpty()) {
        //    openStatusWindow(errorMessage);
        //    return;
        //}
        openResetWindow();
    }

    //private boolean doesLoginMatch(Employee employee, String password) {
    //    //return employee.getPassword().equals(Hash.getPasswordHashed(password));
    //}

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
