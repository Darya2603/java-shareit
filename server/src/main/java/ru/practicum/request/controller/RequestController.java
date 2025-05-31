package ru.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.interfaces.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(
            @RequestBody String text,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        return requestService.add(userId, text);
    }

    @GetMapping
    public List<RequestDto> getByUserId(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        return requestService.getByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAll() {
        return requestService.getAll();
    }

    @GetMapping("/{requestId}")
    public RequestDto getById(@PathVariable Long requestId) {
        return requestService.get(requestId);
    }
}
