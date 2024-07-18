package javafx;

import javafx.application.Application;
import javafx.assets.LanguagePack;
import javafx.assets.ScreenBuilder;
import javafx.panes.LoginPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class SharesInc extends Application {

    public Stage stage;

    @Override
    public void start(Stage stage) {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        this.stage = stage;
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

    private boolean setIcon() {
        return stage.getIcons().add(new Image("assets/images/shares_inc._logo.png"));
    }

    private void setTitle() {
        stage.titleProperty().bind(LanguagePack.createStringBinding("text.window.title"));
    }

    private void setStartPane(Scene scene) {
        LoginPane loginPane = new LoginPane(stage, null);
        scene.setRoot(loginPane);
    }
}