package ru.practicum.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.user.UserMapper;

@Component
public class ItemRequestMapper {

    private final UserMapper userMapper;

    @Autowired
    public ItemRequestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ItemRequestDto toRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestor(userMapper.toUserDto(request.getRequester()));
        dto.setCreated(request.getCreated());
        return dto;
    }

    public ItemRequest toEntity(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequester(userMapper.toEntity(dto.getRequestor()));
        request.setId(dto.getId());
        request.setCreated(dto.getCreated());
        return request;
    }
}
