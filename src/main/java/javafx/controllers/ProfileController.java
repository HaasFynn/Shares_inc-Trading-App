package javafx.controllers;

import console.dao.*;
import console.entities.Picture;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.Authentication;
import javafx.eventlisteners.EventListeners;
import javafx.panes.ProfilePane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ProfileController extends CustomController {
    private static final String URL_PROTOCOL = "file://";
    private static final String PROFILE_PICTURE_PATH = "C:/Users/fhaas/Documents/Ergon/JavaFx/Shares-inc.-Trading-App/src/main/resources/assets/images/profile_pictures/";
    private static final String IMG_TYPE = "png";
    private final ProfilePane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final PictureDao pictureDao;
    private final long userId;
    private final static int UUID_LENGTH = 10;

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
        String email = pane.getEmailInput().getInput().getText();
        if (Authentication.isValidEmail(email)) {
            return email;
        }
        System.err.println("E-Mail is not valid!");
        return null;
    }

    private String getVerifiedPassword() {
        String password = pane.getPasswordInput().getText();
        if (Authentication.doesPasswordComplieToPasswordRules(password)) {
            return password;
        }
        System.err.println("Invalid password");
        return null;
    }

    public boolean saveImage(File file) {
        String name = createImageName();
        if (pictureDao.getByName(name) != null) {
            return false;
        }
        if (writeImage(file, name)) {
            return saveImageName(name);
        }
        return false;
    }

    private boolean writeImage(File file, String name) {
        String path = PROFILE_PICTURE_PATH + name + "." + IMG_TYPE;
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                return false;
            }
            File destination = new File(path);
            if (!destination.getParentFile().exists()) {
                destination.getParentFile().mkdirs();
            }
            System.out.println(destination.getAbsolutePath());

            return ImageIO.write(image, IMG_TYPE, destination);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean saveImageName(String name) {
        Picture picture = pictureDao.getByUserId(userId);
        picture.setFileName(name);
        if (pictureDao.getByUserId(userId) != null) {
            return pictureDao.update(picture);
        }
        return pictureDao.add(picture);
    }

    /*
     *   Name-creation:
     *  (amount of existing files + 1) + "Profile-Picture" + first 8 letters from uuid
     */
    private String createImageName() {
        String separator = "-";
        String index = String.valueOf(getAmountOfFiles() + 1);
        String title = "profile-picture";
        String uuid = getUUIDValues();

        return String.join(separator, index, title, uuid);
    }

    private static long getAmountOfFiles() {
        try (Stream<Path> path = Files.list(Path.of(PROFILE_PICTURE_PATH))) {
            return path.count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String getUUIDValues() {
        char[] uuidArray = UUID.randomUUID().toString().toCharArray();
        return IntStream.range(0, 8).mapToObj(i -> String.valueOf(uuidArray[i])).collect(Collectors.joining());
    }

    public boolean uploadImage() {
        File file = getImageAsFile();
        if (file == null) {
            return false;
        }
        if (!saveImage(file)) {
            return false;
        }
        String URI = file.toURI().toString();
        Image image = new Image(URI);
        pane.getProfileImage().setImage(image);
        return true;
    }

    private static boolean isPrimaryButton(MouseEvent mouseEvent) {
        return mouseEvent.getButton() == MouseButton.PRIMARY;
    }

    private File getImageAsFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("img files (*.png, *.jpg)", "png", "jpg");
        chooser.setFileFilter(filter);
        int status = chooser.showOpenDialog(null);
        if (status != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();
    }

    public boolean deleteImage() {
        String fileName = pictureDao.getByUserId(userId).getFileName();
        File file = new File(PROFILE_PICTURE_PATH + fileName + "." + IMG_TYPE);
        if (file.delete()) {
            pane.getProfileImage().setImage(null);
            return true;
        }
        return false;
    }

    public void handleImageUpload(MouseEvent click) {
        if (!isPrimaryButton(click)) {
            return;
        }
        if (uploadImage()) {
            System.out.println("Image uploaded successfully!");
            return;
        }
        System.out.println("Image could not be uploaded!");
    }

    public void handleImageDeletion(MouseEvent click) {
        if (!isPrimaryButton(click) | isImageNull()) {
            return;
        }
        if (deleteImage()) {
            System.out.println("Image deleted successfully!");
            return;
        }
        System.out.println("Image could not be deleted!");
    }

    private boolean isImageNull() {
        return pane.getProfileImage().getImage() == null;
    }

    public Image getUserProfile() {
        Picture picture = pictureDao.getByUserId(userId);
        if (picture != null) {
            try {
                File file = new File(PROFILE_PICTURE_PATH + picture.getFileName() + "." + IMG_TYPE);
                return new Image(file.toURI().toString());
            } catch (IllegalArgumentException e) {
                System.out.println("User does not have Profile-Picture...");
            }
        }
        return null;
    }
}