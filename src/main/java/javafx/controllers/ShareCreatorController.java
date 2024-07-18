package javafx.controllers;

import console.creators.ShareCreator;
import console.dao.ShareDao;
import console.dao.ShareDaoImpl;
import console.functional.EntityManagement;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.eventlisteners.EventListeners;
import javafx.panes.ShareCreatorPane;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * The type Share creator controller.
 */
public class ShareCreatorController extends CustomController {
    private final ShareCreatorPane pane;
    private final ShareDao shareHandler;

    /**
     * Instantiates a new Share creator controller.
     *
     * @param stage          the stage
     * @param pane           the pane
     * @param eventListeners the event listeners
     */
    public ShareCreatorController(Stage stage, ShareCreatorPane pane, EventListeners eventListeners) {
        super(stage, eventListeners);
        this.pane = pane;
        this.shareHandler = new ShareDaoImpl(EntityManagement.createEntityManagerFactory().createEntityManager());
    }

    /**
     * Handle on enter.
     */
    public void handleOnEnter() {
        if (pane.getInputField().getText().isEmpty()) {
            setStatusText("share.creator.statusText.number.min", true, "text-danger");
            return;
        }
        try {
            int amount = Integer.parseInt(pane.getInputField().getText());
            if (shareHandler.addAll(ShareCreator.createNewShares(amount))) {
                setStatusText("share.creator.statusText.successfully", true, "text-success");
            }
        } catch (NumberFormatException e) {
            setStatusText("share.creator.statusText.failed", true, "text-danger");
        }
    }

    private void setStatusText(String key, boolean visible, String color) {
        Text statusText = pane.getStatusText();
        statusText.textProperty().bind(getValueByKey(key));
        statusText.setVisible(visible);
        statusText.getStyleClass().add(color);
        hide(statusText).start();
    }

    private Thread hide(Node node) {
        return new Thread(() -> {
            synchronized (node) {
                try {
                    node.wait(5000);
                    node.setVisible(false);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }


    /**
     * Handle input validation.
     *
     * @param oldValue the old price
     * @param newValue the new price
     */
    public void handleInputValidation(String oldValue, String newValue) {
        if (newValue.isEmpty()) return;
        try {
            int amount = Integer.parseInt(pane.getInputField().getText());
            String key;
            if (amount >= 1 && amount <= 100) {
                return;
            }
            key = (amount < 1) ? "share.creator.statusText.number.min" : "share.creator.statusText.number.max";
            pane.getInputField().setText(oldValue);
            setStatusText(key, true, "text-danger");
        } catch (NumberFormatException ignored) {
        }
        if (newValue.matches("\\d*")) return;
        pane.getInputField().setText(newValue.replaceAll("[^\\d]", ""));
    }

    /**
     * Handle pane change.
     */
    public void handlePaneChange() {
        //remove Listeners etc. if needed
        //function is called by main panel
    }

    /**
     * Gets price by key.
     *
     * @param key the key
     * @return the price by key
     */
    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}