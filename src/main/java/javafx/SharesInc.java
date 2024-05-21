package javafx;

import javafx.assets.ScreenBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.share_creation.ShareCreatorPane;
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
        stage.setWidth(815);
        stage.setHeight(500);
        stage.setScene(scene);
        scene.setRoot(new ShareCreatorPane("Verdana", scene) /*new LoginPane("Verdana", scene)*/);
    }
}