package com.celada.eatshub.catalog.service.definition;

import com.celada.eatshub.catalog.domain.ReservationRequest;
import com.celada.eatshub.catalog.domain.ReservationResponse;
import com.celada.eatshub.catalog.domain.enums.ReservationStatus;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationService {
    Mono<String> create(ReservationRequest request);
    Mono<ReservationResponse> readById(UUID id);
    Flux<ReservationResponse> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status);
    Mono<ReservationResponse> update(UUID id, ReservationRequest request);
    Mono<Void> delete(UUID id);
}
