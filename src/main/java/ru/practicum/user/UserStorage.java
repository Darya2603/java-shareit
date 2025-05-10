package ru.practicum.user;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    UserDto addUser(User user);

    UserDto updateUser(User user, Long userId);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    Map<Long, User> getUserMap();

}