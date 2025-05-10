package ru.practicum.user;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserValidator {

    @Autowired
    private UserStorage userStorage;

    public static boolean validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Адрес электронной почты не может быть пустым.");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Введен неверный адрес электронной почты.");
            throw new ValidationException("Введен неверный адрес электронной почты.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не может быть пустым.");
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }
        return true;
    }

    private boolean duplicateEmailValidation(User user) {
        List<String> emails = new ArrayList<>();
        for (UserDto u:userStorage.getAllUsers()) {
            emails.add(u.getEmail());
        }
        return emails.contains(user.getEmail());
    }
}
