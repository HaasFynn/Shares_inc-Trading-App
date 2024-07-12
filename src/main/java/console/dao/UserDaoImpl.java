package console.dao;

import console.entities.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * The type User dao.
 */
public class UserDaoImpl implements UserDao {
    private final EntityManager entityManager;

    /**
     * Instantiates a new User dao.
     *
     * @param entityManager the entity manager
     */
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
        } catch (EntityExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(User user) {
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        return true;
    }


    @Override
    public boolean delete(User user) {
        if (user == null) return false;
        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        return true;
    }

}
