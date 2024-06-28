package javafx.controllers;

import console.dao.ShareDaoImpl;
import console.dao.TagDaoImpl;
import console.dao.UserDaoImpl;
import console.entities.Share;
import console.entities.Tag;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.ShareInfoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.eventlisteners.EventListeners;
import javafx.pages.ShareViewPane;
import javafx.pages.TradePane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class TradeController {

    private final EventListeners eventListeners;
    private static final int MAX_SHARELIST_LENGTH = 25;
    @Getter
    private final ArrayList<Tag> selectedFilterTags;
    private final UserDaoImpl userDao;
    private final ShareDaoImpl shareDao;
    private final TagDaoImpl tagDao;
    private final TradePane pane;
    private String username;

    public TradeController(TradePane pane, EventListeners eventListener, User user) {
        this.eventListeners = eventListener;
        this.pane = pane;
        this.selectedFilterTags = new ArrayList<>();
        this.username = user.getUsername();
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.tagDao = new TagDaoImpl(entityManager);
    }

    public ObservableList<ShareInfoBox> getSharesByPrompt(String prompt) {
        return getShareInfoBoxes(shareDao.getByNamePrompt(prompt).toArray(new Share[0]));
    }

    private ObservableList<ShareInfoBox> getEmptyResponse() {
        ShareInfoBox box = new ShareInfoBox(new Share()); //TODO: fix empty Response
        return FXCollections.observableArrayList(box);
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
            ShareInfoBox box = new ShareInfoBox(share);
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
            Share share = shareDao.getByName(selectedItem.getName());
            eventListeners.switchPane(new ShareViewPane(pane.getStage(), eventListeners, getUser(), share));
            searchTableView.getSelectionModel().clearSelection();
        });
    }

    public ArrayList<Tag> getFilterTags() {
        return new ArrayList<>(tagDao.getAll());
    }

    public void handleTextFieldOnPromptChange(TextField inputField) {
        inputField.setOnKeyPressed(event -> {
            pane.getSearchTableView().setDisable(false);
            handleSearchProcess();
        });
    }

    public Tag getFilterTagByName(String name) {
        return tagDao.getByName(name);
    }

    public void handleCheckBoxSelectionChange(CheckBox box) {
        box.selectedProperty().addListener(event -> {
            if (box.isSelected()) {
                addToSelectionList(getFilterTagByName(box.getText()));
            } else {
                getSelectedFilterTags().remove(getFilterTagByName(box.getText()));
            }
            handleSearchProcess();
        });
    }

    private void handleSearchProcess() {
        ObservableList<ShareInfoBox> infoBoxes = getSharesByPrompt(pane.getSearchField().getText());
        if (!selectedFilterTags.isEmpty()) {
            infoBoxes = getFilteredShares(infoBoxes);
        }
        setTableViewItems(infoBoxes);
    }

    private void setTableViewItems(ObservableList<ShareInfoBox> filteredShares) {
        pane.getSearchTableView().setItems(filteredShares);
    }

    private ObservableList<ShareInfoBox> getFilteredShares(ObservableList<ShareInfoBox> infoBoxes) {
        return infoBoxes.filtered(infoBox -> selectedFilterTags.stream().anyMatch(tag -> infoBox.getTags().contains(tag)));
    }

    private void addToSelectionList(Tag tag) {
        getSelectedFilterTags().add(tag);
    }
}
