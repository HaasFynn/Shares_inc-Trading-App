package functional;

import dao.UserDaoImpl;
import entities.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
    UserDaoImpl userDao = new UserDaoImpl(entityManager);

    @Test
    void doesLoginWork() {
        createTestAccount();
        User user = new User();
        user.setUsername("test");
        user.setEmail("john.doe@gmail.com");
        user.setPassword("1234");
        assertEquals(user.getUsername().toLowerCase(), userDao.getByUsername("Test").getUsername().toLowerCase());
    }

    void createTestAccount() {
        User user = new User();
        user.setUsername("Test");
        user.setEmail("john.doe@gmail.com");
        user.setPassword("1234");
        userDao.add(user);
    }
}