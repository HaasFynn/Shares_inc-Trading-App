package javafx.controllers;

import console.dao.UserDao;
import console.dao.UserDaoImpl;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.ColorTheme;
import javafx.assets.LanguagePack;
import javafx.eventlisteners.EventListeners;
import javafx.panes.LoginPane;
import javafx.panes.SettingsPane;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Locale;

public class SettingsController extends CustomController {

    private final SettingsPane pane;
    private final long userId;
    private final UserDao userDao;

    public SettingsController(Stage stage, SettingsPane pane, EventListeners eventListeners, User user) {
        super(stage, eventListeners);
        this.userId = user.getId();
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.pane = pane;
    }


    public void handleLanguageChange(String language) {
        LanguagePack.setLocale(Locale.forLanguageTag(language));
    }

    public void handleAccountDeletion(MouseEvent click) {
        if (!(click.getButton() == MouseButton.PRIMARY) | click.getClickCount() != 2) {
            return;
        }
        pane.getInsuranceButton().setVisible(true);
    }

    private void openLoginPane() {
        stage.getScene().setRoot(new LoginPane(stage, eventListeners));
    }

    public void handleThemeChange(RadioButton button, ColorTheme color) {
        if (button.isSelected()) {
            pane.setColorTheme(color);
        }
        System.out.println("Theme switched to: " + color);
    }

    public void handleAccountInsurance(MouseEvent click) {
        if (!isPrimaryButton(click) || click.getClickCount() != 2) {
            return;
        }
        deleteAccount();
    }

    private void deleteAccount() {
        User user = userDao.get(userId);
        if (userDao.delete(user)) {
            openLoginPane();
            System.out.println("User deleted successfully!");
            return;
        }
        System.out.println("User could not be deleted!");
    }

    private static boolean isPrimaryButton(MouseEvent mouseEvent) {
        return mouseEvent.getButton() == MouseButton.PRIMARY;
    }
}
