package com.celada.eatshub.catalog.service;

import com.celada.eatshub.catalog.domain.ReservationRequest;
import com.celada.eatshub.catalog.domain.ReservationResponse;
import com.celada.eatshub.catalog.domain.enums.ReservationStatus;
import com.celada.eatshub.catalog.exception.ResourceNotFoundException;
import com.celada.eatshub.catalog.mapper.ReservationMapper;
import com.celada.eatshub.catalog.repository.ReservationRepository;
import com.celada.eatshub.catalog.repository.RestaurantRepository;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import com.celada.eatshub.catalog.service.definition.ReservationService;
import com.celada.eatshub.catalog.validator.ReservationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationMapper reservationMapper;

    @Override
    public Mono<String> create(ReservationRequest request) {

        ReservationDto reservationDto = this.reservationMapper.toDto(request);
        reservationDto.setId(UUID.randomUUID());

        final var validations = List.of(
                this.reservationValidator.validateRestaurantNotClosed(),
                this.reservationValidator.validateAvailability()
        );

        return this.reservationValidator.applyValidations(reservationDto, validations)
                // Wait for validations to execute
                .then(
                        this.restaurantRepository.findById(UUID.fromString(reservationDto.getRestaurantId()))
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                ).flatMap(r -> {
                    if (Objects.isNull(reservationDto.getStatus())) {
                        reservationDto.setStatus(ReservationStatus.PENDING);
                    }
                    log.info("Creating reservation with id: {}", reservationDto.getId());
                    return this.reservationRepository.save(reservationDto)
                            .then(Mono.just(reservationDto.getId().toString()));
                });
    }

    @Override
    public Mono<ReservationResponse> readById(UUID id) {
        return this.reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")))
                .transform( this.reservationMapper::toResponseMono);
    }

    @Override
    public Flux<ReservationResponse> readByRestaurantIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return this.restaurantRepository.findById(restaurantId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                // Many to one
                .flatMapMany(r -> {
                    if (Objects.isNull(status)) {
                        log.info("Init search by restaurant id: {}", restaurantId);
                        return this.reservationRepository.findByRestaurantId(restaurantId.toString());
                    }
                    log.info("Init search by restaurant id {} and status {}", restaurantId, status);
                    return this.reservationRepository.findByRestaurantIdAndStatus(restaurantId.toString(), status);
                })
                .transform(this.reservationMapper::toResponseFlux);
    }

    @Override
    public Mono<ReservationResponse> update(UUID id, ReservationRequest request) {
        ReservationDto reservationDto = this.reservationMapper.toDto(request);
        ReservationDto update = ReservationDto.builder()
                .id(id)
                .status(reservationDto.getStatus())
                .customerName(reservationDto.getCustomerName())
                .notes(reservationDto.getNotes())
                .date(reservationDto.getDate())
                .time(reservationDto.getTime())
                .partySize(reservationDto.getPartySize())
                .build();

        final var validations = List.of(
                this.reservationValidator.validateRestaurantNotClosed(),
                this.reservationValidator.validateAvailability()
        );

        return this.reservationRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")))
                .doOnNext(r -> log.info("Updating reservation with id: {}", r.getId()))
                .flatMap(r -> this.reservationValidator.applyValidations(update, validations))
                .flatMap(r -> this.reservationRepository.save(update))
                .transform(this.reservationMapper::toResponseMono);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return this.reservationRepository.deleteById(id)
                .doOnNext(r -> log.info("Deleting reservation with id: {}", id));
    }
}
