package ru.practicum.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.interfaces.BookingService;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.BookingStatus;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;
import ru.practicum.system.exception.AccessDeniedException;
import ru.practicum.system.exception.NotFoundException;
import ru.practicum.system.exception.ValidationException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto add(BookingDto booking, Long userId) {
        checkItemAndUserExist(booking.getItemId(), userId);

        Booking bookingNew = BookingMapper.toBooking(booking);
        bookingNew.setBookerId(userId);
        bookingNew.setStatus(BookingStatus.WAITING);
        BookingDto res = BookingMapper.toDto(bookingRepository.save(bookingNew));

        return addBookingInfo(res);
    }

    @Override
    public BookingDto get(Long id) {
        return getImpl(id);
    }

    @Override
    public List<BookingDto> getByBooker(Long userId, BookingStatus state) {
        List<BookingDto> res;

        if (state.equals(BookingStatus.ALL)) {
            res = BookingMapper.toDto(bookingRepository.getByBookerId(userId));
        } else {
            res = BookingMapper.toDto(bookingRepository.getByBookerIdAndStatus(userId, state));
        }

        return addBookingInfo(res);
    }

    @Override
    public List<BookingDto> getByBookerAndItemAndStatus(Long userId, Long itemId, BookingStatus status) {
        List<BookingDto> res = BookingMapper
                .toDto(bookingRepository.getByBookerIdAndItemIdAndStatus(userId, itemId, status));

        return setStatusPast(res);
    }

    @Override
    public List<BookingDto> getByOwner(Long userId, BookingStatus state) {
        if (userId == null || !userService.checkIdExist(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        List<Long> itemsIds = itemService.getByUserId(userId).stream().map(ItemDto::getId).toList();
        return BookingMapper.toDto(bookingRepository.getByItemIdInAndStatus(itemsIds, state));
    }

    @Override
    @Transactional
    public BookingDto updateStatus(Long bookingId, Long userId, boolean approved) {
        Booking bookingSaved = checkAccessForStatusChangeAndGetBooking(bookingId, userId);

        BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;

        bookingSaved.setStatus(status);

        BookingDto res = BookingMapper.toDto(bookingRepository.save(bookingSaved));

        return addBookingInfo(res);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, BookingDto booking, Long userId) {
        Booking bookingSaved = checkAccessForUpdateAndGetBooking(bookingId, userId);

        if (booking.getStatus() != null) {
            bookingSaved.setStatus(booking.getStatus());
        }

        return BookingMapper.toDto(bookingRepository.save(bookingSaved));
    }

    @Override
    public boolean checkIdExist(Long id) {
        return checkIdExistImpl(id);
    }

    @Override
    public boolean checkItemForBookerWithStatus(Long bookerId, Long itemId, BookingStatus status) {
        return bookingRepository.checkItemForBookerWithStatus(bookerId, itemId, status.ordinal());
    }

    private void checkItemAndUserExist(Long bookingId, Long userId) {
        if (!itemService.checkIdExist(bookingId)) {
            throw new NotFoundException("Вещь с таким id не найдена");
        }
        if (!userService.checkIdExist(userId)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        if (!itemService.isItemAvailable(bookingId)) {
            throw new ValidationException("Эту вещь сейчас невозможно забронировать");
        }
    }

    private Booking checkBookingAndUserExistReturnBooking(Long bookingId, Long userId) {
        if (bookingId == null || !checkIdExistImpl(bookingId)) {
            throw new NotFoundException("Бронь с таким id не найдена");
        }

        if (userId == null || !userService.checkIdExist(userId)) {
            throw new AccessDeniedException("Пользователь с таким id не найден");
        }

        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь с таким id не найдена"));

    }

    private Booking checkAccessForUpdateAndGetBooking(Long bookingId, Long userId) {
        Booking bookingSaved = checkBookingAndUserExistReturnBooking(bookingId, userId);

        if (!bookingSaved.getBookerId().equals(userId)) {
            throw new AccessDeniedException("Только владелец заказа может редактировать заказ");
        }

        return bookingSaved;
    }

    private Booking checkAccessForStatusChangeAndGetBooking(Long bookingId, Long userId) {
        Booking bookingSaved = checkBookingAndUserExistReturnBooking(bookingId, userId);
        Long itemOwnerId = itemService.get(bookingSaved.getItemId()).getOwnerId();

        if (!itemOwnerId.equals(userId)) {
            throw new AccessDeniedException("Только владелец вещи может одобрить заказ");
        }

        return bookingSaved;
    }

    public boolean checkIdExistImpl(Long id) {
        return bookingRepository.existsById(id);
    }

    private BookingDto getImpl(Long id) {
        BookingDto booking = bookingRepository.findById(id)
                .map(BookingMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Бронь с таким id не найдена"));

        return setStatusPast(addBookingInfo(booking));
    }

    private List<BookingDto> addBookingInfo(List<BookingDto> bookings) {
        List<Long> itemsIds = bookings.stream().map(BookingDto::getItemId).toList();
        List<Long> usersIds = bookings.stream().map(BookingDto::getBookerId).toList();

        List<ItemDto> items = itemService.get(itemsIds);
        List<UserDto> users = userService.get(usersIds);

        Map<Long, ItemDto> itemsByIds = items.stream()
                .collect(Collectors.toMap(ItemDto::getId, Function.identity()));

        Map<Long, UserDto> usersByIds = users.stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        return bookings.stream()
                .map(x -> {
                    x.setItem(itemsByIds.getOrDefault(x.getItemId(), null));
                    x.setBooker(usersByIds.getOrDefault(x.getBookerId(), null));
                    return x;
                }).toList();
    }

    private BookingDto addBookingInfo(BookingDto booking) {
        return addBookingInfo(List.of(booking)).get(0);
    }

    private List<BookingDto> setStatusPast(List<BookingDto> bookings) {
        return bookings.stream().map(x -> {
            if (x.getEnd().isBefore(LocalDateTime.now())) {
                x.setStatus(BookingStatus.PAST);
            }
            return x;
        }).toList();
    }

    private BookingDto setStatusPast(BookingDto booking) {
        return setStatusPast(List.of(booking)).get(0);
    }
}