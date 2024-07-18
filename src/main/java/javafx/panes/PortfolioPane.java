package javafx.panes;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

/**
 * The type Portfolio pane.
 */
public class PortfolioPane extends CustomPane {

    /**
     * Instantiates a new Portfolio pane.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     * @param user           the user
     */
    public PortfolioPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user, eventListeners.getColorTheme());
        styleClasses = new String[]{
                "portfolio.css"
        };
    }

    @Override
    protected void build() {

    }

    @Override
    protected void buildNodes() {

    }

    @Override
    protected void addStyleSheets() {

    }

}
