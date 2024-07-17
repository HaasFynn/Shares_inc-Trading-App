package console.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "profile_picture")
public class Picture {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "img")
    private byte[] img;

    @Setter
    @Column(name = "user_idfk")
    private Long userIDFK;

    public Picture(byte[] img, long userIDFK) {
        setImg(img);
        setUserIDFK(userIDFK);
    }

    public Picture() {
    }
}
