package javafx.share_creation;

import javafx.ScreenBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class temp_ShareCreatorMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        scene.setRoot(new ShareCreatorPane(Font.font("Verdana")));
        editStage(stage, scene);
        stage.show();
    }

    public void start(Font font) {
        Stage stage = new Stage();
        Region sceneRoot = new ScreenBuilder().build();
        Scene scene = new Scene(sceneRoot);
        scene.setRoot(new ShareCreatorPane(font));
        editStage(stage, scene);
        stage.show();
    }

    private void editStage(Stage stage, Scene scene) {
        stage.setWidth(815);
        stage.setHeight(500);
        stage.setScene(scene);
    }
}