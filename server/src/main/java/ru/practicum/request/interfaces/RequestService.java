package ru.practicum.request.interfaces;

import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto add(Long userId, String text);

    RequestDto get(Long requestId);

    List<RequestDto> getByUserId(Long userId);

    List<RequestDto> getAll();
}