package console.dao;

import console.entities.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User get(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByUsername(String username) {
        List<User> userList = getAll();
        return userList.stream().filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getByPassword(String username, String password) {
        try {
            return entityManager.createQuery("FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                    .setParameter("username", username).setParameter("password", password).getSingleResult();
        } catch (NoResultException e) {
            System.err.println("Error while getting user by password... " + e);
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public boolean add(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityExistsException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while adding user..." + e);
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
            return true;
        } catch (IllegalArgumentException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while updating user..." + e);
            return false;
        }
    }


    @Override
    public boolean delete(User user) {
        if (user == null) return false;
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityNotFoundException e) {
            entityManager.getTransaction().rollback();
            return false;
        }
    }

}
