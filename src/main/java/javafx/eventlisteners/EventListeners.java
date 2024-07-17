package javafx.eventlisteners;

import javafx.assets.ColorTheme;
import javafx.panes.CustomPane;

public interface EventListeners {

    void switchPane(CustomPane pane);

    void changeColor(CustomPane pane, ColorTheme color);

    ColorTheme getColorTheme();
}
