package ru.practicum.item;

import ru.practicum.comment.CommentDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int ownerId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId);

    ItemDtoBookingsAndComments getItemById(int ownerId, int itemId);

    List<ItemDtoBookingsAndComments> getUserItems(int ownerId);

    List<ItemDto> getSearch(String text);

    CommentDto addComment(CommentDto commentDto, int itemId, int authorId);
}
