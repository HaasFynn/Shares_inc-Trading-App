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

public class SharesInc extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        config(stage, scene);
        stage.show();
    }

    private void config(Stage stage, Scene scene) {
        configStage(stage, scene);
        configScene(scene);
    }

    private static void configStage(Stage stage, Scene scene) {
        stage.titleProperty().bind(LanguagePack.createStringBinding("title"));
        stage.getIcons().add(new Image("image/shares_inc._logo.png"));
        stage.setWidth(815);
        stage.setHeight(500);
        stage.setScene(scene);
    }

    private static void configScene(Scene scene) {
        scene.setRoot(new LoginPane(Font.font("Verdana")));
    }
}