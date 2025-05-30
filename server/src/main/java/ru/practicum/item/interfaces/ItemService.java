package ru.practicum.item.interfaces;

import ru.practicum.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, Long userId);

    ItemDto get(Long id);

    List<ItemDto> getByRequestIds(List<Long> requestIds);

    List<ItemDto> get(List<Long> ids);

    boolean isItemAvailable(Long itemId);

    List<ItemDto> findAvailableByNameOrDescription(String query);

    List<ItemDto> getByUserId(Long userId);

    void delete(Long id);

    ItemDto patch(ItemDto item, Long userId);

    boolean checkIdExist(Long id);
}