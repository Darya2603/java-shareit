package ru.practicum.user.utils;

import jakarta.validation.Valid;
import lombok.experimental.UtilityClass;
import ru.practicum.system.exception.ValidationException;
import ru.practicum.user.dto.UserDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Validate {
    public static void user(@Valid UserDto user) {
        if (user.getEmail() != null && (user.getEmail().isBlank() || !user.getEmail().contains("@")
                || !isValidEmail(user.getEmail()))) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getName() != null && user.getName().isBlank()) {
            user.setName("Unnamed");
        }
    }

    private static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
}
