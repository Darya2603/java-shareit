package ru.practicum.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.interfaces.CommentService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.system.exception.AccessDeniedException;
import ru.practicum.system.exception.NotFoundException;
import ru.practicum.user.interfaces.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Override
    @Transactional
    public ItemDto add(ItemDto itemDto, Long userId) {
        if (!userService.checkIdExist(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto get(Long id) {
        ItemDto itemDto = itemRepository.findById(id)
                .map(ItemMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Вещь с таким id не найдена"));
        return addCommentsAndBookings(itemDto);
    }

    @Override
    public List<ItemDto> get(List<Long> ids) {
        return itemRepository.getByIdIn(ids).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> findAvailableByNameOrDescription(String query) {
        if (query.isBlank()) {
            return List.of();
        }
        List<ItemDto> res = ItemMapper.toDto(
                itemRepository
                        .findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(
                                query,
                                query));
        return addCommentsAndBookings(res);
    }

    @Override
    public List<ItemDto> getByUserId(Long userId) {
        return addCommentsAndBookings(ItemMapper.toDto(itemRepository.findByOwnerId(userId)));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ItemDto patch(ItemDto itemDto, Long userId) {
        Item itemSaved = checkAccess(itemDto, userId);

        if (itemDto.getName() != null) {
            itemSaved.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            itemSaved.setDescription(itemDto.getDescription());
        }

        if (itemDto.getRequestId() != null) {
            itemSaved.setRequestId(itemDto.getRequestId());
        }

        if (itemDto.getAvailable() != null) {
            itemSaved.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toDto(itemRepository.save(itemSaved));
    }

    @Override
    public List<ItemDto> getByRequestIds(List<Long> requestIds) {
        return itemRepository.findByRequestIdIn(requestIds).stream()
                .map(ItemMapper::toDto).toList();
    }

    @Override
    @Transactional
    public boolean isItemAvailable(Long itemId) {
        return itemRepository.findAvailableByItemId(itemId);
    }

    @Override
    public boolean checkIdExist(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    private Item checkAccess(ItemDto item, Long userId) {
        Long itemId = item.getId();

        if (item == null || !checkIdExist(itemId)) {
            throw new NotFoundException("Вещь с таким id не найдена");
        }

        if (!userService.checkIdExist(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        Item itemSaved = itemRepository.findById(itemId).get();

        if (itemSaved.getOwnerId() == null || !itemSaved.getOwnerId().equals(userId)) {
            throw new AccessDeniedException("Только владелец может редактировать вещь");
        }

        return itemSaved;
    }

    private List<ItemDto> addCommentsAndBookings(List<ItemDto> itemsDto) {
        List<Long> itemsIds = itemsDto.stream().map(ItemDto::getId).toList();
        List<CommentDto> comments = commentService.findByItemId(itemsIds);

        Map<Long, List<CommentDto>> itemComments = comments.stream()
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        return itemsDto.stream()
                .map(item -> {
                    List<CommentDto> c = itemComments.getOrDefault(item.getId(), List.of());
                    item.setComments(c);
                    return item;
                }).toList();
    }

    private ItemDto addCommentsAndBookings(ItemDto itemDto) {
        return addCommentsAndBookings(List.of(itemDto)).get(0);
    }
}