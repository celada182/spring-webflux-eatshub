package com.celada.eatshub.catalog.repository;

import com.celada.eatshub.catalog.domain.enums.PriceRange;
import com.celada.eatshub.catalog.repository.model.RestaurantDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantDto, UUID> {
    Flux<RestaurantDto> findByCuisineType(String cuisineType);
    Flux<RestaurantDto> findByNameStartingWithIgnoreCase(String name);
    Flux<RestaurantDto> findByPriceRangeIn(List<PriceRange> prices);
    Flux<RestaurantDto> findByAddressCity(String city);
}
