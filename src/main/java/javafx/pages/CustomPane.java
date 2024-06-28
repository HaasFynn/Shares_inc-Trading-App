package javafx.pages;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public abstract class CustomPane extends GridPane {
    @Getter
    private final Stage stage;
    protected EventListeners eventListeners;
    private User user;
    protected static double STAGE_WIDTH = 815;
    protected static double STAGE_HEIGHT = 500;
    protected static String STYLE_PATH = "style/";

    protected CustomPane(Stage stage, EventListeners eventListeners, User user) {
        this.stage = stage;
        this.eventListeners = eventListeners;
        this.user = user;
    }

    protected abstract void build();
}
