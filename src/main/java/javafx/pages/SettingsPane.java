package javafx.pages;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

public class SettingsPane extends CustomPane {
    public SettingsPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage,eventListeners, user);
    }

    @Override
    protected void build() {

    }
}
