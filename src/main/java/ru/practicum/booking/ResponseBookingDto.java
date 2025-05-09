package ru.practicum.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ResponseBookingDto {

    private int id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
