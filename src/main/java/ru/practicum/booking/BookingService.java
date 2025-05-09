package ru.practicum.booking;

import java.util.List;

public interface BookingService {

    ResponseBookingDto addBooking(BookingDto bookingDto, Long bookerId);  // Изменено на Long

    ResponseBookingDto patchBooking(Long ownerId, Long bookingId, boolean isApproved);  // Изменено на Long

    ResponseBookingDto getBookingById(Long requesterId, Long bookingId);  // Изменено на Long

    List<ResponseBookingDto> getAllUsersBookings(Long usersId, BookingState state);  // Изменено на Long

    List<ResponseBookingDto> getAllItemOwnerBookings(Long ownerId, BookingState state);  // Изменено на Long
}
