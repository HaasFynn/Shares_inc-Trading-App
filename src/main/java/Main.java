import console.dao.PortfolioDaoImpl;
import console.dao.ShareDaoImpl;
import console.dao.UserDaoImpl;
import console.functional.EntityManagement;
import console.functional.InputHandler;
import jakarta.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        UserDaoImpl userDao = new UserDaoImpl(entityManager);
        ShareDaoImpl shareDao = new ShareDaoImpl(entityManager);
        PortfolioDaoImpl portfolioDao = new PortfolioDaoImpl(entityManager);
        new InputHandler(userDao, shareDao, portfolioDao).start(); //For Console Interface
    }
}
