package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;

    @Size(min = 2, max = 255)
    private String name;

    @Email
    @Size(min = 2, max = 255)
    private String email;
}
