package javafx;

import console.dao.UserDaoImpl;
import console.entities.User;
import console.functional.EntityManagement;
import javafx.application.Application;
import javafx.assets.LanguagePack;
import javafx.assets.ScreenBuilder;
import javafx.main_panel.MainPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class SharesInc extends Application {

    public Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        this.stage = stage;
        config(scene);
        stage.show();
    }

    private void config(Scene scene) {
        stage.titleProperty().bind(LanguagePack.createStringBinding("window.title"));
        stage.getIcons().add(new Image("assets/images/shares_inc._logo.png"));
        //LoginPane loginpane = new LoginPane(stage, null); //Standard
        User user = new UserDaoImpl(EntityManagement.createEntityManagerFactory().createEntityManager()).get(652);
        MainPanel mainPanel = new MainPanel(stage, user);
        scene.setRoot(mainPanel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setScene(scene);
    }
}