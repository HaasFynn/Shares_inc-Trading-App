package javafx.controllers;

import console.dao.*;
import console.entities.Portfolio;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.eventlisteners.EventListeners;
import javafx.pages.DashboardPane;
import javafx.pages.ShareViewPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class DashboardController extends CustomController {

    private final DashboardPane pane;
    private final EventListeners eventListeners;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final String username;

    public DashboardController(Stage stage, DashboardPane pane, EventListeners eventListeners, User user) {
        super(stage, eventListeners);
        this.pane = pane;
        this.eventListeners = eventListeners;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.username = user.getUsername();
    }

    public Share[] getTopShares(int amountOfShares) {
        Share[] shares = shareDao.getAll().toArray(new Share[0]);
        amountOfShares = Math.min(amountOfShares, shares.length);
        return Arrays.copyOfRange(shares, 0, amountOfShares);
    }

    public User getUser() {
        return userDao.getByUsername(username);
    }

    public List<Portfolio> getUserPortfolio(long id) {
        return portfolioDao.getUserPortfolio(id);
    }

    public Share get(long id) {
        return shareDao.get(id);
    }

    public Callback<ListView<ShareInfoBox>, ListCell<ShareInfoBox>> getStockListCellFactory() {
        Callback<ListView<ShareInfoBox>, ListCell<ShareInfoBox>> cellFactory = new Callback<>() {
            @Override
            public ListCell<ShareInfoBox> call(ListView listView) {
                return getStockListCell();
            }
        };

        return cellFactory;
    }

    private static ListCell<ShareInfoBox> getStockListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ShareInfoBox shareInfoBox, boolean empty) {
                super.updateItem(shareInfoBox, empty);
                if (empty || shareInfoBox == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                HBox item = buildItem(shareInfoBox);
                setGraphic(item);
            }
        };
    }

    private static HBox buildItem(ShareInfoBox shareInfoBox) {
        HBox item = new HBox();
        item.setSpacing(40);
        Text name = new Text(shareInfoBox.getName());
        name.setTextAlignment(TextAlignment.CENTER);
        item.getChildren().addAll(name, shareInfoBox.getRevenue());
        return item;
    }

    public void handleSearchViewElementSelection(ListView<ShareInfoBox> searchTableView) {
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

}
