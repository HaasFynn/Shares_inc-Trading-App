package javafx.controllers;

import console.dao.*;
import console.entities.Share;
import console.entities.User;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.pages.ShareViewPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class ShareViewController extends CustomController {

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
                    spinner.getEditor().setText(oldValue); // Reset to old value if not valid
                }
            }
        });
    }
}
