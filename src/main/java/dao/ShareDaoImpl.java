package dao;

import entities.Share;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TransactionRequiredException;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.List;

public class ShareDaoImpl implements ShareDao {
    private final EntityManager entityManager;

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
                    .getResultStream().findFirst().orElse(null);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Share> getAll() {
        return entityManager.createQuery("FROM Share", Share.class)
                .getResultList();
    }


    @Override
    public boolean add(Share share) {
        if (share == null) {
            return false;
        }
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
        entityManager.getTransaction().begin();
        for (Share share : shares) {
            try {
                if (share != null) {
                    entityManager.persist(share);
                }
            } catch (Exception e) {
                System.out.println("Worked, but Share: " + share + " already exists and was not added!");
            }
        }
        entityManager.getTransaction().commit();
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
