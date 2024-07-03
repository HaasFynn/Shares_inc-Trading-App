package javafx.controllers;

import javafx.pages.SideBarPane;
import javafx.stage.Stage;


public class SideBarController extends CustomController {

    private final SideBarPane pane;

    public SideBarController(Stage stage, SideBarPane pane) {
        this.pane = pane;
    }
}
