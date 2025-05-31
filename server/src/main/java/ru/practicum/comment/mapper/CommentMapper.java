package ru.practicum.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;

import java.util.List;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItemId(comment.getItemId());
        commentDto.setAuthorId(comment.getAuthorId());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItemId(commentDto.getItemId());
        comment.setAuthorId(commentDto.getAuthorId());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public static List<Comment> toComment(List<CommentDto> commentsDto) {
        return commentsDto.stream().map(CommentMapper::toComment).toList();
    }

    public static List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).toList();
    }
}