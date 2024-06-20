package javafx.eventlisteners;

import console.entities.User;
import javafx.main_panel.MainPanel;
import javafx.pages.CustomPane;
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
        System.out.println("This is the new Pane: " + pane);
    }

}