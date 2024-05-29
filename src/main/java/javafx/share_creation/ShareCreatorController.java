package javafx.share_creation;

import backend.creators.ShareCreator;
import backend.dao.ShareDao;
import backend.dao.ShareDaoImpl;
import backend.functional.EntityManagement;
import javafx.Controller;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class ShareCreatorController extends Controller {
    private final ShareCreatorPane pane;
    private final ShareDao shareHandler;

    public ShareCreatorController(ShareCreatorPane pane) {
        this.pane = pane;
        this.shareHandler = new ShareDaoImpl(EntityManagement.createEntityManagerFactory().createEntityManager());
    }

    public void handleOnEnter() {
        if (pane.inputField.getText().isEmpty()) {
            setStatusText("share.creator.statusText.number.min", true, "text-danger");
            return;
        }
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
        pane.statusText.textProperty().bind(getValueByKey(key));
        pane.statusText.setVisible(visible);
        pane.statusText.getStyleClass().add(color);
    }


    public void handleInputValidation(String oldValue, String newValue) {
        if (newValue.isEmpty()) return;
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
    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}