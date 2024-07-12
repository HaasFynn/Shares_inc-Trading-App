package console.dao;

import console.entities.Share;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

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
                    .getResultStream()
                    .findFirst().orElse(null);
        } catch (NoResultException e) {
            entityManager.getTransaction().rollback();
            System.err.println("No Share found with name " + name + "\n" + "Returned null");
            return null;
        }
    }

    @Override
    public List<Share> getAll() {
        return entityManager.createQuery("FROM Share", Share.class)
                .getResultList();
    }

    @Override
    public List<Share> getByNamePrompt(String prompt) {
        try {
            return entityManager.createQuery("FROM Share s WHERE s.name LIKE :name", Share.class)
                    .setParameter("name", "%" + prompt + "%")
                    .getResultStream()
                    .toList();
        } catch (NoResultException e) {
            entityManager.getTransaction().rollback();
            System.err.println("No Share found with name " + prompt + "\n" + "Returned null");
            return null;
        }
    }

    @Override
    public boolean add(Share share) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(share);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityExistsException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Entity already exists with name " + share.getName() + "\n" + "Returned null");
            return false;
        }
    }

    @Override
    public boolean addAll(Share... shares) {
        for (Share share : shares) {
            if (share != null) {
                try {
                    entityManager.getTransaction().begin();
                    entityManager.persist(share);
                    entityManager.getTransaction().commit();
                } catch (EntityExistsException e) {
                    entityManager.getTransaction().rollback();
                    System.err.println("Error while adding share, entity already exists\n" + e);
                    return false;
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
            return true;
        } catch (EntityNotFoundException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while updating share, not able to find entity " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean delete(Share share) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(share);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityNotFoundException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error deleting share, could not find entity \n" + e);
            return false;
        }
    }

}
