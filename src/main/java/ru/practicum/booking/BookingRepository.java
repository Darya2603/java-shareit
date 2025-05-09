package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByStartDesc(int userId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(int userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int userId, BookingStatus bookingStatus);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(int ownerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int ownerId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(int userId, BookingStatus bookingStatus);

    List<Booking> findAllByItemIdAndStatusNotOrderByStartAsc(int itemId, BookingStatus bookingStatus);

    List<Booking> findAllByItemIdAndBookerIdAndEndBefore(int itemId, int bookerId, LocalDateTime now);
}


