package ru.practicum.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.booking.ShortBookingDto;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.CommentRepository;
import ru.practicum.exception.IncorrectUserException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId) {
        Item existedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));
        if (existedItem.getOwner().getId() != ownerId) {
            throw new IncorrectUserException("Неверный владелец");
        }
        if (itemDto.getName() != null) {
            existedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existedItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(existedItem));
    }

    @Override
    public ItemDtoBookingsAndComments getItemById(int ownerId, int itemId) {
        Item existedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));

        ItemDtoBookingsAndComments itemDtoBookingsAndComments = new ItemDtoBookingsAndComments();

        List<Booking> itemBookings = bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(existedItem.getId(), BookingStatus.REJECTED);
        List<Booking> bookingsBefore = itemBookings.stream().filter(i -> i.getStart().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        List<Booking> bookingsAfter = itemBookings.stream().filter(i -> i.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(itemId);
        List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(commentList);

        if (existedItem.getOwner().getId() == ownerId) {
            ShortBookingDto lastBooking;
            if (bookingsBefore.isEmpty()) {
                lastBooking = null;
            } else {
                Booking before = bookingsBefore.get(bookingsBefore.size() - 1);
                lastBooking = new ShortBookingDto();
                lastBooking.setId(before.getId());
                lastBooking.setBookerId(before.getBooker().getId());
            }

            ShortBookingDto nextBooking;
            if (bookingsAfter.isEmpty()) {
                nextBooking = null;
            } else {
                Booking after = bookingsAfter.get(0);
                nextBooking = new ShortBookingDto();
                nextBooking.setId(after.getId());
                nextBooking.setBookerId(after.getBooker().getId());
            }

            itemDtoBookingsAndComments.setId(existedItem.getId());
            itemDtoBookingsAndComments.setName(existedItem.getName());
            itemDtoBookingsAndComments.setDescription(existedItem.getDescription());
            itemDtoBookingsAndComments.setAvailable(existedItem.isAvailable());
            itemDtoBookingsAndComments.setLastBooking(lastBooking);
            itemDtoBookingsAndComments.setNextBooking(nextBooking);
            itemDtoBookingsAndComments.setComments(commentDtoList);
        } else {
            itemDtoBookingsAndComments.setId(existedItem.getId());
            itemDtoBookingsAndComments.setName(existedItem.getName());
            itemDtoBookingsAndComments.setDescription(existedItem.getDescription());
            itemDtoBookingsAndComments.setAvailable(existedItem.isAvailable());
            itemDtoBookingsAndComments.setLastBooking(null);
            itemDtoBookingsAndComments.setNextBooking(null);
            itemDtoBookingsAndComments.setComments(commentDtoList);
        }

        return itemDtoBookingsAndComments;
    }

    @Override
    public List<ItemDtoBookingsAndComments> getUserItems(int ownerId) {
        List<Item> userItems = itemRepository.findByOwnerId(ownerId);
        List<ItemDtoBookingsAndComments> resultList = new ArrayList<>();
        if (!userItems.isEmpty()) {
            for (Item item : userItems) {
                ItemDtoBookingsAndComments itemDtoBookingsAndComments = new ItemDtoBookingsAndComments();

                List<Booking> itemBookings = bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(item.getId(), BookingStatus.REJECTED);
                List<Booking> bookingsBefore = itemBookings.stream().filter(i -> i.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
                List<Booking> bookingsAfter = itemBookings.stream().filter(i -> i.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
                List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(item.getId());
                List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(commentList);

                ShortBookingDto lastBooking;
                if (bookingsBefore.isEmpty()) {
                    lastBooking = null;
                } else {
                    Booking before = bookingsBefore.get(bookingsBefore.size() - 1);
                    lastBooking = new ShortBookingDto();
                    lastBooking.setId(before.getId());
                    lastBooking.setBookerId(before.getBooker().getId());
                }

                ShortBookingDto nextBooking;
                if (bookingsAfter.isEmpty()) {
                    nextBooking = null;
                } else {
                    Booking after = bookingsAfter.get(0);
                    nextBooking = new ShortBookingDto();
                    nextBooking.setId(after.getId());
                    nextBooking.setBookerId(after.getBooker().getId());
                }

                itemDtoBookingsAndComments.setId(item.getId());
                itemDtoBookingsAndComments.setName(item.getName());
                itemDtoBookingsAndComments.setDescription(item.getDescription());
                itemDtoBookingsAndComments.setAvailable(item.isAvailable());
                itemDtoBookingsAndComments.setLastBooking(lastBooking);
                itemDtoBookingsAndComments.setNextBooking(nextBooking);
                itemDtoBookingsAndComments.setComments(commentDtoList);

                resultList.add(itemDtoBookingsAndComments);
            }
            return resultList.stream().sorted(Comparator.comparing(ItemDtoBookingsAndComments::getId)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> searchedItem = itemRepository.findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
        return ItemMapper.listToItemDto(searchedItem);
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, int itemId, int authorId) {
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Отсутствует текст в комментарии");
        }
        User user = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        List<Booking> bookingList = bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(itemId, authorId, LocalDateTime.now());
        if (bookingList.isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду или срок аренды еще не закончился");
        }
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}



