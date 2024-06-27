package javafx.pages;

import console.entities.Share;
import console.entities.User;
import javafx.controllers.ShareOverviewController;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class ShareOverviewPane extends CustomPane {

    private final ShareOverviewController controller;
    private Share share;

    public ShareOverviewPane(Stage stage, EventListeners eventListeners, User user, Share share) {
        super(stage, eventListeners, user);
        this.share = share;
        this.controller = new ShareOverviewController(this, user);


        build();
    }

    @Override
    protected void build() {


    }
}
