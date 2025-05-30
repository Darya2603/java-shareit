package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.CommentClient;
import ru.practicum.comment.dto.CommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class CommentController {
    private final CommentClient commentClient;

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentDto> addComment(
            @PathVariable @Positive Long itemId,
                    @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId,
            @Valid @RequestBody CommentDto comment) {
        comment.setAuthorId(userId);
        comment.setItemId(itemId);
        return commentClient.add(userId, itemId, comment);
    }
}
