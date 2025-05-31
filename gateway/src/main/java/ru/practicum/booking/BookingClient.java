package ru.practicum.booking;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.BookingStatus;
import ru.practicum.system.client.BaseClient;
import ru.practicum.system.client.ConvertResponse;

import java.util.List;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    ConvertResponse convertResponse;

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, ConvertResponse convertResponse) {
        super(serverUrl, "/bookings");
        this.convertResponse = convertResponse;
    }

    public ResponseEntity<BookingDto> add(BookingDto bookingDto, Long userId) {
        return convertResponse.toEntity(post("", userId, null, bookingDto), new TypeReference<BookingDto>() {
        });
    }

    public ResponseEntity<BookingDto> updateStatus(Long bookingId, Long userId, Boolean approved) {
        return convertResponse.toEntity(patch("/" + bookingId + "?approved=" + approved, userId, null),
                BookingDto.class);
    }

    public ResponseEntity<BookingDto> get(long bookingId) {
        return convertResponse.toEntity(get("/" + bookingId, null, null), BookingDto.class);
    }

    public ResponseEntity<List<BookingDto>> getByBooker(Long userId, BookingStatus state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name());
        return convertResponse.toEntity(get("?state={state}", userId, parameters),
                new TypeReference<List<BookingDto>>() {
                });
    }

    public ResponseEntity<List<BookingDto>> getByOwner(Long userId, BookingStatus state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name());
        return convertResponse.toEntity(get("/owner?state={state}", userId, parameters),
                new TypeReference<List<BookingDto>>() {
                });
    }
}
