package managers;

import entities.Share;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;

import java.util.concurrent.atomic.AtomicReference;

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
    public boolean add(Share share) {
        var succeeded = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            if (getShareByName(share.name) == null) {
                session.persist(share);
                succeeded.set(true);
            }
        });
        return succeeded.get();
    }

    private Share getShareByName(String name) {
        final var existingShare = new AtomicReference<>();
        try {
            sessionFactory.inTransaction(session ->
                    existingShare.set(
                            session.createQuery("from Share s where s.name = :name")
                                    .setParameter("name", name)
                                    .getSingleResult()
                    ));
        } catch (NoResultException e) {
            existingShare.set(null);
        }
        return (Share) existingShare.get();
    }

    /**
     * Save boolean.
     *
     * @param share the share
     * @return <p>if action succeeded</p>
     */
    public boolean save(Share share) {
        var succeeded = new AtomicReference<>(false);
        sessionFactory.inTransaction(session -> {
            session.merge(share);
            succeeded.set(true);
        });
        return succeeded.get();
    }

    /**
     * Delete boolean.
     *
     * @param name the name
     * @return <p>if action succeeded</p>
     */
    public boolean delete(String name) {
        Share share = getShareByName(name);
        var succeeded = new AtomicReference<>(false);
        if (share != null) {
            sessionFactory.inTransaction(session -> {
                session.remove(share);
                succeeded.set(true);
            });
        }
        return succeeded.get();
    }
}
