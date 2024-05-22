package javafx;

import javafx.assets.LanguagePack;
import javafx.assets.ScreenBuilder;
import javafx.application.Application;
import javafx.login.LoginPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class SharesInc extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        config(stage, scene);
    }

    private void config(Stage stage, Scene scene) {
        configStage(stage, scene);
    }

    private static void configStage(Stage stage, Scene scene) {
        stage.titleProperty().bind(LanguagePack.createStringBinding("window.title"));
        stage.getIcons().add(new Image("image/shares_inc._logo.png"));
        LoginPane pane = new LoginPane(stage, Font.font("Verdana"));
        scene.setRoot(pane);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
    }

}