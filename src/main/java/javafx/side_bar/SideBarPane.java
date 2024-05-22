package javafx.side_bar;

import javafx.PaneParent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SideBarPane extends PaneParent {

    private final SideBarController controller;

    public SideBarPane(Stage stage, Font font) {
        super(stage, font);
        this.controller = new SideBarController();
        build();
    }

    @Override
    protected void build() {

    }
}
