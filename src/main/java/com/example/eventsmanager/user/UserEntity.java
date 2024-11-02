package com.example.eventsmanager.user;

import com.example.eventsmanager.event.EventEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @NotNull
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @Email
    @NotNull
    @Column(nullable = false, length = 100)
    private String email;

    @NotNull
    @Column(nullable = false, length = 50)
    private String firstname;

    @NotNull
    @Column(nullable = false, length = 50)
    private String lastname;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "participants", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<EventEntity> bookedEvents;
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "organiser")
    private List<EventEntity> organisedEvents;
    @PreRemove
    private void preRemove() {
        for (EventEntity event : bookedEvents) {
            event.getParticipants().remove(this);
        }
    }
}
