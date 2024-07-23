package javafx.panes;

import console.entities.User;
import javafx.assets.ColorTheme;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.eventlisteners.EventListeners;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.IntStream;

/**
 * The type Custom pane.
 */
public abstract class CustomPane extends GridPane {
    @Getter
private final Stage stage;
protected EventListeners eventListeners;
private User user;
protected static double STAGE_WIDTH = 815;
protected static double STAGE_HEIGHT = 500;
protected static String STYLE_PATH = "style/";
protected static final double V_GAP = 10;
protected String[] styleClasses;

    @Setter
    @Getter
    protected ColorTheme colorTheme;


    /**
     * Instantiates a new Custom pane.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     * @param user           the user
     * @param colorTheme     the color theme
     */
    protected CustomPane(Stage stage, EventListeners eventListeners, User user, ColorTheme colorTheme) {
        this.stage = stage;
        this.eventListeners = eventListeners;
        this.user = user;
        this.colorTheme = colorTheme;
    }

    /**
     * Build.
     */
    protected abstract void build();

    /**
     * Build nodes.
     */
    protected abstract void buildNodes();

    /**
     * Add style sheets.
     */
    protected void addStyleSheets() {
        IntStream.range(0, styleClasses.length).forEach(i -> getStylesheets().add(STYLE_PATH + styleClasses[i]));
    }

    /**
     * Bind.
     *
     * @param text the text
     * @param key  the key
     */
    public void bind(StringProperty text, String key) {
        if (key.isEmpty()) {
            return;
        }
        text.bind(getValueByKey(key));
    }

    /**
     * Gets price by key.
     *
     * @param key the key
     * @return the price by key
     */
    protected StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

    public String toString() {
        return getClass().getName();
    }
}
