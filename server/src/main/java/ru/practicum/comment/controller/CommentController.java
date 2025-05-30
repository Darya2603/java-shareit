package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.interfaces.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto addComment(
            @PathVariable Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId,
            @Valid @RequestBody CommentDto comment) {
        comment.setAuthorId(userId);
        comment.setItemId(itemId);
        return commentService.add(comment);
    }
}
