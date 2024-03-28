package functional;

import creators.ShareCreator;
import dao.ShareDao;
import dao.UserDao;
import entities.Share;
import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;

/**
 * The type Input handler.
 */
public class InputHandler {
    @PersistenceContext
    private EntityManager entityManager;
    private UserDao userDao;
    private ShareDao shareDao;
    private User loggedInUser;

    /**
     * Instance of the "Reader" class
     */
    Reader in = new Reader();

    /**
     * Instantiates a new Input handler.
     */
    public InputHandler() {
        try {
            this.entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
            this.userDao = new UserDao(entityManager);
            this.shareDao = new ShareDao(entityManager);
            this.loggedInUser = null;
        } catch (IllegalStateException e) {
            System.out.println("Exception Thrown");
        }
    }

    /**
     * Start.
     */
    public void start() {
        while (true) {
            switch (in.getIntAnswer("""
                    [1] User management
                    [2] Share actions
                    [3] Trade
                    [4] Return
                    """)) {
                case 1 -> userStart();
                case 2 -> shareStart();
                case 3 -> trade();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    // TODO Create Buy, Sell, ShowPortfolio Functions
    private void trade() {
        while (true) {
            switch (in.getIntAnswer("""
                    [1] Buy
                    [2] Sell
                    [3] Show Portfolio
                    [4] Return
                    """)) {
                case 1 -> buy();
                case 2 -> sell();
                case 3 -> showPortfolio();
                case 4 -> {
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
                [3] Edit User
                [4] Delete User
                [5] Return""")) {
            case 1 -> login();
            case 2 -> createUser();
            case 3 -> editUser();
            case 4 -> deleteUser();
        }
    }

    private void shareStart() {
        switch (in.getIntAnswer("""
                [1] Generate Shares
                [2] Delete Share
                [3] Return""")) {
            case 1 -> generateShares();
            case 2 -> deleteShare();
        }
    }

    private void generateShares() {
        int amount = in.getIntAnswer("How many shares would you like to generate?");
        for (int i = 0; i < amount; i++) {
            shareDao.add(ShareCreator.getNewShare());
        }
        System.out.println("Successfully created " + amount + " shares!");
    }

    private void editUser() {
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
                default -> System.out.println("This function does not exist!");
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
            if (userDao.getByUsername(username) == null) {
                System.out.println("Username already exists!");
                return false;
            } else {
                loggedInUser.username = username;
                return userDao.update(loggedInUser);
            }
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editFirstname() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.lastname = in.getStringAnswer("New Firstname:");
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editLastname() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.lastname = in.getStringAnswer("New Lastname:");
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editEmail() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.email = in.getStringAnswer("New E-Mail:");
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editPassword() {
        String oldPass = in.getStringAnswer("Old Password");
        if (passwordsEqual(oldPass)) {
            loggedInUser.password = in.getPassword("New Password:");
            return userDao.update(loggedInUser);
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
        User user = userDao.getByPassword(username, pass);
        if (user != null) {
            loggedInUser = user;
            System.out.println("Login succeeded!");
        } else {
            System.out.println("Couldn't find user!");
        }
    }

    private void createUser() {
        User user = getNewUser();
        if (userDao.add(user)) {
            System.out.println("User was created successfully!");
        } else {
            System.out.println("User creation failed!");
        }
    }

    private void deleteUser() {
        String username = in.getStringAnswer("Username:");
        String password = in.getStringAnswer("Password:");
        if (userDao.delete(userDao.getByPassword(username, password))) {
            System.out.println("User deletion successfully!");
        } else {
            System.out.println("Was not able to delete User!");
        }
    }

    private void createShare() {
        Share share = getNewShare();
        if (shareDao.add(share)) {
            System.out.println("Share was created successfully!");
        } else {
            System.out.println("Share creation failed!");
        }
    }

    private void deleteShare() {
        String name = in.getStringAnswer("Name:");

        if (shareDao.delete(shareDao.getByName(name))) {
            System.out.println("Share deletion successfully!");
        } else {
            System.out.println("Was not able to delete share!!");
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
