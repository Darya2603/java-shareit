package ru.practicum.item.utils;

import jakarta.validation.Valid;
import lombok.experimental.UtilityClass;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.system.exception.ValidationException;

@UtilityClass
public class Validate {
    public static void itemDto(@Valid ItemDto item) {
        if (item.getOwnerId() == null) {
            throw new ValidationException("Укажите владельца");
        }

        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Укажите название");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Задайте описание");
        }

        if (item.getAvailable() == null) {
            throw new ValidationException("Укажите статус о том, доступна или нет вещь для аренды");
        }
    }
}
