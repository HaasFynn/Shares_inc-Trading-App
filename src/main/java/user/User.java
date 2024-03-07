package user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

public class User {
    @Id
    @GeneratedValue
    UUID userID;


}
