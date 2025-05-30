package ru.practicum.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.interfaces.BookingService;
import ru.practicum.booking.model.BookingStatus;
import ru.practicum.booking.utils.BookingValidate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(
            @Valid @RequestBody BookingDto bookingDto,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        bookingDto.setBookerId(userId);
        BookingValidate.bookingDto(bookingDto);
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(
            @PathVariable Long bookingId,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId,
            @RequestParam(defaultValue = "true") boolean approved) {
        return bookingService.updateStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@PathVariable Long bookingId) {
        return bookingService.get(bookingId);
    }

    @GetMapping
    public List<BookingDto> getByUser(
            @RequestParam(defaultValue = "ALL") BookingStatus state,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
        return bookingService.getByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwner(
            @RequestParam(defaultValue = "ALL") BookingStatus state,
            @RequestHeader(value = "X-Sharer-User-Id", required = false) @Positive Long userId) {
        return bookingService.getByOwner(userId, state);
    }
}
