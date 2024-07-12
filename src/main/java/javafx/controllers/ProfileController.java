package javafx.controllers;

import console.dao.*;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.Authentication;
import javafx.eventlisteners.EventListeners;
import javafx.pages.ProfilePane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ProfileController extends CustomController {
    private final ProfilePane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final long userId;

    public ProfileController(Stage stage, ProfilePane pane, EventListeners eventListeners, User user) {
        super(stage, eventListeners);
        this.userId = user.getId();
        this.pane = pane;

        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
    }

    public User user() {
        return userDao.get(userId);
    }

    public void handleEnterPressed() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveInput();
            }
        });
    }

    public void saveInput() {
        User user = userDao.get(userId);
        user.setUsername(getVerifiedUsername());
        user.setFirstname(getFirstnameInput());
        user.setLastname(getLastNameInput());
        user.setEmail(getVerifiedEmail());
        String password = getVerifiedPassword();
        if (password != null) {
            user.setPassword(password);
        }
        userDao.update(user);
    }

    private String getVerifiedUsername() {
        String usernameInput = pane.getUsernameInput().getInput().getText();
        User user = userDao.getByUsername(usernameInput);
        if (user == null) {
            return usernameInput;
        }
        return user.getUsername();
    }

    private String getFirstnameInput() {
        return pane.getFirstNameInput().getInput().getText();
    }

    private String getLastNameInput() {
        return pane.getLastNameInput().getInput().getText();
    }

    private String getVerifiedEmail() {
        return pane.getEmailInput().getInput().getText();
    }

    private String getVerifiedPassword() {
        String password = pane.getPasswordInput().getText();
        if (Authentication.doesPasswordComplieToPasswordRules(password)) {
            return password;
        }
        System.err.println("Invalid password");
        return null;
    }

}
