package com.celada.eatshub.catalog.service.definition;

import com.celada.eatshub.catalog.domain.RestaurantResponse;
import com.celada.eatshub.catalog.domain.enums.PriceRange;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RestaurantService {
    Flux<RestaurantResponse> readAll();

    Flux<RestaurantResponse> readByCuisineType(String cuisineType);

    Flux<RestaurantResponse> readByNameStartingWithIgnoreCase(String name);

    Flux<RestaurantResponse> readByPriceRangeIn(List<PriceRange> prices);

    Flux<RestaurantResponse> readByAddressCity(String city);
}
