package ru.practicum.user;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    UserDto addUser(User user);

    UserDto updateUser(User user, int userId);

    void deleteUser(int userId);

    List<UserDto> getAllUsers();

    UserDto getUserById(int userId);

    Map<Integer, User> getUserMap();

}