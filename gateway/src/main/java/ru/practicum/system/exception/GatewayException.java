package ru.practicum.system.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class GatewayException extends RuntimeException {
    private HttpStatusCode status;
    private HttpHeaders headers;
    private Object body;

    public GatewayException(ResponseEntity<Object> source) {
        super("Ошибка при конвертации ответа от сервера к шлюзу: " + source.getStatusCode().toString());
        HttpStatusCode status = source.getStatusCode();
        HttpHeaders headers = source.getHeaders();
        Object body = source.getBody();

        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }
}