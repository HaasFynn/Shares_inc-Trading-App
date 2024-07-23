package javafx.controllers;

import console.dao.ShareDaoImpl;
import console.dao.TagDaoImpl;
import console.dao.UserDaoImpl;
import console.entities.Share;
import console.entities.Tag;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.custom_nodes.ShareInfoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.eventlisteners.EventListeners;
import javafx.panes.ShareViewPane;
import javafx.panes.TradePane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Trade controller.
 */
public class TradeController extends CustomController {

    private final TradePane pane;
    private static final int MAX_SHARELIST_LENGTH = 25;
    @Getter
    private final ArrayList<Tag> selectedFilterTags;
    private final UserDaoImpl userDao;
    private final ShareDaoImpl shareDao;
    private final TagDaoImpl tagDao;
    private String username;

    /**
     * Instantiates a new Trade controller.
     *
     * @param stage         the stage
     * @param pane          the pane
     * @param eventListener the event listener
     * @param user          the user
     */
    public TradeController(Stage stage, TradePane pane, EventListeners eventListener, User user) {
        super(stage, eventListener);
        this.pane = pane;
        this.selectedFilterTags = new ArrayList<>();
        this.username = user.getUsername();
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.tagDao = new TagDaoImpl(entityManager);
    }

    /**
     * Gets shares by prompt.
     *
     * @param prompt the prompt
     * @return the shares by prompt
     */
    public ObservableList<ShareInfoBox> getSharesByPrompt(String prompt) {
        return getShareInfoBoxes(shareDao.getByNamePrompt(prompt).toArray(new Share[0]));
    }

    private ObservableList<ShareInfoBox> getEmptyResponse() {
        ShareInfoBox box = new ShareInfoBox(new Share()); //TODO: fix empty Response
        return FXCollections.observableArrayList(box);
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return userDao.getByUsername(username);
    }

    /**
     * Gets share.
     *
     * @param name the name
     * @return the share
     */
    public Share getShare(String name) {
        return shareDao.getByName(name);
    }

    /**
     * Get shares share [ ].
     *
     * @param amountOfShares the amount of shares
     * @return the share [ ]
     */
    public Share[] getShares(int amountOfShares) {
        Share[] shares = shareDao.getAll().toArray(new Share[0]);
        amountOfShares = Math.min(amountOfShares, shares.length);
        return Arrays.copyOfRange(shares, 0, amountOfShares);
    }

    /**
     * Gets share info boxes.
     *
     * @param shares the shares
     * @return the share info boxes
     */
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

    /**
     * Handle search view element selection.
     *
     * @param searchTableView the search table view
     */
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

    /**
     * Gets filter tags.
     *
     * @return the filter tags
     */
    public ArrayList<Tag> getFilterTags() {
        return new ArrayList<>(tagDao.getAll());
    }

    /**
     * Handle text field on prompt change.
     *
     * @param inputField the input field
     */
    public void handleTextFieldOnPromptChange(TextField inputField) {
        inputField.setOnKeyPressed(event -> {
            pane.getSearchTableView().setDisable(false);
            handleSearchProcess();
        });
    }

    /**
     * Gets filter tag by name.
     *
     * @param name the name
     * @return the filter tag by name
     */
    public Tag getFilterTagByName(String name) {
        return tagDao.getByName(name);
    }

    /**
     * Handle check box selection change.
     *
     * @param box the box
     */
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
