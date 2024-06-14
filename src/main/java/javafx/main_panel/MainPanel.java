package javafx.main_panel;

import backend.entities.User;
import javafx.CustomPane;
import javafx.pages.*;
import javafx.scene.layout.HBox;
import javafx.main_panel.side_bar.SideBarPane;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MainPanel extends CustomPane {

    SideBarEventListenersImpl eventListeners;
    private final SideBarPane sideBar;
    private CustomPane currentPane;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;

    public MainPanel(Stage stage, User user) {
        super(stage);
        this.currentPane = new DashboardPane(stage, user);
        sideBar = getSideBarPane(stage, user);
        build();
    }

    private SideBarPane getSideBarPane(Stage stage, User user) {
        this.eventListeners = new SideBarEventListenersImpl(stage, this, user);
        return new SideBarPane(stage, user, eventListeners);
    }

    HBox box;

    protected void build() {
        box = buildHBox();
        getChildren().add(box);
        if (stage.isShowing()) {
            adjustWindow();
        }
    }

    private void adjustWindow() {
        stage.setHeight(STAGE_HEIGHT);
        stage.setWidth(STAGE_WIDTH);
        stage.centerOnScreen();
        stage.setResizable(true);
    }

    private HBox buildHBox() {
        HBox box = new HBox();
        box.getChildren().addAll(sideBar, currentPane);
        return box;
    }

    public void switchPage(CustomPane pane) {
        box.getChildren().remove(currentPane);
        this.currentPane = pane;
        box.getChildren().add(currentPane);
    }
}
