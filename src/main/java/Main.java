import dao.*;
import functional.EntityManagement;
import functional.InputHandler;
import jakarta.persistence.EntityManager;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        UserDaoImpl userDao = new UserDaoImpl(entityManager);
        ShareDaoImpl shareDao = new ShareDaoImpl(entityManager);
        PortfolioDaoImpl portfolioDao = new PortfolioDaoImpl(entityManager);
        new InputHandler(userDao, shareDao, portfolioDao).start();
    }

}
