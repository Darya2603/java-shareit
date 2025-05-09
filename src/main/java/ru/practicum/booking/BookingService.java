package ru.practicum.booking;

import java.util.List;

public interface BookingService {

    ResponseBookingDto addBooking(BookingDto bookingDto, int bookerId);

    ResponseBookingDto patchBooking(int ownerId, int bookingId, boolean isApproved);

    ResponseBookingDto getBookingById(int requesterId, int bookingId);

    List<ResponseBookingDto> getAllUsersBookings(int usersId, BookingState state);

    List<ResponseBookingDto> getAllItemOwnerBookings(int ownerId, BookingState state);
}