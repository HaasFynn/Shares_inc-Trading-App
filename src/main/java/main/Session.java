package main;
import entities.Share;
import entities.User;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import static java.time.LocalDateTime.now;

/**
 * The type Session.
 */
public class Session {

    private static SessionFactory sessionFactory = null;

    /**
     * Create session factory session factory.
     *
     * @return the session factory
     */
    protected static SessionFactory createSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .loadProperties("hibernate.properties").build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(Share.class)
                            .addAnnotatedClass(User.class)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return sessionFactory;
    }
}
