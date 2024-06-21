package javafx.controllers;

import console.dao.ShareDaoImpl;
import console.dao.UserDaoImpl;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.ShareInfoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.eventlisteners.EventListeners;
import javafx.eventlisteners.EventListenersImpl;
import javafx.pages.ShareOverviewPane;
import javafx.pages.TradePane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradeController {

    private final EventListeners eventListeners;
    private static final int MAX_SHARELIST_LENGTH = 25;
    private final UserDaoImpl userDao;
    private final ShareDaoImpl shareDao;
    private final TradePane pane;
    private String username;

    public TradeController(TradePane pane, EventListeners eventListener, User user) {
        this.eventListeners = eventListener;
        this.pane = pane;
        this.username = user.getUsername();
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
    }

    public ObservableList<ShareInfoBox> getSharesByPrompt(String prompt) {
        return getShareInfoBoxes(shareDao.getByNamePrompt(prompt).toArray(new Share[0]));
    }

    private ObservableList<ShareInfoBox> getEmptyResponse() {
        ShareInfoBox box = new ShareInfoBox(pane.getValueByKey("trade.error.no_entry_found").get(), 0, false);
        return FXCollections.observableArrayList(List.of(box));
    }

    public User getUser() {
        return userDao.getByUsername(username);
    }

    public Share getShare(String name) {
        return shareDao.getByName(name);
    }

    public Share[] getShares(int amountOfShares) {
        Share[] shares = shareDao.getAll().toArray(new Share[0]);
        amountOfShares = Math.min(amountOfShares, shares.length);
        return Arrays.copyOfRange(shares, 0, amountOfShares);
    }

    public ObservableList<ShareInfoBox> getShareInfoBoxes(Share[] shares) {
        ArrayList<ShareInfoBox> infoBoxes = new ArrayList<>();
        if (shares.length == 0) {
            return getEmptyResponse();
        }

        for (Share share : shares) {
            ShareInfoBox box = new ShareInfoBox(share.getName());
            infoBoxes.add(box);
        }

        return FXCollections.observableArrayList(infoBoxes);
    }

    public void handleSearchViewElementSelection(TableView<ShareInfoBox> searchTableView) {
        searchTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() != 2) {
                return;
            }
            ShareInfoBox selectedItem = searchTableView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            Share share = getShare(selectedItem.getShareName());
            eventListeners.switchPane(new ShareOverviewPane(pane.getStage(), getUser(), share));
            searchTableView.getSelectionModel().clearSelection();
        });
    }

    public String getFilterTags() {
        return null;
    }

    public void handleTextFieldOnEnter(TextField inputField) {
        inputField.setOnKeyPressed(event -> {
            pane.getSearchTableView().setItems(getSharesByPrompt(inputField.getText()));
        });
    }
}
