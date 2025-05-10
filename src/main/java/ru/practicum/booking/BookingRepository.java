package ru.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);  // Изменен на Long

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(Long userId, LocalDateTime now, LocalDateTime now1);  // Изменен на Long

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);  // Изменен на Long

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);  // Изменен на Long

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);  // Изменен на Long

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);  // Изменен на Long

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime now, LocalDateTime now1);  // Изменен на Long

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);  // Изменен на Long

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);  // Изменен на Long

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus bookingStatus);  // Изменен на Long

    List<Booking> findAllByItemIdAndStatusNotOrderByStartAsc(Long itemId, BookingStatus bookingStatus);  // Изменен на Long

    List<Booking> findAllByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime now);  // Изменен на Long
}


