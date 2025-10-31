package com.celada.eatshub.catalog.service;

import com.celada.eatshub.catalog.repository.RestaurantRepository;
import com.celada.eatshub.catalog.repository.enums.PriceRange;
import com.celada.eatshub.catalog.repository.model.RestaurantDto;
import com.celada.eatshub.catalog.service.definition.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Flux<RestaurantDto> readByCuisineType(String cuisineType) {
        return this.restaurantRepository.findByCuisineType(cuisineType)
                .doOnSubscribe(s -> log.info("Init search by cuisine type: {}", cuisineType))
                .doOnNext(r -> log.info("Found restaurant: {}", r.getName()))
                .onErrorResume(e -> {
                    log.error("Error searching by cuisine type: {}", cuisineType, e);
                    return Flux.empty();
                });
    }

    @Override
    public Flux<RestaurantDto> readByNameStartingWithIgnoreCase(String name) {
        return this.restaurantRepository.findByNameStartingWithIgnoreCase(name)
                .doOnSubscribe(s -> log.info("Init search by name: {}", name))
                .doOnNext(r -> log.info("Found restaurant: {}", r.getName()))
                .onErrorResume(e -> {
                    log.error("Error searching by name: {}", name, e);
                    return Flux.empty();
                });
    }

    @Override
    public Flux<RestaurantDto> readByPriceRangeIn(List<PriceRange> prices) {
        return this.restaurantRepository.findByPriceRangeIn(prices)
                .switchIfEmpty(Flux.empty()
                        .cast(RestaurantDto.class)
                        .doOnSubscribe(s -> log.info("No restaurants found for price ranges: {}", prices)))
                .doOnSubscribe(s -> log.info("Init search by price ranges: {}", prices))
                .doOnNext(r -> log.info("Found restaurant: {}", r.getName()))
                .onErrorResume(e -> {
                    log.error("Error searching by price ranges: {}", prices, e);
                    return Flux.empty();
                });
    }

    @Override
    public Flux<RestaurantDto> readByAddressCity(String city) {
        return this.restaurantRepository.findByAddressCity(city)
                .doOnSubscribe(s -> log.info("Init search by city: {}", city))
                .doOnNext(r -> log.info("Found restaurant: {}", r.getName()))
                .onErrorResume(e -> {
                    log.error("Error searching by city: {}", city, e);
                    return Flux.empty();
                });
    }
}
