package javafx.main_panel;

import backend.entities.User;
import javafx.pages.CustomPane;
import javafx.stage.Stage;

public class SideBarEventListenersImpl implements SideBarEventListeners {

    private final User user;
    private final Stage stage;
    private final MainPanel mainPanel;

    public SideBarEventListenersImpl(Stage stage, MainPanel mainPanel, User user) {
        this.user = user;
        this.stage = stage;
        this.mainPanel = mainPanel;
    }

    @Override
    public void handleIconClick(CustomPane pane) {
        mainPanel.switchPage(pane);
        System.out.println("This is the new Pane: " + pane);
    }

}