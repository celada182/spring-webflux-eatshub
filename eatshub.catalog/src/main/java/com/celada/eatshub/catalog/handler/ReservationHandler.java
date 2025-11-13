package com.celada.eatshub.catalog.handler;

import com.celada.eatshub.catalog.domain.ReservationRequest;
import com.celada.eatshub.catalog.service.definition.ReservationService;
import com.celada.eatshub.catalog.validator.ReactiveValidator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReservationHandler {
    private final ReservationService reservationService;
    private final ReactiveValidator reactiveValidator;

    public Mono<ServerResponse> postReservation(ServerRequest request) {
        return request.bodyToMono(ReservationRequest.class)
                .flatMap(this.reactiveValidator::validate)
                .flatMap(this.reservationService::create)
                .flatMap(id ->
                        ServerResponse.created(URI.create("reservation/" + id))
                                .contentType(MediaType.APPLICATION_JSON)
                                .build())
                .doOnSuccess(response -> log.info("Reservation created successfully"))
                .doOnError(error -> log.error("Error creating reservation", error));
    }

    public Mono<ServerResponse> getReservationById(ServerRequest request) {
        return parseUUID(request.pathVariable("id"))
                .flatMap(this.reservationService::readById)
                .flatMap(reservation -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(reservation))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnSuccess(response -> log.info("Reservation retrieved successfully"))
                .doOnError(error -> log.error("Error retrieving reservation", error));
    }

    public Mono<ServerResponse> putReservation(ServerRequest request) {
        return parseUUID(request.pathVariable("id"))
                .flatMap(id -> request.bodyToMono(ReservationRequest.class)
                        .flatMap(this.reactiveValidator::validate)
                        .flatMap(reservationRequest -> this.reservationService.update(id, reservationRequest))
                        .flatMap(reservation -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(reservation))
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .doOnSuccess(response -> log.info("Reservation updated successfully"))
                        .doOnError(error -> log.error("Error updating reservation", error))
                );
    }

    public Mono<ServerResponse> deleteReservation(ServerRequest request) {
        return parseUUID(request.pathVariable("id"))
                .flatMap(this.reservationService::delete)
                .then(ServerResponse.noContent().build())
                .doOnSuccess(response -> log.info("Reservation deleted successfully"))
                .doOnError(error -> log.error("Error deleting reservation", error));
    }

    private Mono<UUID> parseUUID(String id) {
        try {
            return Mono.just(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return Mono.error(new ValidationException("Invalid UUID: " + id));
        }
    }

}
