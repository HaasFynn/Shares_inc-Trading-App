package javafx.controllers;

import console.dao.*;
import console.entities.Picture;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.Authentication;
import javafx.eventlisteners.EventListeners;
import javafx.pages.ProfilePane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;

public class ProfileController extends CustomController {
    private final ProfilePane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final PictureDao pictureDao;
    private final long userId;

    public ProfileController(Stage stage, ProfilePane pane, EventListeners eventListeners, User user) {
        super(stage, eventListeners);
        this.userId = user.getId();
        this.pane = pane;

        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.pictureDao = new PictureDaoImpl(entityManager);
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

    public boolean saveImage(BufferedImage image) {
        String name = createImageName();
        String filePath = getProfileImagePath(name);
        if (writeImage(image, filePath)) {
            saveImagePath(filePath);
            return true;
        }
        return false;
    }

    private boolean writeImage(BufferedImage image, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(image.toString());
            System.out.println("Image file created successfully at " + path);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving image file.");
            e.printStackTrace();
            return false;
        }
    }

    private void saveImagePath(String path) {
        pictureDao.add(new Picture(path, user().getId()));
    }

    private String getProfileImagePath(String name) {
        return System.getProperty("user.dir") + "/src/main/resources/assets/profile_pictures" + name;
    }

    private String createImageName() {
        return ""; //TODO: reads amount of files + a shortl for images
    }


    public Picture getProfilePicture() {
        return pictureDao.getByUserId(user().getId());
    }

}
