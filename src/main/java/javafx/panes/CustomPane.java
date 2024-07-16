package javafx.panes;

import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.eventlisteners.EventListeners;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;

public abstract class CustomPane extends GridPane {
    @Getter
    private final Stage stage;
    protected EventListeners eventListeners;
    private User user;
    protected static double STAGE_WIDTH = 815;
    protected static double STAGE_HEIGHT = 500;
    protected static String STYLE_PATH = "style/";
    protected static final double V_GAP = 10;


    protected CustomPane(Stage stage, EventListeners eventListeners, User user) {
        this.stage = stage;
        this.eventListeners = eventListeners;
        this.user = user;
    }

    protected abstract void build();

    protected abstract void buildNodes();

    protected abstract void addStyleSheets();

    protected void bind(StringProperty text, String key) {
        if (key.isEmpty()) {
            return;
        }
        text.bind(getValueByKey(key));
    }

    protected StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}
