package ru.practicum.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.system.client.BaseClient;
import ru.practicum.system.client.ConvertResponse;

@Service
public class CommentClient extends BaseClient {
    ConvertResponse convertResponse;

    public CommentClient(@Value("${shareit-server.url}") String serverUrl, ConvertResponse convertResponse) {
        super(serverUrl, "/items");
        this.convertResponse = convertResponse;
    }

    public ResponseEntity<CommentDto> add(Long userId, Long itemId, CommentDto comment) {
        return convertResponse.toEntity(post("/" + itemId + "/comment", userId, comment), CommentDto.class);
    }

}
