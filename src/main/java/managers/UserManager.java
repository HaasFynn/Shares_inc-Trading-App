package managers;

import entities.User;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The type User manager.
 */
public class UserManager {
    private final SessionFactory sessionFactory;


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
     * @return <p>if action succeeded</p>
     */
    public boolean add(User user) {
        var succeeded = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            if (getUserWithPass(user.username, user.password) == null) {
                session.persist(user);
                succeeded.set(true);
            }
        });
        return succeeded.get();
    }

    public User getUserWithPass(String username, String pass) {
        final var existingUser = new AtomicReference<>();
        try {
            sessionFactory.inTransaction(session ->
                    existingUser.set(
                            session.createQuery("from User u where u.username = :username and u.password = :password")
                                    .setParameter("username", username)
                                    .setParameter("password", pass)
                                    .getSingleResult()
                    ));
        } catch (NoResultException e) {
            existingUser.set(null);
        }
        return (User) existingUser.get();
    }

    public User getUserByUsername(String username) {
        final var existingUser = new AtomicReference<>();
        try {
            sessionFactory.inTransaction(session ->
                    existingUser.set(
                            session.createQuery("from User u where u.username = :username")
                                    .setParameter("username", username)
                                    .getSingleResult()
                    ));
        } catch (NoResultException e) {
            existingUser.set(null);
        }
        return (User) existingUser.get();
    }

    /**
     * Save boolean.
     *
     * @param user the user
     * @return <p>if action succeeded</p>
     */
    public boolean save(User user) {
        var succeeded = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            session.merge(user);
            succeeded.set(true);
        });
        return succeeded.get();
    }

    /**
     * Delete boolean.
     *
     * @param username the username
     * @param pass     the pass
     * @return <p>if action succeeded</p>
     */
    public boolean delete(String username, String pass) {
        User user = getUserWithPass(username, pass);
        var succeeded = new AtomicReference<>(false);
        if (user != null) {
            sessionFactory.inTransaction(session -> {
                session.remove(user);
                succeeded.set(true);
            });
        }
        return succeeded.get();
    }

}
