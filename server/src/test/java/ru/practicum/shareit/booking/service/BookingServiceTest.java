package ru.practicum.shareit.booking.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.ShareItApp;
import ru.practicum.shareit.UtilsTests;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.interfaces.BookingService;
import ru.practicum.booking.model.BookingStatus;
import ru.practicum.comment.interfaces.CommentService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserService;

@SpringBootTest(classes = ShareItApp.class)
public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Test
    void addSuccess() {
        UserDto userDto = UtilsTests.genUserDto(userService);
        ItemDto itemDto = UtilsTests.genItemDto(itemService, userDto.getId());

        BookingDto bookingDto = UtilsTests.genBookingDto(
                bookingService,
                userDto.getId(),
                itemDto.getId());

        BookingDto bookingSaved = bookingService.add(bookingDto, userDto.getId());

        assertTrue(bookingSaved != null);
        assertTrue(bookingSaved.getBookerId().equals(bookingDto.getBookerId()));
        assertTrue(bookingSaved.getItemId().equals(bookingDto.getItemId()));
    }

    @Test
    void checkIdExistSuccess() {
        BookingDto bookingDto = addUserItembookingGetBookingDto();
        BookingDto bookingSaved = bookingService.get(bookingDto.getId());
        assertTrue(bookingSaved.getId().equals(bookingDto.getId()));
    }

    @Test
    void deleteSuccess() {
        BookingDto bookingDto = addUserItembookingGetBookingDto();
        Long id = bookingDto.getId();
        BookingDto bookingSaved = bookingService.get(id);
        assertTrue(bookingSaved.getId().equals(id));
        bookingService.delete(id);
        assertThrows(RuntimeException.class, () -> commentService.get(id));
    }

    @Test
    void getSuccess() {
        BookingDto bookingDto = addUserItembookingGetBookingDto();
        Long id = bookingDto.getId();
        BookingDto bookingSaved = bookingService.get(id);
        assertTrue(bookingSaved.getId().equals(id));
    }

    @Test
    void updateStatusSuccess() {
        BookingDto bookingDto = addUserItembookingGetBookingDto();
        Long id = bookingDto.getId();
        assertNotEquals(bookingDto.getStatus(), BookingStatus.APPROVED);

        bookingService.updateStatus(id, bookingDto.getBookerId(), true);

        BookingDto bookingSaved = bookingService.get(id);

        assertEquals(bookingSaved.getStatus(), BookingStatus.PAST);
    }

    private BookingDto addUserItembookingGetBookingDto() {
        UserDto userDto = UtilsTests.genUserDto(userService);
        ItemDto itemDto = UtilsTests.genItemDto(itemService, userDto.getId());

        BookingDto bookingDto = UtilsTests.genBookingDto(
                bookingService,
                userDto.getId(),
                itemDto.getId());

        return bookingService.add(bookingDto, userDto.getId());
    }
}