package javafx.main_panel;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.side_bar.SideBarPane;
import javafx.stage.Stage;

import javafx.scene.text.Font;

public class MainPanel extends HBox {

    public final SideBarPane sideBar;
    public final GridPane currentPage;

    public MainPanel(Stage stage, GridPane pane, Font font) {
        this.sideBar = new SideBarPane(stage, font);
        this.currentPage = pane;
        build();
    }

    private void build() {
        getChildren().addAll(sideBar, currentPage);
    }
}
