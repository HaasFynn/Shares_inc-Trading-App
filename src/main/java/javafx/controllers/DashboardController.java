package javafx.controllers;

import console.dao.*;
import console.entities.Portfolio;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.pages.DashboardPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

import static javafx.pages.DashboardPane.SHARE_BOX_AMOUNT;

public class DashboardController extends Controller {

    private final Stage stage;
    private final DashboardPane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final String username;

    public DashboardController(Stage stage, DashboardPane pane, User user) {
        this.stage = stage;
        this.pane = pane;
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
}
