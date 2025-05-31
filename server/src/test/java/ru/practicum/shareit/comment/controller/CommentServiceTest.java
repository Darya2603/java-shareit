package ru.practicum.shareit.comment.controller;

import ru.practicum.ShareItApp;
import ru.practicum.shareit.UtilsTests;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.interfaces.BookingService;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.interfaces.CommentService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItApp.class)
class CommentServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addSuccess() {
        UserDto userDto = UtilsTests.genUserDto(userService);
        ItemDto itemDto = UtilsTests.genItemDto(itemService, userDto.getId());

        BookingDto bookingDto = UtilsTests.genBookingDto(
                bookingService,
                userDto.getId(),
                itemDto.getId());

        bookingService.updateStatus(bookingDto.getId(), userDto.getId(), true);

        CommentDto commentDto = UtilsTests.genCommentDto(
                commentService,
                userDto.getId(),
                itemDto.getId());

        List<CommentDto> commentsSaved = commentService.getAll();
        assertTrue(commentsSaved.size() > 0);
        assertTrue(!commentsSaved.stream().filter(x -> x.getText().equals(commentDto.getText())).toList().isEmpty());

    }

    @Test
    void addErrorNoBooking() {
        UserDto userDto = UtilsTests.genUserDto(userService);
        ItemDto itemDto = UtilsTests.genItemDto(itemService, userDto.getId());
        assertThrows(RuntimeException.class, () -> UtilsTests.genCommentDto(
                commentService,
                userDto.getId(),
                itemDto.getId()));
    }

    @Test
    void deleteSuccess() {
        CommentDto commentDto = addUserItemCommentGetComment();
        Long id = commentDto.getId();
        assertEquals(commentService.get(id).getId(), id);
        commentService.delete(id);
        assertThrows(RuntimeException.class, () -> commentService.get(id));
    }

    @Test
    void updateSuccess() {
        CommentDto commentDto = addUserItemCommentGetComment();
        Long id = commentDto.getId();
        commentDto.setText("New text");
        commentService.update(commentDto);
        assertTrue(commentService.get(id).getText().equals("New text"));
    }

    @Test
    void getAllSuccess() {
        CommentDto commentDto1 = addUserItemCommentGetComment();
        CommentDto commentDto2 = addUserItemCommentGetComment();
        CommentDto commentDto3 = addUserItemCommentGetComment();
        List<CommentDto> commentsSaved = commentService.getAll();

        assertTrue(!commentsSaved.stream().filter(x -> x.getText().equals(commentDto1.getText())).toList().isEmpty());

        assertTrue(!commentsSaved.stream().filter(x -> x.getText().equals(commentDto2.getText())).toList().isEmpty());

        assertTrue(!commentsSaved.stream().filter(x -> x.getText().equals(commentDto3.getText())).toList().isEmpty());
    }

    @Test
    void getByIdSuccess() {
        CommentDto commentDto = addUserItemCommentGetComment();
        Long id = commentDto.getId();
        assertEquals(commentService.get(id).getId(), id);
    }

    @Test
    void findByItemIdSuccess() {
        CommentDto commentDto = addUserItemCommentGetComment();
        Long id = commentDto.getId();
        assertEquals(commentService.findByItemId(commentDto.getItemId()).get(0).getId(), id);
    }

    @Test
    void findByAuthorIdSuccess() {
        CommentDto commentDto = addUserItemCommentGetComment();
        Long id = commentDto.getId();
        assertEquals(commentService.findByAuthorId(commentDto.getAuthorId()).get(0).getId(), id);
    }

    private CommentDto addUserItemCommentGetComment() {

        UserDto userDto = UtilsTests.genUserDto(userService);
        ItemDto itemDto = UtilsTests.genItemDto(itemService, userDto.getId());

        BookingDto bookingDto = UtilsTests.genBookingDto(
                bookingService,
                userDto.getId(),
                itemDto.getId());

        bookingService.updateStatus(bookingDto.getId(), userDto.getId(), true);

        return UtilsTests.genCommentDto(
                commentService,
                userDto.getId(),
                itemDto.getId());
    }
}
