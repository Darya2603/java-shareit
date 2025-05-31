package ru.practicum.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getByItemIdInAndStatus(List<Long> itemsIds, BookingStatus state);

    List<Booking> getByBookerId(Long userId);

    List<Booking> getByBookerIdAndStatus(Long userId, BookingStatus state);

    List<Booking> getByBookerIdAndItemIdAndStatus(Long userId, Long itemId, BookingStatus state);

    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 " +
            "FROM bookings " +
            "WHERE item_id = :itemId " +
            "AND booker_id = :bookerId " +
            "AND status = :statusId " +
            "LIMIT 1" +
            ")", nativeQuery = true)
    Boolean checkItemForBookerWithStatus(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("statusId") Integer statusId);
}