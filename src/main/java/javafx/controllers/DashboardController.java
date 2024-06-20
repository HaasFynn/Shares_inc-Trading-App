package javafx.controllers;

import console.dao.*;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.pages.DashboardPane;
import javafx.stage.Stage;

public class DashboardController extends Controller{

    public final UserDao userDao;
    public final PortfolioDao portfolioDao;
    public final ShareDao shareDao;
    private final String username;

    public DashboardController(Stage stage, DashboardPane pane, User user) {
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.username = user.getUsername();
    }

    public User getUser() {
        return userDao.getByUsername(username);
    }
}
