package managers;

import entities.Share;
import entities.User;
import jakarta.persistence.NoResultException;
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
    public boolean add(Share share) {
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            if (getExistingShare(share.name) == null) {
                session.persist(share);
                exists.set(true);
            }
        });
        return exists.get();
    }

    private Share getExistingShare(String name) {
        final AtomicReference<Share> existingShare = new AtomicReference<>();
        try {
            sessionFactory.inTransaction(session ->
                    existingShare.set(
                            (Share) session.createQuery("from Share s where s.name = :name")
                                    .setParameter("name", name)
                                    .getSingleResult()
                    ));
        } catch (NoResultException e) {
            existingShare.set(null);
        }
        return existingShare.get();
    }

    /**
     * Delete boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean delete(String name) {
        Share share = getExistingShare(name);
        AtomicReference<Boolean> exists = new AtomicReference<>(false);
        if (share != null) {
            sessionFactory.inTransaction(session -> {
                session.delete(share);
                exists.set(true);
            });
        }
        return exists.get();
    }
}
