package ru.practicum.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "description", nullable = false)
    String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    User requester;
    LocalDateTime created;
}
