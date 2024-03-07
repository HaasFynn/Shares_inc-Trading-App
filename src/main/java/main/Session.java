package main;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import user.User;

import static java.time.LocalDateTime.now;

public class Session {
    private SessionFactory sessionFactory;

    protected void setUp(Class givenClass) {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .loadProperties("hibernate.properties").build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(givenClass)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public void addUser(Class givenClass, User user) {
        setUp(givenClass);
        sessionFactory.inTransaction(session -> {
            session.persist(user.userID, user);
        });
    }

}
