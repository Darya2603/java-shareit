package ru.practicum.request;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.system.client.BaseClient;
import ru.practicum.system.client.ConvertResponse;

import java.util.List;

@Service
public class RequestClient extends BaseClient {
    ConvertResponse convertResponse;

    public RequestClient(@Value("${shareit-server.url}") String serverUrl, ConvertResponse convertResponse) {
        super(serverUrl, "/requests");
        this.convertResponse = convertResponse;
    }

    public ResponseEntity<RequestDto> add(long userId, String text) {
        return convertResponse.toEntity(post("", userId, null, text), RequestDto.class);
    }

    public ResponseEntity<List<RequestDto>> getByUserId(long userId) {
        return convertResponse.toEntity(get("", userId), new TypeReference<List<RequestDto>>() {
        });
    }

    public ResponseEntity<List<RequestDto>> getAll() {
        return convertResponse.toEntity(get("/all", null, null), new TypeReference<List<RequestDto>>() {
        });
    }

    public ResponseEntity<RequestDto> get(Long requestId) {
        return convertResponse.toEntity(get("/" + requestId, null, null), RequestDto.class);
    }
}
