package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.system.exception.DuplicatedDataException;
import ru.practicum.system.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserService;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto get(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
    }

    @Override
    public List<UserDto> get(List<Long> ids) {
        return userRepository.getByIdIn(ids).stream().map(UserMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        if (checkEmailExist(user.getEmail())) {
            throw new DuplicatedDataException("Пользователь с таким email уже существует");
        }

        if (user.getName() != null && user.getName().isBlank()) {
            user.setName("Unnamed");
        }

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean checkIdExist(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean checkEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserDto patch(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        if (!userRepository.existsById(user.getId())) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        if (user.getEmail() != null && checkEmailExist(user.getEmail())) {
            throw new DuplicatedDataException("Пользователь с таким email уже существует");
        }

        Long id = user.getId();
        User userSaved = userRepository.findById(id).get();

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userSaved.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            userSaved.setName(user.getName());
        }

        return UserMapper.toDto(userRepository.save(userSaved));
    }
}