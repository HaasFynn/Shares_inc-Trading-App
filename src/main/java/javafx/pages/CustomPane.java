package javafx.pages;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;

public abstract class CustomPane extends GridPane {
    @Getter
    private final Stage stage;
    private EventListeners eventListeners;
    private User user;
    protected CustomPane(Stage stage, EventListeners eventListeners, User user) {
        this.stage = stage;
        this.eventListeners = eventListeners;
        this.user = user;
    }

    protected abstract void build();
}
