package ru.practicum.comment;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", nullable = false)
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @JoinColumn(name = "created", nullable = false)
    private LocalDateTime created;

    public Comment() {

    }
}

