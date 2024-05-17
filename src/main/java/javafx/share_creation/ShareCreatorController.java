package javafx.share_creation;

import backend.creators.ShareCreator;
import backend.dao.ShareDao;


public class ShareCreatorController {
    private final ShareCreatorPane scene;
    private final ShareDao shareHandler;

    public ShareCreatorController(ShareCreatorPane scene, ShareDao shareHandler) {
        this.scene = scene;
        this.shareHandler = shareHandler;
    }

    public void handleOnEnter() {
        int amount = Integer.parseInt(scene.inputField.getText());
        if (shareHandler.addAll(ShareCreator.createNewShares(amount))) {
            //Change A Text to "Share Creation Successfully"
        }
    }


}