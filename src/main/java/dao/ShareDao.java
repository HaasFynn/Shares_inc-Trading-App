package dao;

import entities.Share;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;

public class ShareDao implements Dao<Share> {
    private final EntityManager entityManager;

    public ShareDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Share get(long id) {
        return entityManager.find(Share.class, id);
    }

    public Share getByName(String name) {
        return entityManager.createQuery("FROM Share s WHERE s.name = :name", Share.class)
                .setParameter("name", name)
                .getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Share> getAll() {
        return entityManager.createQuery("FROM Share", Share.class)
                .getResultList();
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
    public boolean update(Share share) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(share);
            entityManager.getTransaction().commit();
        } catch (TransactionRequiredException e) {
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
        } catch (TransactionRequiredException e) {
            return false;
        }
        return true;
    }

}
