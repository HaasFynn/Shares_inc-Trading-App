package javafx.controllers;

import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

public abstract class CustomController {

    protected final Stage stage;
    protected final EventListeners eventListeners;

    protected CustomController(Stage stage, EventListeners eventListeners) {
        this.stage = stage;
        this.eventListeners = eventListeners;
    }
}
