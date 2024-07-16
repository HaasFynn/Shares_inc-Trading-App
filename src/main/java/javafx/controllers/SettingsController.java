package javafx.controllers;

import console.dao.UserDao;
import console.dao.UserDaoImpl;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.LanguagePack;
import javafx.eventlisteners.EventListeners;
import javafx.panes.LoginPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Locale;

public class SettingsController extends CustomController {

    private final long userId;
    private final UserDao userDao;

    public SettingsController(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners);
        this.userId = user.getId();
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
    }


    public void handleLanguageChange(String language) {
        LanguagePack.setLocale(Locale.forLanguageTag(language));
    }

    public void handleAccountDeletion(MouseEvent click) {
        if (!(click.getButton() == MouseButton.PRIMARY) | click.getClickCount() != 2) {
            return;
        }
        User user = userDao.get(userId);
        if (userDao.delete(user)) {
            openLoginPane();
            System.out.println("User deleted successfully!");
            return;
        }
        System.out.println("User could not be deleted!");
    }

    private void openLoginPane() {
        stage.getScene().setRoot(new LoginPane(stage, eventListeners));
    }
}
