import console.dao.*;
import console.functional.EntityManagement;
import console.functional.InputHandler;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.assets.LanguagePack;
import javafx.assets.ScreenBuilder;
import javafx.panes.LoginPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        openChooser();
    }

    public static void main(String[] args) {

    }

    private void openChooser() {
        printOptionlist();
        int i = readInput();
        switch (i) {
            case 1 -> startConsole();
            case 2 -> startUI();
            default -> {
                System.err.println(i + " is not a valid option!");
                openChooser();
            }
        }
    }

    private int readInput() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Integer.parseInt(in.readLine());
        } catch (Exception ignored) {
            System.err.println("Invalid Input!");
            return 0;
        }
    }

    private void printOptionlist() {
        System.out.print("""
                ======================
                [1] <- Console Version
                [2] <- UI Version
                ======================
                """);
    }

    private void startConsole() {
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        UserDao userDao = new UserDaoImpl(entityManager);
        ShareDao shareDao = new ShareDaoImpl(entityManager);
        PortfolioDao portfolioDao = new PortfolioDaoImpl(entityManager);
        new InputHandler(userDao, shareDao, portfolioDao).start();
    }

    private void startUI() {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        config(scene);
        stage.show();
    }

    private void config(Scene scene) {
        configTitle();
        setStartPane(scene);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setScene(scene);
    }

    private void configTitle() {
        setTitle();
        setIcon();
    }

    private void setIcon() {
        stage.getIcons().add(new Image("assets/images/shares_inc._logo.png"));
    }

    private void setTitle() {
        stage.titleProperty().bind(LanguagePack.createStringBinding("text.window.title"));
    }

    private void setStartPane(Scene scene) {
        LoginPane loginPane = new LoginPane(stage, null);
        scene.setRoot(loginPane);
    }
}