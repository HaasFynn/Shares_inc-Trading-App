package console.dao;

import console.entities.Share;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * The type Share dao.
 */
public class ShareDaoImpl implements ShareDao {
    private final EntityManager entityManager;

    /**
     * Instantiates a new Share dao.
     *
     * @param entityManager the entity manager
     */
    public ShareDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Share get(long id) {
        try {
            return entityManager.find(Share.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Share getByName(String name) {
        try {
            return entityManager.createQuery("FROM Share s WHERE s.name = :name", Share.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst().orElse(null);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Share> getAll() {
        return entityManager.createQuery("FROM Share", Share.class).getResultList();
    }

    @Override
    public List<Share> getByNamePrompt(String prompt) {
        try {
            return entityManager.createQuery("FROM Share s WHERE s.name LIKE :name", Share.class)
                    .setParameter("name", "%" + prompt + "%")
                    .getResultStream()
                    .toList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean add(Share share) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(share);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Share... shares) {
        for (Share share : shares) {
            if (share != null) {
                try {
                    entityManager.getTransaction().begin();
                    entityManager.persist(share);
                    entityManager.getTransaction().commit();
                } catch (Exception ignored) {
                }
            }
        }
        return true;
    }

    @Override
    public boolean update(Share share) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(share);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }


    @Override
    public boolean delete(Share share) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(share);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
