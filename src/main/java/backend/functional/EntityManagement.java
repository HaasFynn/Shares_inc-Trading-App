package backend.functional;

import backend.entities.Portfolio;
import backend.entities.Share;
import backend.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * The type Session.
 */
public class EntityManagement {

    private static EntityManagerFactory entityManagerFactory = null;

    public static EntityManagerFactory createEntityManagerFactory() {
        if (entityManagerFactory != null) {
            return entityManagerFactory;
        }
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .loadProperties("hibernate.properties").build();
        try {
            entityManagerFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(Share.class)
                            .addAnnotatedClass(User.class)
                            .addAnnotatedClass(Portfolio.class)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return entityManagerFactory;
    }
}
