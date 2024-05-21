package javafx;

import javafx.assets.ScreenBuilder;
import javafx.application.Application;
import javafx.login.LoginPane;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.share_creation.ShareCreatorPane;
import javafx.stage.Stage;

public class SharesInc extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        scene.setRoot(new ShareCreatorPane("Verdana", scene) /*new LoginPane("Verdana", scene)*/);
        editStage(stage, scene);
        stage.show();
    }

    private void editStage(Stage stage, Scene scene) {
        stage.setWidth(815);
        stage.setHeight(500);
        stage.setScene(scene);
    }
}