package ru.practicum.booking.interfaces;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.BookingStatus;
import ru.practicum.system.exception.AccessDeniedException;
import ru.practicum.system.exception.NotFoundException;

import java.util.List;

public interface BookingService {
    BookingDto get(Long id) throws NotFoundException;

    List<BookingDto> getByBooker(Long userId, BookingStatus state);

    List<BookingDto> getByBookerAndItemAndStatus(Long userId, Long itemId, BookingStatus state);

    List<BookingDto> getByOwner(Long userId, BookingStatus state);

    BookingDto add(BookingDto booking, Long userId);

    BookingDto updateStatus(Long bookingId, Long userId, boolean approved);

    void delete(Long id);

    BookingDto update(Long bookingId, BookingDto booking, Long userId) throws NotFoundException, AccessDeniedException;

    boolean checkIdExist(Long id);

    boolean checkItemForBookerWithStatus(Long bookerId, Long itemId, BookingStatus status);
}