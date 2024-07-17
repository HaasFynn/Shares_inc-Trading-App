package javafx.panes;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

public class PortfolioPane extends CustomPane {

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
