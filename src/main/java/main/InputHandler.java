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
    private final UserManager userManager;
    private final ShareManager shareManager;
    private User loggedInUser;
    /**
     * The In.
     */
    Reader in = new Reader();

    /**
     * Instantiates a new Input handler.
     */
    public InputHandler() {
        SessionFactory sessionFactory = Session.createSessionFactory();
        this.userManager = new UserManager(sessionFactory);
        this.shareManager = new ShareManager(sessionFactory);
        this.loggedInUser = null;
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            switch (in.getIntAnswer("""
                    [1] User actions
                    [2] Share actions""")) {
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
                [4] Delete User
                [5] Return""")) {
            case 1 -> login();
            case 2 -> createUser();
            case 3 -> changeUser();
            case 4 -> deleteUser();
        }
    }

    private void shareStart() {
        switch (in.getIntAnswer("""
                [1] Create Share
                [2] Delete Share
                [6] Return""")) {
            case 1 -> createShare();
            case 2 -> deleteShare();
        }
    }

    private void changeUser() {
        if (loggedInUser == null) {
            System.out.println("You're not logged in!");
        } else {
            boolean userChanged = false;
            switch (in.getIntAnswer("""
                    [1] Change Username
                    [2] Change Firstname
                    [3] Change Lastname
                    [4] Change E-Mail
                    [5] Change Password
                    [6] Return""")) {
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
    }

    private boolean editUsername() {
        String pass = in.getStringAnswer("Password:");
        String username = in.getStringAnswer("New Username:");
        if (passwordsEqual(pass)) {
            if (userManager.getUserByUsername(username) == null) {
                loggedInUser.username = username;
                return userManager.save(loggedInUser);
            } else {
                System.out.println("Username already exists!");
                return false;
            }
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editFirstname() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.lastname = in.getStringAnswer("New Lastname:");
            return userManager.save(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editLastname() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.lastname = in.getStringAnswer("New Lastname:");
            return userManager.save(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editEmail() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.email = in.getStringAnswer("New E-Mail:");
            return userManager.save(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;

    }

    private boolean editPassword() {
        String oldPass = in.getStringAnswer("Old Password");
        if (passwordsEqual(oldPass)) {
            loggedInUser.password = in.getPassword("New Password:");
            return userManager.save(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean passwordsEqual(String oldPass) {
        return oldPass.equals(loggedInUser.password);
    }


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
