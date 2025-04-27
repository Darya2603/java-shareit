package ru.practicum.booking;

import lombok.*;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
