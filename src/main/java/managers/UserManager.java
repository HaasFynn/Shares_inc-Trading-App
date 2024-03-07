package managers;

import entities.Share;
import entities.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
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
    public boolean addUser(User user) {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            if (userExists(user, session)) {
                session.persist(user);
                exists.set(true);
            }
        });
        return exists.get();
    }

    private static boolean userExists(User user, Session session) {
        Share existingUser;
        try {
            existingUser = (Share) session.createQuery("from User u where u.username = :username")
                    .setParameter("username", user.username)
                    .getSingleResult();
        } catch (NoResultException e) {
            existingUser = null;
        }
        return existingUser != null;
    }

}
