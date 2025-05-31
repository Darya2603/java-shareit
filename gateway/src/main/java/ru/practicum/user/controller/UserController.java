package ru.practicum.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.UserClient;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.PatchUserDto;
import ru.practicum.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> get(@PathVariable @Positive Long id) {
        return userClient.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> add(@Valid @RequestBody NewUserDto user) {
        return userClient.save(user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> patch(@PathVariable @Positive Long userId,
                    @Valid @RequestBody PatchUserDto user) {
        user.setId(userId);
        return userClient.patch(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable @Positive Long id) {
        userClient.delete(id);
    }
}
