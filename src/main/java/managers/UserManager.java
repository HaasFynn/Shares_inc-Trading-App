package managers;

import entities.User;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The type User manager.
 */
public class UserManager {
    private SessionFactory sessionFactory;


    /**
     * Instantiates a new User manager.
     *
     * @param sessionFactory the session factory
     */
    public UserManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Add user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean add(User user) {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            if (getUserWithPass(user.username, user.password) == null) {
                session.persist(user);
                exists.set(true);
            }
        });
        return exists.get();
    }

    public User getUserWithPass(String username, String pass) {
        final AtomicReference<User> existingUser = new AtomicReference<>();
        try {
            sessionFactory.inTransaction(session ->
                    existingUser.set(
                            (User) session.createQuery("from User u where u.username = :username and u.password = :password")
                                    .setParameter("username", username)
                                    .setParameter("password", pass)
                                    .getSingleResult()
                    ));
        } catch (NoResultException e) {
            existingUser.set(null);
        }
        return existingUser.get();
    }

    public User getUserByUsername(String username) {
        final AtomicReference<User> existingUser = new AtomicReference<>();
        try {
            sessionFactory.inTransaction(session ->
                    existingUser.set(
                            (User) session.createQuery("from User u where u.username = :username")
                                    .setParameter("username", username)
                                    .getSingleResult()
                    ));
        } catch (NoResultException e) {
            existingUser.set(null);
        }
        return existingUser.get();
    }

    /**
     * Save boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean save(User user) {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            session.save(user);
            exists.set(true);
        });
        return exists.get();
    }

    /**
     * Delete boolean.
     *
     * @param username the username
     * @param pass     the pass
     * @return the boolean
     */
    public boolean delete(String username, String pass) {
        User user = getUserWithPass(username, pass);
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        if (user != null) {
            sessionFactory.inTransaction(session -> {
                session.delete(user);
                exists.set(true);
            });
        }
        return exists.get();
    }

}
