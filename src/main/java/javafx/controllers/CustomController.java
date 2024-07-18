package javafx.controllers;

import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

/**
 * The type Custom controller.
 */
public abstract class CustomController {

    /**
     * The Stage.
     */
    protected final Stage stage;
    /**
     * The Event listeners.
     */
    protected final EventListeners eventListeners;

    /**
     * Instantiates a new Custom controller.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     */
    protected CustomController(Stage stage, EventListeners eventListeners) {
        this.stage = stage;
        this.eventListeners = eventListeners;
    }
}
