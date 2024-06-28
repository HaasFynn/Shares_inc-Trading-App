package javafx.pages;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

public class StockMarketPane extends CustomPane {
    public StockMarketPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user);
    }

    @Override
    protected void build() {

    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
