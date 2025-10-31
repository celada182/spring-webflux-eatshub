package com.celada.eatshub.catalog.service;

import com.celada.eatshub.catalog.repository.ReservationRepository;
import com.celada.eatshub.catalog.repository.RestaurantRepository;
import com.celada.eatshub.catalog.repository.enums.ReservationStatus;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import com.celada.eatshub.catalog.service.definition.ReservationService;
import com.celada.eatshub.catalog.service.definition.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Mono<ReservationDto> create(ReservationDto reservationDto) {
        return null;
    }

    @Override
    public Flux<ReservationDto> readByRestaurantId(String restaurantId) {
        return null;
    }

    @Override
    public Flux<ReservationDto> readByRestaurantIdAndStatus(String restaurantId, ReservationStatus status) {
        return null;
    }

    @Override
    public Mono<ReservationDto> update(UUID id, ReservationDto reservationDto) {
        return null;
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return null;
    }
}
