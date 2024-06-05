package javafx;

import backend.entities.User;
import javafx.application.Application;
import javafx.assets.LanguagePack;
import javafx.assets.ScreenBuilder;
import javafx.dashboard.DashboardPane;
import javafx.login.LoginPane;
import javafx.main_panel.MainPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class SharesInc extends Application {

    public Stage stage;
    Font font = Font.font("Verdana");
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
        stage.getIcons().add(new Image("image/shares_inc._logo.png"));
        //LoginPane pane = new LoginPane(stage, font); //Standard
        //ShareCreatorPane pane = new ShareCreatorPane(stage, font);
        User user = new User();
        user.setUsername("fhaas");
        DashboardPane pane = new DashboardPane(stage, font, user);
        MainPanel mainPanel = new MainPanel(stage, pane, font);
        scene.setRoot(mainPanel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
    }

}