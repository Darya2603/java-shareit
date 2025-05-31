package ru.practicum.user.interfaces;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto get(Long id);

    List<UserDto> get(List<Long> id);

    UserDto save(UserDto user);

    void delete(Long id);

    boolean checkIdExist(Long id);

    boolean checkEmailExist(String email);

    UserDto patch(UserDto user);
}