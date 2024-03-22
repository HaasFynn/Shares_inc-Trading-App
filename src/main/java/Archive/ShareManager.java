package Archive;

import entities.Share;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * The type Share manager.
 */
public class ShareManager {

    private final SessionFactory sessionFactory;


    /**
     * Instantiates a new Share manager.
     *
     * @param sessionFactory the session factory
     */
    public ShareManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Add share boolean.
     *
     * @param share the share
     * @return <p>if action succeeded</p>
     */
    @Transactional
    public boolean add(Share share) {
        return false;
    }

    private boolean shareExists(String shareName) {
        return getShareByName(shareName) == null;
    }

    private Share getShareByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Share s where s.name = :name", Share.class).setParameter("name", name).getSingleResultOrNull();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Save boolean.
     *
     * @param share the share
     * @return <p>if action succeeded</p>
     */
    public boolean save(Share share) {
        sessionFactory.inTransaction(session -> {
            session.merge(share);
        });
        return true;
    }

    /**
     * Delete boolean.
     *
     * @param name the name
     * @return <p>if action succeeded</p>
     */
    public boolean delete(String name) {
        Share share = getShareByName(name);
        if (share != null) {
            Transaction tx = null;
            try (Session session = sessionFactory.openSession()) {
                tx = session.beginTransaction();
                tx.begin();
                session.createMutationQuery("delete from Share s where s.name = :username")
                        .setParameter("name", share.name)
                        .executeUpdate();
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
            }
            return true;
        }
        return false;
    }
}
