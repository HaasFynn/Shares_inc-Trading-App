package javafx.main_panel;

import javafx.PaneParent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.side_bar.SideBarPane;
import javafx.stage.Stage;

public class MainPanel extends PaneParent {

    public final SideBarPane sideBar;
    public final GridPane currentPage;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;

    public MainPanel(Stage stage, GridPane pane, Font font) {
        super(stage, font);
        this.sideBar = new SideBarPane(stage, font);
        this.currentPage = pane;
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
        box.getChildren().addAll(sideBar, currentPage);
        return box;
    }

}
