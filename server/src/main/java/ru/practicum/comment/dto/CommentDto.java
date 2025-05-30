package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 1000)
    private String text;

    @Positive
    private Long itemId;

    @Positive
    private Long authorId;
    private LocalDateTime created;

    private String authorName;
}
