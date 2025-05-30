package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

import java.util.List;

@UtilityClass
public class RequestMapper {
    public static RequestDto toDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setRequestorId(request.getRequestorId());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    public static Request toRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        request.setRequestorId(requestDto.getRequestorId());
        request.setCreated(requestDto.getCreated());
        return request;
    }

    public static List<Request> toRequest(List<RequestDto> itemsDto) {
        return itemsDto.stream().map(RequestMapper::toRequest).toList();
    }

    public static List<RequestDto> toDto(List<Request> items) {
        return items.stream().map(RequestMapper::toDto).toList();
    }
}