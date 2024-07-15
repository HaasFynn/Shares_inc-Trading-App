package javafx.eventlisteners;

import console.entities.User;
import javafx.MainPanel;
import javafx.panes.CustomPane;
import javafx.stage.Stage;

public class EventListenersImpl implements EventListeners {

    private final User user;
    private final Stage stage;
    private final MainPanel mainPanel;

    public EventListenersImpl(Stage stage, MainPanel mainPanel, User user) {
        this.user = user;
        this.stage = stage;
        this.mainPanel = mainPanel;
    }

    @Override
    public void switchPane(CustomPane pane) {
        mainPanel.switchPage(pane);
    }

}