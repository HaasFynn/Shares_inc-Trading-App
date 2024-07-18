package console.functional;

import console.entities.*;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * The type Entity management.
 */
public class EntityManagement {

    private static EntityManagerFactory entityManagerFactory = null;

    /**
     * Create entity manager factory entity manager factory.
     *
     * @return the entity manager factory
     */
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
                            .addAnnotatedClasses(
                                    Share.class,
                                    User.class,
                                    Portfolio.class,
                                    Tag.class,
                                    Picture.class)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return entityManagerFactory;
    }
}
