package ru.practicum.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getByUserId(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        return itemService.getByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto add(
            @Valid @RequestBody ItemDto item,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        item.setOwnerId(userId);
        return itemService.add(item, userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto get(@PathVariable Long id) {
        return itemService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto patch(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemDto item,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        item.setId(itemId);
        item.setOwnerId(userId);
        return itemService.patch(item, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.findAvailableByNameOrDescription(text);
    }
}