package com.celada.eatshub.catalog.handler;

import com.celada.eatshub.catalog.constants.ErrorConstants;
import com.celada.eatshub.catalog.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
// One is being created by default
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Global Error: {}", ex.getMessage());

        HttpStatus status;
        String message;
        String errorType;

        if (ex instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
            errorType = ErrorConstants.ERROR_TYPE_VALIDATION;
        } else if (ex instanceof BusinessException) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
            message = ex.getMessage();
            errorType = ErrorConstants.ERROR_TYPE_BUSINESS;
        } else if (Objects.nonNull(ex.getMessage()) && ex.getMessage().contains(ErrorConstants.ERROR_MESSAGE_RESTAURANT_NOT_FOUND)) {
            status = HttpStatus.NOT_FOUND;
            message = ex.getMessage();
            errorType = ErrorConstants.ERROR_TYPE_NOT_FOUND;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = ErrorConstants.ERROR_MESSAGE_UNEXPECTED;
            errorType = ErrorConstants.ERROR_TYPE_INTERNAL;
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ErrorConstants.RESPONSE_KEY_TIMESTAMP, LocalDateTime.now().toString());
        errorResponse.put(ErrorConstants.RESPONSE_KEY_PATH, exchange.getRequest().getPath().value());
        errorResponse.put(ErrorConstants.RESPONSE_KEY_STATUS, status.value());
        errorResponse.put(ErrorConstants.RESPONSE_KEY_ERROR, status.getReasonPhrase());
        errorResponse.put(ErrorConstants.RESPONSE_KEY_ERROR_TYPE, errorType);
        errorResponse.put(ErrorConstants.RESPONSE_KEY_MESSAGE, message);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            final var bytes = objectMapper.writeValueAsBytes(errorResponse);
            final var buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse()
                    .writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Error writing error response", e);
            return Mono.error(e);
        }
    }
}
