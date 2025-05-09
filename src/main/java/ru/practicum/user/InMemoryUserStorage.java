package ru.practicum.user;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import ru.practicum.exception.DuplicateEmailException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    // Используем Long для ключа в Map
    private Map<Long, User> userStorage = new HashMap<>();
    private long userId = 0L; // Используем long для userId

    @Override
    public UserDto addUser(User user) {
        if (isUserDuplicated(user.getEmail())) {
            throw new DuplicateEmailException("Пользователь с такой почтой уже существует.");
        }
        user.setId(generateNewId()); // Используем Long для ID
        userStorage.put(user.getId(), user); // Сохраняем в Map с Long ключом
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(User user, Long userId) {
        User existedUser = userStorage.get(userId);
        if (user.getEmail() != null && !user.getEmail().contains("@")) {
            throw new ValidationException("Введен неверный адрес электронной почты.");
        }
        if (user.getEmail() != null && !existedUser.getEmail().equals(user.getEmail())) {
            if (isUserDuplicated(user.getEmail())) {
                throw new DuplicateEmailException("Пользователь с такой почтой уже существует.");
            }
            existedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            existedUser.setName(user.getName());
        }
        userStorage.put(userId, existedUser);
        return UserMapper.toUserDto(existedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.remove(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = new ArrayList<>(userStorage.values());
        return allUsers.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(userStorage.get(userId));
    }

    @Override
    public Map<Long, User> getUserMap() {
        return userStorage;
    }

    // Обновленный метод для генерации нового ID (Long)
    private long generateNewId() {
        return ++userId;
    }

    private boolean isUserDuplicated(String email) {
        var duplicatedUser = getAllUsers().stream()
                .filter(x -> x.getEmail().equals(email))
                .findAny();
        return duplicatedUser.isPresent();
    }
}


