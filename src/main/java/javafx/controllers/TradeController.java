package javafx.controllers;

import backend.dao.ShareDao;
import backend.dao.ShareDaoImpl;
import backend.dao.UserDaoImpl;
import backend.entities.User;
import backend.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.ShareInfoBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.pages.TradePane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TradeController {

    private final UserDaoImpl userDao;
    private final ShareDaoImpl shareDao;
    private final TradePane pane;
    private User user;

    public TradeController(TradePane pane, User user) {
        this.pane = pane;
        this.user = user;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
    }

    public ObservableList<ShareInfoBox> getListViewItems(String prompt) {
        return FXCollections.observableArrayList(getShareList(prompt));
    }

    private ArrayList<ShareInfoBox> getShareList(String prompt) {
        ArrayList<ShareInfoBox> dbList = new ArrayList<>();
        //TODO: Search with prompt
        if (dbList.isEmpty()) {
            ShareInfoBox box = new ShareInfoBox(pane.getValueByKey("no.database.found").get(), 0, false);
            dbList = (ArrayList<ShareInfoBox>) List.of(box);
        }
        return dbList;
    }
}
