package javafx;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public abstract class PaneParent extends GridPane {
    public final Stage stage;
    public final Font font;
    public double stageHeight;
    public double stageWidth;

    protected PaneParent(Stage stage, Font font) {
        this.stage = stage;
        this.font = font;
    }

    protected abstract void build();
}
