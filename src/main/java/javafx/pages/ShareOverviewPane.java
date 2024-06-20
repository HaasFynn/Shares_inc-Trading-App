package javafx.pages;

import console.entities.Share;
import console.entities.User;
import javafx.controllers.ShareOverviewController;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class ShareOverviewPane extends CustomPane {

    private final ShareOverviewController controller;
    private Share share;

    protected ShareOverviewPane(Stage stage, User user, Share share) {
        super(stage);
        this.share = share;
        this.controller = new ShareOverviewController(this, user);


        build();
    }

    @Override
    protected void build() {


    }
}
