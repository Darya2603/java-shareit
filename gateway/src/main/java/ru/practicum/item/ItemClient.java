package ru.practicum.item;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.system.client.BaseClient;
import ru.practicum.system.client.ConvertResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    ConvertResponse convertResponse;

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, ConvertResponse convertResponse) {
        super(serverUrl, "/items");
        this.convertResponse = convertResponse;
    }

    public ResponseEntity<List<ItemDto>> getByUserId(Long userId) {
        return convertResponse.toEntity(get("", userId), new TypeReference<List<ItemDto>>() {
        });
    }

    public ResponseEntity<ItemDto> add(ItemDto itemDto, Long userId) {
        return convertResponse.toEntity(post("", userId, null, itemDto), ItemDto.class);
    }

    public ResponseEntity<ItemDto> get(Long id) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        return convertResponse.toEntity(get("/{id}", null, parameters), ItemDto.class);
    }

    public ResponseEntity<ItemDto> patch(ItemDto itemDto, long userId) {
        return convertResponse.toEntity(patch("/" + itemDto.getId(), userId, itemDto), ItemDto.class);
    }

    public void delete(long id) {
        delete("/" + id);
    }

    public ResponseEntity<List<ItemDto>> findAvailableByNameOrDescription(String text) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text", text);
        return convertResponse.toEntity(get("/search?text={text}", null, parameters),
                new TypeReference<List<ItemDto>>() {
                });
    }
}
