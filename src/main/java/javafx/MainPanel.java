package javafx;

import console.entities.User;
import javafx.assets.ColorTheme;
import javafx.eventlisteners.EventListenersImpl;
import javafx.panes.CustomPane;
import javafx.panes.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MainPanel extends CustomPane {

    EventListenersImpl eventListeners;
    private final SideBarPane sideBar;
    private CustomPane currentPane;
    private static final double STAGE_WIDTH = 946;
    private static final double STAGE_HEIGHT = 539;

    public MainPanel(Stage stage, User user) {
        super(stage, null, user, ColorTheme.DARK);
        this.eventListeners = new EventListenersImpl(stage, this, user);
        this.currentPane = new DashboardPane(stage, eventListeners, user);
        sideBar = getSideBarPane(stage, user);
        build();
    }

    private SideBarPane getSideBarPane(Stage stage, User user) {
        return new SideBarPane(stage, eventListeners, user);
    }

    HBox box;

    protected void build() {
        box = buildHBox();
        getChildren().add(box);
        if (getStage().isShowing()) {
            adjustWindow();
        }
    }

    @Override
    protected void buildNodes() {
    }

    @Override
    protected void addStyleSheets() {
    }

    private void adjustWindow() {
        getStage().setHeight(STAGE_HEIGHT);
        getStage().setWidth(STAGE_WIDTH);
        getStage().centerOnScreen();
    }

    private HBox buildHBox() {
        HBox box = new HBox();
        box.getChildren().addAll(sideBar, currentPane);
        return box;
    }

    public void switchPage(CustomPane pane) {
        if (classesEqual(pane)) {
            return;
        }
        box.getChildren().remove(currentPane);
        this.currentPane = pane;
        box.getChildren().add(currentPane);
        getScene().setRoot(this);
        System.out.println("Switched Pane to -> " + pane);
    }

    private boolean classesEqual(CustomPane pane) {
        return pane.getClass().equals(currentPane.getClass());
    }
}
