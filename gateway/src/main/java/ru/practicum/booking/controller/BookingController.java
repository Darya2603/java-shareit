package ru.practicum.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.BookingClient;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.BookingStatus;
import ru.practicum.booking.utils.BookingValidate;

import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<BookingDto> add(
			@Valid @RequestBody BookingDto bookingDto,
			@RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
		bookingDto.setBookerId(userId);
		BookingValidate.bookingDto(bookingDto);
		return bookingClient.add(bookingDto, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<BookingDto> updateStatus(
			@PathVariable Long bookingId,
			@RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId,
			@RequestParam(defaultValue = "true") boolean approved) {
		return bookingClient.updateStatus(bookingId, userId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingDto> get(@PathVariable @Positive Long bookingId) {
		return bookingClient.get(bookingId);
	}

	@GetMapping
	public ResponseEntity<List<BookingDto>> getByUser(
			@RequestParam(defaultValue = "ALL") BookingStatus state,
			@RequestHeader(value = "X-Sharer-User-Id", required = true) @Positive Long userId) {
		return bookingClient.getByBooker(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<List<BookingDto>> getByOwner(
			@RequestParam(defaultValue = "ALL") BookingStatus state,
			@RequestHeader(value = "X-Sharer-User-Id", required = false) @Positive Long userId) {
		return bookingClient.getByOwner(userId, state);
	}
}
