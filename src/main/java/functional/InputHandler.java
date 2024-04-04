package functional;

import creators.ShareCreator;
import dao.PortfolioDao;
import dao.ShareDao;
import dao.UserDao;
import entities.Portfolio;
import entities.Share;
import entities.User;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Input handler.
 */
public class InputHandler {
    @PersistenceContext
    private UserDao userDao;
    private ShareDao shareDao;
    private PortfolioDao portfolioDao;
    private User loggedInUser;

    /**
     * Instance of the "Reader" class
     */
    Reader in = new Reader();

    /**
     * Instantiates a new Input handler.
     */

    public InputHandler(UserDao userDao, ShareDao shareDao, PortfolioDao portfolioDao) {
        this.userDao = userDao;
        this.shareDao = shareDao;
        this.portfolioDao = portfolioDao;
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
                    """)) {
                case 1 -> userStart();
                case 2 -> shareStart();
                case 3 -> trade();
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    // TODO Create Buy(), Sell(), showPortfolio() Functions
    private void trade() {
        if (loggedInUser == null) {
            System.out.println("Please register first!");
            return;
        }
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

    private void buy() {
        Share share = shareDao.getByName(in.getStringAnswer("Whats the name of the share you would like to buy?"));
        if (share == null) {
            System.out.println("No Such Share");
            return;
        }
        int amountOfShares = in.getIntAnswer("How many shares would you like to purchase?");
        if (amountOfShares <= 0) {
            System.out.println("Invalid number of shares");
            return;
        }
        if (userHasEnoughMoney(share, amountOfShares)) {
            System.out.println("Insufficient Funds");
            return;
        }
        if (!portfolioDao.add(createPortfolio(share.Id, amountOfShares))) {
            System.out.println("Sorry share could not be purchased");
        } else {
            withdrawMoney(share);
            System.out.println("Successfully purchased");
        }
    }

    private void sell() {
        Share share = shareDao.getByName(in.getStringAnswer("Whats the name of the share you would like to sell?"));
        if (share == null) {
            System.out.println("No Such Share");
            return;
        }
        int sellAmountOfShares = in.getIntAnswer("How many shares would you like to sell?");
        if (sellAmountOfShares <= 0) {
            System.out.println("Invalid number of shares");
        }
        Portfolio portfolio = portfolioDao.get(loggedInUser.Id, share.Id);
        if (portfolio.getAmount() < sellAmountOfShares) {
            System.out.println("Sorry, you tried to sell too many shares...");
        } else {
            updatePortfolio(portfolio, sellAmountOfShares);
            depositMoney(share.getPricePerShare() * sellAmountOfShares);
        }
    }

    private void showPortfolio() {
        List<Portfolio> userPortfolio = portfolioDao.getAll();
        List<Share> shareList = new ArrayList<>();
        for (Portfolio p : userPortfolio) {
            shareList.add(shareDao.get(p.getShareId()));
        }
        printPortfolio(userPortfolio, shareList);
    }

    private static void printPortfolio(List<Portfolio> userPortfolio, List<Share> shareList) {
        System.out.println("Portfolio: \n");
        System.out.println("======================");
        for (int i = 0; i < userPortfolio.size(); i++) {
            System.out.println(i + ". Share:");
            System.out.println("Name: " + shareList.get(i).getName());
            System.out.println("Amount of your shares: " + userPortfolio.get(i).getAmount());
            System.out.println("======================\n");
        }
        System.out.println("======================\n");
    }

    private void depositMoney(double depositAmount) {
        loggedInUser.setAccountBalance(depositAmount);
        userDao.update(loggedInUser);
    }

    private void updatePortfolio(Portfolio portfolio, int oldAmountOfShares) {
        int amountOfShares = portfolio.getAmount() - oldAmountOfShares;
        portfolio.setAmount(amountOfShares);
        portfolioDao.update(portfolio);
    }

    private void withdrawMoney(Share share) {
        loggedInUser.setAccountBalance(loggedInUser.getAccountBalance() - share.getPricePerShare());
    }

    private boolean userHasEnoughMoney(Share share, int amountOfShares) {
        return loggedInUser.getAccountBalance() - (share.getPricePerShare() * amountOfShares) < 0;
    }

    private Portfolio createPortfolio(long shareId, int amount) {
        return new Portfolio(loggedInUser.Id, shareId, amount);
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
            case 3 -> {
                if (loggedInUser == null) {
                    System.out.println("Please login first!");
                    break;
                }
                if (editUser()) {
                    System.out.println("Changes Saved!");
                    break;
                }
                System.out.println("Changes could not be saved!");
            }
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

    private boolean editUser() {
        switch (in.getIntAnswer("""
                [1] Change Username
                [2] Change Firstname
                [3] Change Lastname
                [4] Change E-Mail
                [5] Change Password
                [6] Return""")) {
            case 1 -> {
                return editUsername();
            }
            case 2 -> {
                return editFirstname();
            }
            case 3 -> {
                return editLastname();
            }
            case 4 -> {
                return editEmail();
            }
            case 5 -> {
                return editPassword();
            }
            default -> System.out.println("This function does not exist!");
        }
        return false;
    }

    private boolean editUsername() {
        String pass = in.getStringAnswer("Password:");
        String username = in.getStringAnswer("New Username:");
        if (passwordsEqual(pass)) {
            if (userDao.getByUsername(username) == null) {
                System.out.println("Username already exists!");
                return false;
            }
            loggedInUser.setUsername(username);
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editFirstname() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.setLastname(in.getStringAnswer("New Firstname:"));
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editLastname() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.setLastname(in.getStringAnswer("New Lastname:"));
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editEmail() {
        String pass = in.getStringAnswer("Password:");
        if (passwordsEqual(pass)) {
            loggedInUser.setEmail(in.getStringAnswer("New Email:"));
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean editPassword() {
        String oldPass = in.getStringAnswer("Old Password");
        if (passwordsEqual(oldPass)) {
            loggedInUser.setPassword(in.getStringAnswer("Password:"));
            return userDao.update(loggedInUser);
        }
        System.out.println("Wrong Password!");
        return false;
    }

    private boolean passwordsEqual(String oldPass) {
        return oldPass.equals(loggedInUser.getPassword());
    }

    public void login() {
        String username = in.getStringAnswer("Username:");
        String pass = in.getStringAnswer("Password:");
        User user = userDao.getByPassword(username, pass);
        if (user == null) {
            System.out.println("Couldn't find user!");
            return;
        }
        loggedInUser = user;
        System.out.println("Login succeeded!");
    }

    private void createUser() {
        User user = getNewUser();
        if (!userDao.add(user)) {
            System.out.println("User creation failed!");
            return;
        }
        System.out.println("User was created successfully!");
    }

    private void deleteUser() {
        String username = in.getStringAnswer("Username:");
        String password = in.getStringAnswer("Password:");
        if (!userDao.delete(userDao.getByPassword(username, password))) {
            System.out.println("Was not able to delete User!");
            return;
        }
        System.out.println("User deletion successfully!");
    }

    private void createShare() {
        Share share = getNewShare();
        if (!shareDao.add(share)) {
            System.out.println("Share creation failed!");
            return;
        }
        System.out.println("Share was created successfully!");
    }

    private void deleteShare() {
        String name = in.getStringAnswer("Name:");
        if (!shareDao.delete(shareDao.getByName(name))) {
            System.out.println("Was not able to delete share!!");
            return;
        }
        System.out.println("Share deletion successfully!");

    }

    private User getNewUser() {
        User user = new User();
        user.setUsername(in.getStringAnswer("Username:"));
        user.setFirstname(in.getStringAnswer("Firstname:"));
        user.setLastname(in.getStringAnswer("Lastname:"));
        user.setEmail(in.getStringAnswer("E-Mail:"));
        user.setPassword(in.getPassword("Password:"));
        return user;
    }

    private Share getNewShare() {
        Share share = new Share();
        share.setName(in.getStringAnswer("Name:"));
        share.setShortl(in.getStringAnswer("Shortl:"));
        share.setPricePerShare(in.getDoubleAnswer("PricePerShare:"));
        share.setStockReturn(in.getDoubleAnswer("Stockreturn:"));
        share.setExistingSharesAmount(in.getIntAnswer("existingSharesAmount:"));
        share.setDate(LocalDateTime.now());
        return share;
    }

}
