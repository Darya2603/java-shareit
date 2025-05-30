package ru.practicum.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @NotNull
    @Column(name = "item_id", nullable = false)
    @Positive
    private Long itemId;

    @NotNull
    @Column(name = "booker_id", nullable = false)
    @Positive
    private Long bookerId;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private BookingStatus status;
}
