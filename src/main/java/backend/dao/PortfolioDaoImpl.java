package backend.dao;

import backend.entities.Portfolio;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class PortfolioDaoImpl implements PortfolioDao {
    private final EntityManager entityManager;

    public PortfolioDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Portfolio get(long id) {
        return entityManager.find(Portfolio.class, id);
    }

    @Override
    public Portfolio get(long shareId, long userId) {
        try {
            return entityManager.createQuery("FROM Portfolio p WHERE p.shareId = :shareId AND p.userId = :userId", Portfolio.class)
                    .setParameter("shareId", shareId)
                    .setParameter("userId", userId)
                    .getResultStream().findFirst().orElse(null);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Portfolio> getAll() {
        return entityManager.createQuery("FROM Portfolio", Portfolio.class)
                .getResultList();
    }
    @Override
    public List<Portfolio> getAllFromUser(long userId) {
        try {
            return entityManager.createQuery("FROM Portfolio p WHERE p.userId = :userId", Portfolio.class)
                    .setParameter("userId", userId)
                    .getResultStream().toList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean add(Portfolio portfolio) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(portfolio);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Portfolio portfolio) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(portfolio);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Portfolio portfolio) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(portfolio);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
