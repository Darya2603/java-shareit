package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.practicum.item.interfaces.ItemService;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.service.RequestServiceImpl;
import ru.practicum.system.exception.NotFoundException;
import ru.practicum.user.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestService = new RequestServiceImpl(requestRepository, userService, itemService);
    }

    @Test
    void testAddSuccess() {
        Long userId = 1L;
        String description = "Test request";
        Request request = new Request();
        request.setId(1L);
        request.setRequestorId(userId);
        request.setDescription(description);
        request.setCreated(LocalDateTime.now());

        RequestDto expectedDto = RequestMapper.toDto(request);

        when(userService.checkIdExist(userId)).thenReturn(true);
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        RequestDto result = requestService.add(userId, description);

        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getRequestorId(), result.getRequestorId());

        verify(userService).checkIdExist(userId);
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void testAddUserNotFound() {
        Long userId = 1L;
        String description = "Test request";

        when(userService.checkIdExist(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            requestService.add(userId, description);
        });

        verify(userService).checkIdExist(userId);
        verify(requestRepository, never()).save(any());
    }

    @Test
    void testGetSuccess() {
        Long requestId = 1L;
        Request request = new Request();
        request.setId(requestId);
        request.setDescription("Test request");

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

        RequestDto result = requestService.get(requestId);

        assertEquals(requestId, result.getId());
        assertEquals("Test request", result.getDescription());

        verify(requestRepository).findById(requestId);
    }

    @Test
    void testGetNotFound() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            requestService.get(requestId);
        });

        verify(requestRepository).findById(requestId);
    }

    @Test
    void testGetAllSuccess() {
        Request request1 = new Request();
        request1.setId(1L);
        request1.setDescription("Request 1");

        Request request2 = new Request();
        request2.setId(2L);
        request1.setDescription("Request 2");

        when(requestRepository.findAll()).thenReturn(List.of(request1, request2));

        List<RequestDto> result = requestService.getAll();

        assertEquals(2, result.size());
        verify(requestRepository).findAll();
    }

    @Test
    void testGetByUserIdSuccess() {
        Long userId = 1L;
        Request request1 = new Request();
        request1.setId(1L);
        request1.setRequestorId(userId);
        request1.setDescription("Request 1");

        Request request2 = new Request();
        request2.setId(2L);
        request1.setRequestorId(userId);
        request1.setDescription("Request 2");

        when(requestRepository.getByRequestorId(userId))
                .thenReturn(List.of(request1, request2));

        List<RequestDto> result = requestService.getByUserId(userId);

        assertEquals(2, result.size());

        verify(requestRepository).getByRequestorId(userId);
    }

    @Test
    void testGetByUserIdNoRequests() {
        Long userId = 1L;

        when(requestRepository.getByRequestorId(userId))
                .thenReturn(List.of());

        List<RequestDto> result = requestService.getByUserId(userId);

        assertTrue(result.isEmpty());

        verify(requestRepository).getByRequestorId(userId);
    }

}

