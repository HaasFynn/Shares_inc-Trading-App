package javafx.main_panel;

import console.entities.User;
import javafx.eventlisteners.EventListenersImpl;
import javafx.pages.CustomPane;
import javafx.pages.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MainPanel extends CustomPane {

    EventListenersImpl eventListeners;
    private final SideBarPane sideBar;
    private CustomPane currentPane;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;

    public MainPanel(Stage stage, User user) {
        super(stage, null, user);
        this.eventListeners = new EventListenersImpl(stage, this, user);
        this.currentPane = new DashboardPane(stage, eventListeners, user);
        sideBar = getSideBarPane(stage, user);
        build();
    }

    private SideBarPane getSideBarPane(Stage stage, User user) {
        return new SideBarPane(stage, eventListeners, user);
    }


    protected void build() {
        if (getStage().isShowing()) {
            adjustWindow();
        }
    }

    private void adjustWindow() {
        getStage().setHeight(STAGE_HEIGHT);
        getStage().setWidth(STAGE_WIDTH);
        getStage().centerOnScreen();
    }

    public void switchPage(CustomPane pane) {
        if (classesEqual(pane)) {
            return;
        }
        getChildren().remove(currentPane);
        this.currentPane = pane;
        getChildren().add(currentPane);
        System.out.println("Switched Pane to -> " + pane);
    }

    private boolean classesEqual(CustomPane pane) {
        return pane.getClass().equals(currentPane.getClass());
    }
}
