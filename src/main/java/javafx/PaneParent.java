package javafx;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class PaneParent extends GridPane {
    public final Stage stage;

    protected PaneParent(Stage stage) {
        this.stage = stage;
    }

    protected abstract void build();
}
