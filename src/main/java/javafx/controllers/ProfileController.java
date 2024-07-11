package javafx.controllers;

import console.dao.*;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.eventlisteners.EventListeners;
import javafx.stage.Stage;

public class ProfileController extends CustomController {
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final long userId;

    public ProfileController(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners);
        this.userId = user.getId();

        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
    }

    public User user() {
        return userDao.getByUsername(username);
    }
}
