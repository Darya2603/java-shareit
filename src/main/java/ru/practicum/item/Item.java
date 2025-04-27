package ru.practicum.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

@Setter
@Getter
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    @Size(min = 10, max = 1000)
    private String description;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne
    @JoinColumn(name = "request")
    private ItemRequest request;
}
