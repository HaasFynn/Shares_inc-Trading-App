package Archive;

import entities.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Objects;

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

            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                if (userExists(user.username)) {
                    tx = session.beginTransaction();
                    tx.begin();
                    session.persist(user);
                    tx.commit();
                }
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
            }
            return true;
    }

    private boolean userExists(String username) {
        User user = getUserByUsername(username);
        return Objects.equals(user.username, "") || Objects.equals(user.email, "");
    }

    public User getUserWithPass(String username, String pass) {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("from User u where u.username = :username and u.password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", pass).getSingleResultOrNull();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("from User u where u.username = :username", User.class)
                    .setParameter("username", username).getSingleResultOrNull();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Save boolean.
     *
     * @param user the user
     * @return <p>if action succeeded</p>
     */
    public boolean save(User user) {
        sessionFactory.inTransaction(session -> {
            session.merge(user);
        });
        return true;
    }

    /**
     * Delete boolean.
     *
     * @param username the username
     * @param password the password
     * @return <p>if action succeeded</p>
     */
    public boolean delete(String username, String password) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            tx.begin();
            session
                    .createMutationQuery("delete from User u where u.username = :username and u.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return false;
        }
        return true;
    }

}
