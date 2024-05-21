package javafx.login;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class LoginPane extends GridPane {

    private final Scene scene;
    private final Font font;


    public LoginPane(String font, Scene scene) {
        this.scene = scene;
        this.font = Font.font(font);
        create();
    }

    private void create() {
        setMinSize(200, 150);
        setVgap(10);
        //addListeners();
        //add(getMainBox(), 0, 0);
    }


}
