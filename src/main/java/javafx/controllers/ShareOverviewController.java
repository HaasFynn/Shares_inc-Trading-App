package javafx.controllers;

import console.dao.*;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.pages.ShareOverviewPane;

public class ShareOverviewController extends Controller {

    private final ShareOverviewPane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private User user;
    private Share share;

    public ShareOverviewController(ShareOverviewPane pane, User user) {
        this.pane = pane;

        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);

        this.user = user;
        this.share = pane.getShare();
    }
}
