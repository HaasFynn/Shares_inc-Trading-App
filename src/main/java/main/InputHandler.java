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
    private User loggedInUser;
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
    public void login() {
        String username = in.getStringAnswer("Username:");
        String pass = in.getStringAnswer("Password:");
        User user = userManager.getUserWithPass(username, pass);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Login succeeded!");
        } else {
            System.out.println("Couldn't find User!");
        }
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            switch (in.getIntAnswer("""
                    [1] User
                    [2] Share
                    """)) {
                case 1 -> userStart();
                case 2 -> shareStart();
                case 3 -> {
                    return;
                }
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void userStart() {
        switch (in.getIntAnswer("""
                [1] Login
                [2] Create User
                [3] Change User
                [4] Delete User""")) {
            case 1 -> login();
            case 2 -> createUser();
            case 3 -> changeUser();
            case 4 -> deleteUser();
        }
    }

    private void shareStart() {
        switch (in.getIntAnswer("""
                [1] Create Share
                [2] Delete Share""")) {
            case 1 -> createShare();
            case 2 -> deleteShare();
        }
    }

    private void changeUser() {
        boolean userChanged = false;
        switch (in.getIntAnswer("""
                [1] Change Username
                [2] Change Firstname
                [3] Change Lastname
                [4] Change E-Mail
                [5] Change Password""")) {
            case 1 -> userChanged = editUsername();
            case 2 -> userChanged = editFirstname();
            case 3 -> userChanged = editLastname();
            case 4 -> userChanged = editEmail();
            case 5 -> userChanged = editPassword();
            default -> System.out.println("This Function does not exist!");
        }
        if (userChanged) {
            System.out.println("Changes saved!");
        }
    }

    private boolean editUsername() {
        String username = in.getStringAnswer("Set new Username:");
        if (userManager.getUserByUsername(username) == null) {
            loggedInUser.username = username;
        }
        return userManager.save(loggedInUser);
    }

    private boolean editFirstname() {
        loggedInUser.firstname = in.getStringAnswer("Firstname:");
        return userManager.save(loggedInUser);
    }

    private boolean editLastname() {
        loggedInUser.lastname = in.getStringAnswer("Lastname:");
        return userManager.save(loggedInUser);
    }

    private boolean editEmail() {
        loggedInUser.email = in.getStringAnswer("E-Mail:");
        return userManager.save(loggedInUser);
    }

    private boolean editPassword() {
        String password = in.getStringAnswer("Password:");
        if (password.equals(loggedInUser.password)) {
            return userManager.save(loggedInUser);
        }
        return false;
    }


    private void createUser() {
        if (userManager.add(getNewUser())) {
            System.out.println("User was created successfully!");
        } else {
            System.out.println("User creation failed!");
        }
    }

    private void deleteUser() {
        String username = in.getStringAnswer("Username:");
        String pass = in.getStringAnswer("Password:");
        if (userManager.delete(username, pass)) {
            System.out.println("User deletion successfully!");
        } else {
            System.out.println("User deletion successfully!");
        }
    }

    private void createShare() {
        if (shareManager.add(getNewShare())) {
            System.out.println("Share was created successfully!");
        } else {
            System.out.println("Share creation failed!");
        }
    }

    private void deleteShare() {
        String name = in.getStringAnswer("Name:");
        if (shareManager.delete(name)) {
            System.out.println("Share deletion successfully!");
        } else {
            System.out.println("Share deletion successfully!");
        }
    }

    private User getNewUser() {
        User user = new User();
        user.username = in.getStringAnswer("Username:");
        user.firstname = in.getStringAnswer("Firstname:");
        user.lastname = in.getStringAnswer("Lastname:");
        user.email = in.getStringAnswer("E-Mail:");
        user.password = in.getPassword("Password:");
        return user;
    }

    private Share getNewShare() {
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
