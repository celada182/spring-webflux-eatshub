package com.celada.eatshub.catalog.service.definition;

import com.celada.eatshub.catalog.domain.enums.ReservationStatus;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationService {
    Mono<ReservationDto> create(ReservationDto reservationDto);
    Mono<ReservationDto> readById(UUID id);
    Flux<ReservationDto> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status);
    Mono<ReservationDto> update(UUID id, ReservationDto reservationDto);
    Mono<Void> delete(UUID id);
}
