package console.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "profile_picture_path")
public class Picture {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "file_path")
    private String filePath;

    @Setter
    @Column(name = "user_idfk")
    private Long userId;
}
