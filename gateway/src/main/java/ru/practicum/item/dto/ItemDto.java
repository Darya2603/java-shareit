package ru.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.comment.dto.CommentDto;

import java.util.List;

@Getter
@Setter
public class ItemDto {
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;
    private Boolean available;

    @Positive
    private Long ownerId;

    @Positive
    private Long requestId;

    private List<CommentDto> comments;
    private Long lastBooking;
    private Long nextBooking;
}