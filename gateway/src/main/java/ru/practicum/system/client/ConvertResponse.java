package ru.practicum.system.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.system.exception.GatewayException;

@Component
public class ConvertResponse {
    private final ObjectMapper mapper;

    public ConvertResponse(ObjectMapper mapper) {
        this.mapper = mapper;
        mapper.registerModule(new JavaTimeModule());
        mapper.findAndRegisterModules();
    }

    public <T> ResponseEntity<T> toEntity(ResponseEntity<Object> source, Class<T> targetType) {
        if (source.getStatusCode().is2xxSuccessful()) {
            try {
                String json = mapper.writeValueAsString(source.getBody());
                T body = mapper.readValue(json, targetType);
                return ResponseEntity
                        .status(source.getStatusCode())
                        .headers(source.getHeaders())
                        .body(body);
            } catch (JsonProcessingException e) {
                throw new GatewayException(source);
            }
        }
        throw new GatewayException(source);
    }

    public <T> ResponseEntity<T> toEntity(ResponseEntity<Object> source, TypeReference<T> targetType) {
        if (source.getStatusCode().is2xxSuccessful()) {
            try {
                String json = mapper.writeValueAsString(source.getBody());
                T body = mapper.readValue(json, targetType);
                return ResponseEntity
                        .status(source.getStatusCode())
                        .headers(source.getHeaders())
                        .body(body);
            } catch (JsonProcessingException e) {
                throw new GatewayException(source);
            }
        }
        throw new GatewayException(source);
    }
}