package javafx.controllers;

import console.dao.*;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.pages.ShareViewPane;

public class ShareViewController extends Controller {

    private final ShareViewPane pane;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final User user;
    private final Share share;

    public ShareViewController(ShareViewPane pane, Share share, User user) {
        this.pane = pane;

        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);

        this.share = share;
        this.user = user;
    }

    public Share share() {
        return shareDao.get(share.getId());
    }
}
