package javafx.pages;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class CustomPane extends GridPane {
    public final Stage stage;

    protected CustomPane(Stage stage) {
        this.stage = stage;
    }

    protected abstract void build();
}
