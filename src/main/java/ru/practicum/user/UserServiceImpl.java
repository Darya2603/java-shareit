package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.UserNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserDto addUser(User user) {
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public UserDto updateUser(User user, int userId) {
        User existedUser = repository.findById(userId).get();
        if (user.getName() != null) {
            existedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existedUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(repository.save(existedUser));
    }

    @Override
    public void deleteUser(int userId) {
        repository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int userId) {
        Optional<User> userOpt = repository.findById(userId);
        if (userOpt.isPresent()) {
            return UserMapper.toUserDto(userOpt.get());
        } else {
            throw new UserNotFoundException("Такого пользователя не существует");
        }
    }

    @Override
    public Map<Integer, User> getUserMap() {
        return null;
    }
}