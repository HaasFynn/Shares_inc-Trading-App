package main;

import entities.Share;
import entities.User;
import managers.ShareManager;
import managers.UserManager;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;

/**
 * The type Input handler.
 */
public class InputHandler {
    private SessionFactory sessionFactory;
    private UserManager userManager;
    private ShareManager shareManager;

    /**
     * The In.
     */
    Reader in = new Reader();

    /**
     * Instantiates a new Input handler.
     */
    public InputHandler() {
        this.sessionFactory = Session.createSessionFactory();
        this.userManager = new UserManager(sessionFactory);
        this.shareManager = new ShareManager(sessionFactory);
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            int selection = in.getIntAnswer("User[1] / Share[2]");
            switch (selection) {
                case 1 -> createUser();
                case 2 -> createShare();
                case 3 -> {
                    return;
                }
                default -> System.out.println("Wrong");
            }
        }
    }

    private void createUser() {
        if (userManager.addUser(getUserInformation())) {
            System.out.println("User was created successfully!");
        } else {
            System.out.println("User creation failed successfully!");
        }
    }

    private void createShare() {
        if (shareManager.addShare(getShareInformation())) {
            System.out.println("Share was created successfully!");
        } else {
            System.out.println("Share creation failed successfully!");
        }
    }

    private User getUserInformation() {
        User user = new User();
        user.username = in.getStringAnswer("Username:");
        user.firstname = in.getStringAnswer("Firstname:");
        user.lastname = in.getStringAnswer("Lastname:");
        user.email = in.getStringAnswer("E-Mail:");
        user.password = in.getPassword("Password:");
        return user;
    }
    private Share getShareInformation() {
        Share share = new Share();
        share.name = in.getStringAnswer("Name:");
        share.shortl = in.getStringAnswer("Shortl:");
        share.pricePerShare = in.getDoubleAnswer("PricePerShare:");
        share.stockReturn = in.getDoubleAnswer("Stockreturn:");
        share.existingSharesAmount = in.getIntAnswer("existingSharesAmount:");
        share.date = LocalDateTime.now();
        return share;
    }
}
