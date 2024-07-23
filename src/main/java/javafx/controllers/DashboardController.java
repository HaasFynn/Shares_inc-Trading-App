package javafx.controllers;

import console.dao.*;
import console.entities.Portfolio;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.custom_nodes.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.eventlisteners.EventListeners;
import javafx.panes.DashboardPane;
import javafx.panes.ShareViewPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * The type Dashboard controller.
 */
public class DashboardController extends CustomController {

    private final DashboardPane pane;
    private final EventListeners eventListeners;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final String username;

    /**
     * Instantiates a new Dashboard controller.
     *
     * @param stage          the stage
     * @param pane           the pane
     * @param eventListeners the event listeners
     * @param user           the user
     */
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

    /**
     * Get top shares share [ ].
     *
     * @param amountOfShares the amount of shares
     * @return the share [ ]
     */
    public Share[] getTopShares(int amountOfShares) {
        Share[] shares = shareDao.getAll().toArray(new Share[0]);
        amountOfShares = Math.min(amountOfShares, shares.length);
        return Arrays.copyOfRange(shares, 0, amountOfShares);
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
     * Gets user portfolio.
     *
     * @param id the id
     * @return the user portfolio
     */
    public List<Portfolio> getUserPortfolio(long id) {
        return portfolioDao.getUserPortfolio(id);
    }

    /**
     * Get share.
     *
     * @param id the id
     * @return the share
     */
    public Share get(long id) {
        return shareDao.get(id);
    }

    /**
     * Gets stock list cell factory.
     *
     * @return the stock list cell factory
     */
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

    /**
     * Handle search view element selection.
     *
     * @param searchTableView the search table view
     */
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

    /**
     * Create string binding string binding.
     *
     * @param supplier the supplier
     * @return the string binding
     */
    public StringBinding createStringBinding(Supplier<String> supplier) {
        return new StringBinding() {
            @Override
            protected String computeValue() {
                return supplier.get() + ".-";
            }
        };
    }

    /**
     * User user.
     *
     * @return the user
     */
    public User user() {
        return userDao.getByUsername(username);
    }

    /**
     * Gets acc balance.
     *
     * @return the acc balance
     */
    public double getAccBalance() {
        double accountBalance = 0;
        if (user() != null) {
            accountBalance = user().getAccountBalance();
        }
        return accountBalance;
    }

    /**
     * Gets price of shares.
     *
     * @return the price of shares
     */
    public double getValueOfShares() {
        List<Portfolio> entries = portfolioDao.getUserPortfolio(user().getId());
        double value = 0;
        for (Portfolio entry : entries) {
            Share share = shareDao.get(entry.getShareId());
            value += share.getPricePerShare() * entry.getAmount();
        }
        return value;
    }

    /**
     * Format number string.
     *
     * @param number the number
     * @return the string
     */
    public String formatNumber(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("###,###.00", symbols);
        return df.format(number);

    }

}
