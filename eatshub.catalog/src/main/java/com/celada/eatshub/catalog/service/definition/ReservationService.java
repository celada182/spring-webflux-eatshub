package com.celada.eatshub.catalog.service.definition;

import com.celada.eatshub.catalog.repository.enums.PriceRange;
import com.celada.eatshub.catalog.repository.enums.ReservationStatus;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import com.celada.eatshub.catalog.repository.model.RestaurantDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    Mono<ReservationDto> create(ReservationDto reservationDto);
    Flux<ReservationDto> readByRestaurantId(String restaurantId);
    Flux<ReservationDto> readByRestaurantIdAndStatus(String restaurantId, ReservationStatus status);
    Mono<ReservationDto> update(UUID id, ReservationDto reservationDto);
    Mono<Void> delete(UUID id);
}
