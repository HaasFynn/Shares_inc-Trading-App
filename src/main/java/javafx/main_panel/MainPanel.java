package javafx.main_panel;

import javafx.PaneParent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.side_bar.SideBarPane;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MainPanel extends PaneParent {

    private final SideBarPane sideBar;
    private final GridPane page;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;

    public MainPanel(Stage stage, GridPane pane) {
        super(stage);
        this.sideBar = new SideBarPane(stage);
        this.page = pane;
        build();
    }

    HBox box;

    protected void build() {
        box = buildHBox();
        getChildren().add(box);
        adjustWindow();
    }

    private void adjustWindow() {
        stage.setHeight(STAGE_HEIGHT);
        stage.setWidth(STAGE_WIDTH);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    private HBox buildHBox() {
        HBox box = new HBox();
        box.getChildren().addAll(sideBar, page);
        return box;
    }

}
