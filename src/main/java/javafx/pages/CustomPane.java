package javafx.pages;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;

public abstract class CustomPane extends GridPane {
    @Getter
    private final Stage stage;

    protected CustomPane(Stage stage) {
        this.stage = stage;
    }

    protected abstract void build();
}
