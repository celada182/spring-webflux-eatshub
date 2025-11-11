package com.celada.eatshub.catalog.repository;

import com.celada.eatshub.catalog.domain.enums.ReservationStatus;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ReservationRepository extends ReactiveMongoRepository<ReservationDto, UUID> {
    Flux<ReservationDto> findByRestaurantId(String restaurantId);
    Flux<ReservationDto> findByRestaurantIdAndStatus(String restaurantId, ReservationStatus status);
}
