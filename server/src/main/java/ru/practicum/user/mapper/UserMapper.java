package ru.practicum.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

@UtilityClass
public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static List<User> toUser(List<UserDto> usersDto) {
        return usersDto.stream().map(UserMapper::toUser).toList();
    }

    public static List<UserDto> toDto(List<User> users) {
        return users.stream().map(UserMapper::toDto).toList();
    }
}