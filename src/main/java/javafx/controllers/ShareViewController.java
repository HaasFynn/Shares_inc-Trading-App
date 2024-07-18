package javafx.controllers;

import console.dao.*;
import console.entities.Portfolio;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.eventlisteners.EventListeners;
import javafx.panes.ShareViewPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;

/**
 * The type Share view controller.
 */
public class ShareViewController extends CustomController {

    private final ShareViewPane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final User user;
    private final Share share;

    /**
     * Instantiates a new Share view controller.
     *
     * @param stage          the stage
     * @param pane           the pane
     * @param eventListeners the event listeners
     * @param share          the share
     * @param user           the user
     */
    public ShareViewController(Stage stage, ShareViewPane pane, EventListeners eventListeners, Share share, User user) {
        super(stage, eventListeners);
        this.pane = pane;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);

        this.share = share;
        this.user = user;
    }

    /**
     * Share share.
     *
     * @return the share
     */
    public Share share() {
        return shareDao.get(share.getId());
    }

    /**
     * User user.
     *
     * @return the user
     */
    public User user() {
        return userDao.get(user.getId());
    }

    /**
     * Add integer filter.
     *
     * @param spinner the spinner
     */
    public void addIntegerFilter(Spinner<Integer> spinner) {
        addFilter(spinner);
        addSpinnerListener(spinner);
    }

    private void addFilter(Spinner<Integer> spinner) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([0-9]+)?")) {
                return change;
            }
            return null;
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(integerFilter);
        spinner.getEditor().setTextFormatter(textFormatter);
    }

    private void addSpinnerListener(Spinner<Integer> spinner) {
        spinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    spinner.getValueFactory().setValue(value);
                } catch (NumberFormatException e) {
                    spinner.getEditor().setText(oldValue); // Reset to old price if not valid
                }
            }
        });
    }

    /**
     * Buy.
     */
    public void buy() {
        if (share() == null) {
            System.out.println("No Such Share"); //TODO: Change to StatusText
            return;
        }
        int amountOfShares = pane.getShareAmountSpinner().getValue();
        if (amountOfShares <= 0) {
            System.out.println("Invalid number of shares"); //TODO: Change to StatusText
            return;
        }
        if (userHasEnoughMoney(amountOfShares)) {
            System.out.println("Insufficient Funds"); //TODO: Change to StatusText
            return;
        }
        if (!addToPortfolio(share.getId(), amountOfShares)) {
            System.out.println("Sorry share could not be purchased"); //TODO: Change to StatusText
        } else {
            withdrawMoney();
            System.out.println("Successfully purchased!"); //TODO: Change to StatusText
        }
    }

    private boolean userHasEnoughMoney(int amountOfShares) {
        return user().getAccountBalance() - (share().getPricePerShare() * amountOfShares) < 0;
    }

    private boolean addToPortfolio(long shareId, int amount) {
        if (portfolioDao.get(shareId, user().getId()) == null) {
            return portfolioDao.add(new Portfolio(user().Id, shareId, amount));
        }
        return updatePortfolio(shareId, amount);
    }

    private boolean updatePortfolio(long shareId, int amount) {
        Portfolio portfolio = portfolioDao.get(shareId, user().getId());
        portfolio.setAmount(portfolio.getAmount() + amount);
        return portfolioDao.update(portfolio);
    }

    private void withdrawMoney() {
        user().setAccountBalance(user().getAccountBalance() - share().getPricePerShare());
    }

    /**
     * Sell.
     */
    public void sell() {
        if (share() == null) {
            System.out.println("No Such Share");//TODO: Change to StatusText
            return;
        }
        int sellAmountOfShares = pane.getShareAmountSpinner().getValue();
        if (sellAmountOfShares <= 0) {
            System.out.println("Invalid number of shares");//TODO: Change to StatusText
            return;
        }
        Portfolio portfolio = portfolioDao.get(share.getId(), user.Id);
        if (portfolio == null) {
            System.out.println("Share in not part of User Portfolio..."); //TODO: Change to StatusText
            return;
        }
        if (portfolio.getAmount() < sellAmountOfShares) {
            System.out.println("Sorry, you tried to sell too many shares...");//TODO: Change to StatusText
            return;
        }
        updatePortfolio(portfolio, sellAmountOfShares);
        depositMoney(share().getPricePerShare() * sellAmountOfShares);
        System.out.println("Successfully sold!");//TODO: Change to StatusText
    }

    private void updatePortfolio(Portfolio portfolio, int oldAmountOfShares) {
        int amountOfShares = portfolio.getAmount() - oldAmountOfShares;
        portfolio.setAmount(amountOfShares);
        portfolioDao.update(portfolio);
    }

    private void depositMoney(double depositAmount) {
        User user = user();
        user.setAccountBalance(user().getAccountBalance() + depositAmount);
        userDao.update(user);
    }

}
