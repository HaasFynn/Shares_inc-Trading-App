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
import javafx.pages.TradePane;

import java.util.ArrayList;
import java.util.List;

public class TradeController {

    private final UserDaoImpl userDao;
    private final ShareDaoImpl shareDao;
    private final TradePane pane;
    private String username;

    public TradeController(TradePane pane, User user) {
        this.pane = pane;
        this.username = user.getUsername();
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
    }

    public ObservableList<ShareInfoBox> getSharesByPrompt(String prompt) {
        return FXCollections.observableArrayList(getShareList(prompt));
    }

    private ArrayList<ShareInfoBox> getShareList(String prompt) {
        ArrayList<ShareInfoBox> dbList = new ArrayList<>();
        shareDao.getByPromptAndTag(prompt, pane.getFilterList().getSelectionModel().getSelectedItem());

        return dbList;
    }

    private ArrayList<ShareInfoBox> getEmptyResponse() {
        ArrayList<ShareInfoBox> dbList;
        ShareInfoBox box = new ShareInfoBox(pane.getValueByKey("no.database.found").get(), 0, false);
        dbList = (ArrayList<ShareInfoBox>) List.of(box);
        return dbList;
    }

    public User getUser() {
        return userDao.getByUsername(username);
    }

    public Share getShare(String name) {
        return shareDao.getByName(name);
    }

    public Share[] getShares() {
        Share[] shares = shareDao.getAll().toArray(new Share[0]);
        for (int i = 0; i < shares.length; i++) {
            if (shares.length <= 25) {
                break;
            }
            shares[shares.length - 1] = null;
        }
        return shares;
    }

    public String getFilterTags() {
        return null;
    }
}
