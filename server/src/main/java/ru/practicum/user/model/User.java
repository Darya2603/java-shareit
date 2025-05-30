package ru.practicum.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull
    @Size(min = 2, max = 255)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    @NotNull
    @Email
    @Size(min = 5, max = 255)
    private String email;
}
