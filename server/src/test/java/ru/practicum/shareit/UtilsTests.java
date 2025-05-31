package ru.practicum.shareit;

import java.time.LocalDateTime;
import java.util.Random;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.interfaces.BookingService;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.interfaces.CommentService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserService;

public class UtilsTests {

    public static String genEmail() {
        return UtilsTests.genString(15) + "@test.test";
    }

    public static String genString(int length) {
        String randomChars = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomChars += (char) (random.nextInt(26) + 'a');
        }
        return randomChars;
    }

    public static UserDto genUserDto(UserService userService) {
        UserDto userDto = new UserDto();
        userDto.setEmail(UtilsTests.genEmail());
        userDto.setName("username " + UtilsTests.genString(5));
        return userService.save(userDto);
    }

    public static ItemDto genItemDto(ItemService itemService, Long userId) {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setName("Item name " + UtilsTests.genString(5));
        itemDto.setDescription("Item descritption " + UtilsTests.genString(15));
        itemDto.setOwnerId(userId);
        return itemService.add(itemDto, userId);
    }

    public static CommentDto genCommentDto(CommentService commentService, Long userId, Long itemId) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        commentDto.setText("Comment text " + UtilsTests.genString(15));

        return commentService.add(commentDto);
    }

    public static BookingDto genBookingDto(BookingService bookingService, Long userId, Long itemId) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookerId(userId);
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now().plusSeconds(0));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(1));
        BookingDto saved = bookingService.add(bookingDto, userId);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Interrupted while sleeping", e);
        }

        return saved;
    }
}