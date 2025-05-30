package ru.practicum.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.ItemClient;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.utils.Validate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ItemDto>> getByUserId(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        return itemClient.getByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemDto> add(
            @Valid @RequestBody ItemDto item,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        item.setOwnerId(userId);
        Validate.itemDto(item);
        return itemClient.add(item, userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemDto> get(@PathVariable @Positive Long itemId) {
        return itemClient.get(itemId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        itemClient.delete(id);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ItemDto> patch(
            @PathVariable @Positive Long itemId,
                    @Valid @RequestBody ItemDto item,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        item.setId(itemId);
        item.setOwnerId(userId);
        return itemClient.patch(item, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ItemDto>> search(@RequestParam @NotBlank String text) {
        return itemClient.findAvailableByNameOrDescription(text);
    }
}