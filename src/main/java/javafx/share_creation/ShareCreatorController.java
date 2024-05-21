package javafx.share_creation;

import backend.creators.ShareCreator;
import backend.dao.ShareDao;


public class ShareCreatorController {
    private final ShareCreatorPane pane;
    private final ShareDao shareHandler;

    public ShareCreatorController(ShareCreatorPane pane, ShareDao shareHandler) {
        this.pane = pane;
        this.shareHandler = shareHandler;
    }

    public void handleOnEnter() {
        try {
            int amount = Integer.parseInt(pane.inputField.getText());
            if (shareHandler.addAll(ShareCreator.createNewShares(amount))) {
                setStatusText("share.creator.statusText.successfully", true, "text-success");
            }
        } catch (NumberFormatException e) {
            setStatusText("share.creator.statusText.failed", true, "text-danger");
        }
    }

    private void setStatusText(String key, boolean visible, String color) {
        pane.statusText.textProperty().bind(pane.getValueByKey(key));
        pane.statusText.setVisible(visible);
        pane.statusText.getStyleClass().add(color);
    }


    public void handleInputValidation(String oldValue, String newValue) {
        if (newValue.isEmpty()) {
            return;
        }
        try {
            int amount = Integer.parseInt(pane.inputField.getText());
            String key;
            if (amount >= 1 && amount <= 100) {
                return;
            }
            key = (amount < 1) ? "share.creator.statusText.number.min" : "share.creator.statusText.number.max";
            pane.inputField.setText(oldValue);
            setStatusText(key, true, "text-danger");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (newValue.matches("\\d*")) return;
        pane.inputField.setText(newValue.replaceAll("[^\\d]", ""));
    }

    public void handlePaneChange() {
        //remove Listeners etc. if needed
        //function is called by main panel
    }

}