package functional;

import com.sun.tools.javac.Main;
import creators.ShareCreator;
import dao.PortfolioDaoImpl;
import dao.ShareDaoImpl;
import dao.UserDaoImpl;
import entities.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

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

    @Test
    void doSharesGetAddedEveryTime() {
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        ShareDaoImpl shareDao = new ShareDaoImpl(entityManager);


        assertThrows(Exception.class, () -> {
            for (int i = 1; i <= 10; i++) {
                TimeUnit.SECONDS.sleep(3);
                shareDao.addAll(ShareCreator.createNewShares(100));
                System.out.println("Didnt Work this time. Attempt number: " + i);
            }
        });
    }
}