package javafx.controllers;

import console.dao.*;
import console.entities.Picture;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.Authentication;
import javafx.eventlisteners.EventListeners;
import javafx.pages.ProfilePane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

public class ProfileController extends CustomController {
    public static final String PROFILE_PICTURE_PATH = "/src/main/resources/assets/profile_pictures";
    public static final String IMG_TYPE = ".png";
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

    public boolean saveImage(Image image) {
        String name = createImageName();
        String filePath = getProfileImagePath(name);
        BufferedImage buffImage = writeBuffImage(image);
        if (buffImage == null) return false;
        if (writeImage(buffImage, filePath)) {
            return saveImagePath(name);
        }
        return false;
    }

    private BufferedImage writeBuffImage(Image image) {
        //TODO: Stackoverflow: https://stackoverflow.com/questions/21540378/convert-javafx-image-to-bufferedimage
        return null;
    }

    private boolean writeImage(BufferedImage image, String name) {
        String filePath = PROFILE_PICTURE_PATH + name + IMG_TYPE;
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(image.toString());
            System.out.println("Image file created successfully at " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving image file. " + filePath);
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveImagePath(String path) {
        return pictureDao.add(new Picture(path, user().getId()));
    }

    private String getProfileImagePath(String name) {
        return System.getProperty("user.dir") + PROFILE_PICTURE_PATH + name + IMG_TYPE;
    }

    /*
     *   Name-creation:
     *  "Profile-Picture" + first 10 letters from uuid + (amount of existing files + 1)
     */
    private String createImageName() {
        String separator = "-";
        String beginning = "profile-picture";
        String uuid = getUUIDValues();
        String fileAmount = String.valueOf(getAmountOfFiles() + 1);

        return String.join(separator, beginning, uuid, fileAmount);
    }

    private static long getAmountOfFiles() {
        try (Stream<Path> path = Files.list(Path.of(PROFILE_PICTURE_PATH))) {
            return path.filter(Files::isRegularFile).count();
        } catch (IOException e) {
            throw new RuntimeException("Was not able to get amount of files...\n\n" + e);
        }
    }

    private String getUUIDValues() {
        char[] uuidArray = UUID.randomUUID().toString().toCharArray();
        StringBuilder sb = new StringBuilder();
        Stream.of(uuidArray).limit(UUID_LENGTH).forEach(sb::append);
        return sb.toString();
    }


    public BufferedImage getProfilePicture() {
        String name = pictureDao.getByUserId(userId).getFileName();
        File file = new File(getProfileImagePath(name));
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean uploadImage() {
        Image image = getImage();
        if (saveImage(image)) {
            pane.getProfileImage().setImage(image);
            return true;
        }
        return false;
    }

    private static boolean isPrimaryButton(MouseEvent mouseEvent) {
        return mouseEvent.getButton() == MouseButton.PRIMARY;
    }

    private Image getImage() {
        JFileChooser chooser = new JFileChooser();
        int status = chooser.showOpenDialog(null);
        if (status != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();
        if (file == null) {
            return null;
        }

        String fileName = file.getAbsolutePath();
        return new Image(fileName);
    }

    public boolean deleteImage() {
        String fileName = pictureDao.getByUserId(userId).getFileName();
        File file = new File(fileName);
        if (file.delete()) {
            pane.getProfileImage().setImage(null);
            return true;
        }
        return false;
    }

    public void handleImageDeletion(MouseEvent click) {
        if (!isPrimaryButton(click)) {
            return;
        }
        if (deleteImage()) {
            System.out.println("Image deleted successfully!");
            return;
        }
        System.out.println("Image could not be deleted!");
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
}
