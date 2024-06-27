package javafx.pages;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

public class ProfilePane extends CustomPane {
    public ProfilePane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user);
    }

    @Override
    protected void build() {

    }
}
