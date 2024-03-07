package managers;

import entities.User;
import entities.Share;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The type Share manager.
 */
public class ShareManager {

    private SessionFactory sessionFactory;


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
     * @return the boolean
     */
    public boolean addShare(Share share) {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            if (shareExists(share, session)) {
                session.persist(share);
                exists.set(true);
            }
        });
        return exists.get();
    }

    private static boolean shareExists(Share share, Session session) {
        Share existingShare;
        try {
            existingShare = (Share) session.createQuery("from Share s where s.name = :name")
                    .setParameter("name", share.name)
                    .getSingleResult();
        } catch (NoResultException e) {
            existingShare = null;
        }
        return existingShare != null;
    }

}
