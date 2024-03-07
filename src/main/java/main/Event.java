package main;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue()
    UUID id;

    @Setter
    private String title;

    @Setter
    @Column(name = "eventDate")
    private LocalDateTime date;

    public Event() {
    }

    public Event(String title, LocalDateTime now) {
        this.title = title;
        this.date = now;
    }
}
