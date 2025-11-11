package com.celada.eatshub.catalog.validator;

import com.celada.eatshub.catalog.clients.PlannerMSClient;
import com.celada.eatshub.catalog.exception.BusinessException;
import com.celada.eatshub.catalog.exception.ResourceNotFoundException;
import com.celada.eatshub.catalog.repository.RestaurantRepository;
import com.celada.eatshub.catalog.repository.model.ReservationDto;
import com.celada.eatshub.catalog.repository.model.RestaurantDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationValidator {
    private final RestaurantRepository restaurantRepository;
    private final PlannerMSClient plannerMSClient;

    // Chain pattern
    public <T> Mono<Void> applyValidations(T input, List<BusinessValidator<T>> validations) {
        if (validations.isEmpty()) return Mono.empty();
        return validations.stream()
                .reduce(Mono.empty(),
                        (chain, validator) -> chain.then(validator.validate(input)),
                        Mono::then);
    }

    public BusinessValidator<ReservationDto> validateRestaurantNotClosed() {
        return reservation -> {
            final var restaurantId = UUID.fromString(reservation.getRestaurantId());
            return this.restaurantRepository.findById(restaurantId)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .doOnNext(r -> log.info("Restaurant found: {}", r.getId()))
                    .flatMap(r -> {
                        if (this.isRestaurantClosed(r, reservation.getTime())) {
                            return Mono.error(new BusinessException("Restaurant is closed"));
                        }
                        return Mono.empty();
                    });
        };
    }

    public BusinessValidator<ReservationDto> validateAvailability() {
        return reservation -> {
            final var restaurantId = UUID.fromString(reservation.getRestaurantId());
            return this.plannerMSClient
                    .verifyAvailability(reservation.getDate(), reservation.getTime(), restaurantId)
                    .doOnNext(a -> log.info("Availability verified for restaurant with id: {}, date: {}, time: {}",
                            restaurantId, reservation.getDate(), reservation.getTime()))
                    .flatMap(isAvailable -> {
                        if (!isAvailable) {
                            return Mono.error(new BusinessException("Restaurant is not available"));
                        }
                        return Mono.empty();
                    });
        };
    }

    private boolean isRestaurantClosed(RestaurantDto restaurant, String reservationTime) {
        try {
            if (Objects.isNull(restaurant.getCloseAt()) || Objects.isNull(reservationTime)) {
                return true;
            }

            LocalTime closeAtLocalTime = LocalTime.parse(restaurant.getCloseAt(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime reservationLocalTime = LocalTime.parse(reservationTime, DateTimeFormatter.ofPattern("HH:mm"));

            return reservationLocalTime.isAfter(closeAtLocalTime);
        } catch (Exception e) {
            log.error("Error checking if restaurant is closed", e);
            return true;
        }
    }
}
